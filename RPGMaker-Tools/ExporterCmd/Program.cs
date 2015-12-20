using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Rpg;
using Rpg.Properties;

namespace ExporterCmd
{
    public class Program
    {
        private static void Main(string[] args)
        {
            if (args.Length == 2)
            {
                var input = args[0];
                var output = args[1];
                if (File.Exists(input))
                {
                    try
                    {
                        //Rpg.Program.ExportFromDev(input, output);
                        Settings.Default.Architecture = 0;
                        Settings.Default.LastOutput = output;
                        Settings.Default.Save();

                        Exporter.DataReceived += Exporter_DataReceived;
                        Exporter.Build(input);
                        Console.WriteLine("Done!");
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e.Message);
                        Console.WriteLine("Error!");
                    }
                }
                else
                {
                    Console.WriteLine("File \"" + input + "\" doesn't exists!");
                }
            }
            else
            {
                Console.WriteLine("Bad arguments amount! Should be 2 (was " + args.Length + ")");
            }
            Console.ReadKey();
        }

        private static void Exporter_DataReceived(object sender, DataReceivedEventArgs e)
        {
            Console.WriteLine((e.Data));
        }
    }
}
