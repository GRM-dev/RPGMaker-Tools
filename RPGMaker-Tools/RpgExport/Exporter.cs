using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Linq.Expressions;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Text.RegularExpressions;
using System.Threading;
using System.Windows.Forms;

namespace Rpg
{
    /// <summary>
    /// Static class for decompiling/compiling assemblies with exported functions
    /// </summary>
    public static class Exporter
    {
        #region Events/Delegates

        public static event DataReceivedEventHandler DataReceived;

        #endregion

        #region Fields

        private static ParserState _state;
        private static AssemblyData _data;
        private static List<string> _ilFile;
        private static int _exportPosition;
        private static Dictionary<string, Dictionary<string, KeyValuePair<string, string>>> _dic;

        #endregion

        #region Compile/Decompile

        private static int Decompile(string filename)
        {
            var info = new ProcessStartInfo
            {
                FileName = Properties.Settings.Default.IldasmPath,
                Arguments = string.Format("/nobar /out:{0}.il {0}.dll", filename),
                UseShellExecute = false,
                CreateNoWindow = true,
                RedirectStandardOutput = true,
                RedirectStandardError = true
            };
            using (var proc = new Process())
            {
                proc.StartInfo = info;
                proc.EnableRaisingEvents = true;
                proc.OutputDataReceived += proc_OutputDataReceived;
                proc.Start();
                proc.BeginErrorReadLine();
                proc.BeginOutputReadLine();
                proc.WaitForExit();
                return proc.ExitCode;
            }
        }

        private static int Compile(string filename)
        {
            // Create arguments
            var output = Properties.Settings.Default.LastOutput;
            if (string.IsNullOrEmpty(output))
                output = Properties.Settings.Default.LastInput;
            var args = new List<string>()
            {
                /*"/nologo",*/
                $"/out:\"{output}\"",
                $"\"{filename}.il\"",
            };
            if (Path.GetExtension(output) == ".dll")
                args.Add("/DLL");
            var resourceFile = filename + ".res";
            if (File.Exists(resourceFile))
                args.Add($"/resource={resourceFile}");
            if (Properties.Settings.Default.Optimize)
                args.Add("/optimize");
            switch (Properties.Settings.Default.Architecture)
            {
                case 1:
                    args.Add("/32bitpreferred");
                    break;
                case 2:
                    args.Add("/pe64");
                    break;
            }
            var argsJ = string.Join(" ", args.ToArray());
            Console.WriteLine(Properties.Settings.Default.IlasmPath+" | "+argsJ);
            // Create Process 
            var info = new ProcessStartInfo
            {
                FileName = Properties.Settings.Default.IlasmPath,
                Arguments = argsJ,
                UseShellExecute = false,
                CreateNoWindow = true,
                RedirectStandardOutput = true,
                RedirectStandardError = true
            };
            using (var proc = new Process())
            {
                proc.StartInfo = info;
                proc.EnableRaisingEvents = true;
                proc.OutputDataReceived += proc_OutputDataReceived;
                proc.Start();
                proc.BeginErrorReadLine();
                proc.BeginOutputReadLine();
                proc.WaitForExit();
                return proc.ExitCode;
            }
        }

        private static void proc_OutputDataReceived(object sender, DataReceivedEventArgs e)
        {
            DataReceived?.Invoke(sender, e);
        }

        #endregion

        #region Parse

        private static bool ParseNormal(string line)
        {
            if (line.StartsWith(".class"))
            {
                _state = ParserState.ClassDeclaration;
                _data.ClassDeclaration = line;
                return false;
            }
            else if (line.StartsWith(".assembly extern RpgExport"))
            {
                _state = ParserState.DeleteExportDependency;
                return false;
            }
            return true;
        }

        private static bool ParseClassDeclaration(string line)
        {
            if (line.StartsWith("{"))
            {
                _state = ParserState.Class;
                var classname = string.Empty;
                var reg = new Regex(@".+\s+([^\s]+) extends \[.*");
                var m = reg.Match(_data.ClassDeclaration);
                if (m.Groups.Count > 1)
                    classname = m.Groups[1].Value;
                classname = classname.Replace("'", "");
                if (_data.ClassNames.Count > 0)
                    classname = _data.ClassNames.Peek() + "+" + classname;
                _data.ClassNames.Push(classname);
                _ilFile.Add(_data.ClassDeclaration);
            }
            else
            {
                _data.ClassDeclaration += " " + line;
                return false;
            }

            return true;
        }

        private static bool ParseClass(string line)
        {
            if (line.StartsWith(".class"))
            {
                _state = ParserState.ClassDeclaration;
                _data.ClassDeclaration = line;
                return false;
            }
            else if (line.StartsWith(".method"))
            {
                if (!_dic.ContainsKey(_data.ClassNames.Peek())) return true;
                _data.MethodDeclaration = line;
                _state = ParserState.MethodDeclaration;
                return false;
            }
            else if (line.StartsWith("} // end of class"))
            {
                _data.ClassNames.Pop();
                _state = _data.ClassNames.Count > 0 ? ParserState.Class : ParserState.Normal;
            }
            return true;
        }

        private static bool ParseDeleteExportDependency(string line)
        {
            if (line.StartsWith("}"))
                _state = ParserState.Normal;
            return false;
        }

        private static bool ParseMethodDeclaration(string line)
        {
            if (line.StartsWith("{"))
            {
                var reg = new Regex(@"(?<before>[^\(]+(\(\s[^\)]+\))*\s)(?<method>[^\(]+)(?<after>\(.*)");
                var m = reg.Match(_data.MethodDeclaration);
                if (m.Groups.Count > 3)
                {
                    _data.MethodBefore = m.Groups["before"].Value;
                    _data.MethodAfter = m.Groups["after"].Value;
                    _data.MethodName = m.Groups["method"].Value;
                }
                if (_dic[_data.ClassNames.Peek()].ContainsKey(_data.MethodName))
                {
                    _data.MethodPosition = _ilFile.Count;
                    _state = ParserState.MethodProperties;
                }
                else
                {
                    _ilFile.Add(_data.MethodDeclaration);
                    _state = ParserState.Method;
                    _data.MethodPosition = 0;
                }
            }
            else
            {
                _data.MethodDeclaration += " " + line;
                return false;
            }
            return true;
        }

        private static bool ParseMethodProperties(string line)
        {
            if (line.StartsWith(".custom instance void [RpgExport"))
            {
                _state = ParserState.DeleteExportAttribute;
                return false;
            }
            else if (line.StartsWith("// Code"))
            {
                _state = ParserState.Method;
                if (_data.MethodPosition != 0)
                    _ilFile.Insert(_data.MethodPosition, _data.MethodDeclaration);
            }
            return true;
        }

        private static bool ParseMethod(string line)
        {
            if (line.StartsWith("} // end of method"))
                _state = ParserState.Class;
            return true;
        }

        private static bool ParseDeleteExportAttribute(string line)
        {
            if (line.StartsWith(".custom") || line.StartsWith("// Code"))
            {
                var attr = _dic[_data.ClassNames.Peek()][_data.MethodName];
                if (_data.MethodBefore.Contains("marshal( "))
                {
                    var pos = _data.MethodBefore.IndexOf("marshal( ");
                    _data.MethodBefore = _data.MethodBefore.Insert(pos, "modopt([mscorlib]" + attr.Value + ") ");
                    _data.MethodDeclaration = _data.MethodBefore + _data.MethodName + _data.MethodAfter;
                }
                if (_data.MethodPosition != 0)
                    _ilFile.Insert(_data.MethodPosition, _data.MethodDeclaration);
                if (_data.MethodName == "DllMain")
                    _ilFile.Add(" .entrypoint");
                _ilFile.Add($".export [{_exportPosition}] as {_dic[_data.ClassNames.Peek()][_data.MethodName].Key}");
                _exportPosition++;
                _state = ParserState.Method;
                return true;
            }
            return false;
        }

        #endregion

        #region Load/Build

        /// <summary>
        /// Recompiles assembly and exports the specified functions so they
        /// can be invoked by unmanaged programs.
        /// </summary>
        /// <param name="filepath">Full path to the assembly to recompile</param>
        /// <returns>Error code if error occurs, else 0</returns>
        public static void Build(string filepath)
        {
            try
            {
                // Configure paths
                var path = Path.GetDirectoryName(filepath);
                if (string.IsNullOrEmpty(path))
                {
                    throw new Exception("Input file cannot be found.");
                }
                var filename = Path.GetFileNameWithoutExtension(filepath);
                Directory.SetCurrentDirectory(path);
                var ilPath = Path.Combine(path, filename + ".il");
                // Use reflection to load data from assembly
                var domain = AppDomain.CreateDomain("ReflectionOnly");
                domain.ReflectionOnlyAssemblyResolve += new ResolveEventHandler(CurrentDomain_ReflectionOnlyAssemblyResolve);
                domain.SetData("FilePath", filepath);
                domain.DoCallBack(new CrossAppDomainDelegate(Load));
                _dic = (Dictionary<string, Dictionary<string, KeyValuePair<string, string>>>)domain.GetData("dic");
                var exportscount = (int)domain.GetData("ExportCount");
                AppDomain.Unload(domain);
                // Return if no exported methods were found
                Console.Out.WriteLine(_dic);
                if (exportscount == 0)
                {
                    throw new Exception("No exports found within assembly.");
                }

                // Decompile and parse through generated IL file
                _exportPosition = 1;
                Decompile(filename);
                _ilFile = new List<string>();
                _data = new AssemblyData();
                _state = ParserState.Normal;
                // Open IL file and parse
                using (var streamReader = File.OpenText(ilPath))
                {
                    while (!streamReader.EndOfStream)
                    {
                        var line = streamReader.ReadLine();
                        var trimmedLine = line.Trim();
                        var addLine = true;
                        // Branch by current parser state
                        switch (_state)
                        {
                            case ParserState.Normal:
                                addLine = ParseNormal(trimmedLine);
                                break;
                            case ParserState.DeleteExportDependency:
                                addLine = ParseDeleteExportDependency(trimmedLine);
                                break;
                            case ParserState.ClassDeclaration:
                                addLine = ParseClassDeclaration(trimmedLine);
                                break;
                            case ParserState.Class:
                                addLine = ParseClass(trimmedLine);
                                break;
                            case ParserState.MethodDeclaration:
                                addLine = ParseMethodDeclaration(trimmedLine);
                                break;
                            case ParserState.Method:
                                addLine = ParseMethod(trimmedLine);
                                break;
                            case ParserState.MethodProperties:
                                addLine = ParseMethodProperties(trimmedLine);
                                break;
                            case ParserState.DeleteExportAttribute:
                                addLine = ParseDeleteExportAttribute(trimmedLine);
                                break;
                        }
                        if (addLine)
                            _ilFile.Add(line);
                    }
                }
                // Re-write the IL file with modifications
                using (var streamWriter = File.CreateText(ilPath))
                {
                    foreach (var line in _ilFile)
                        streamWriter.WriteLine(line);
                }
                var result= Compile(filename);
                if (result != 0)
                {
                    throw new Exception("Got return code "+result);
                }
            }
            catch (Exception ex)
            {
                DisplayError(ex.Message);
                }
        }

        private static void DisplayError(string msg)
        {
            MessageBox.Show(msg, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        private static void Load()
        {
            var diccc = new Dictionary<CallingConvention, string>
            {
                [CallingConvention.Cdecl] = typeof(CallConvCdecl).FullName,
                [CallingConvention.FastCall] = typeof(CallConvFastcall).FullName,
                [CallingConvention.StdCall] = typeof(CallConvStdcall).FullName,
                [CallingConvention.ThisCall] = typeof(CallConvThiscall).FullName,
                [CallingConvention.Winapi] = typeof(CallConvStdcall).FullName
            };
            var dic = new Dictionary<string, Dictionary<string, KeyValuePair<string, string>>>();
            var assembly = Assembly.ReflectionOnlyLoadFrom((string)AppDomain.CurrentDomain.GetData("FilePath"));
            var types = assembly.GetTypes();
            var exportscount = 0;
            foreach (var type in types)
            {
                var infos = type.FindMembers(MemberTypes.All, BindingFlags.Public | BindingFlags.Static, new MemberFilter((mi, obj) => true), null);
                foreach (var info in infos)
                {
                    var attrs = CustomAttributeData.GetCustomAttributes(info);
                    foreach (var attr in attrs.Where(attr => attr.Constructor.ReflectedType == null || attr.Constructor.ReflectedType.Name == "RpgExport"))
                    {
                        if (!dic.ContainsKey(type.FullName))
                        {
                            dic[type.FullName] = new Dictionary<string, KeyValuePair<string, string>>();
                        }
                        switch (attr.ConstructorArguments.Count)
                        {
                            case 0:
                                dic[type.FullName][info.Name] = new KeyValuePair<string, string>(info.Name, typeof(CallConvStdcall).FullName);
                                break;
                            case 1:
                                dic[type.FullName][info.Name] = new KeyValuePair<string, string>((string)attr.ConstructorArguments[0].Value, typeof(CallConvStdcall).FullName);
                                break;
                            case 2:
                                dic[type.FullName][info.Name] = new KeyValuePair<string, string>((string)attr.ConstructorArguments[0].Value, diccc[(CallingConvention)attr.ConstructorArguments[1].Value]);
                                break;
                            default:
                                break;
                        }
                        exportscount++;
                    }
                }
            }
            AppDomain.CurrentDomain.SetData("ExportCount", exportscount);
            AppDomain.CurrentDomain.SetData("dic", dic);
        }

        private static Assembly CurrentDomain_ReflectionOnlyAssemblyResolve(object sender, ResolveEventArgs args)
        {
            return Assembly.ReflectionOnlyLoad(args.Name);
        }

        #endregion
    }
}
