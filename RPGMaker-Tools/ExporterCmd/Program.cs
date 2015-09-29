using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Rpg;

namespace ExporterCmd
{
    class Program
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
                        Rpg.Program.ExportFromDev(input, output);
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
                    Console.WriteLine("File \""+input +"\" doesn't exists!");
                }
            }
            else
            {
                Console.WriteLine("Bad arguments amount! Should be 2 (was "+args.Length+")");
            }
            Console.ReadKey();
        }
    }
}
