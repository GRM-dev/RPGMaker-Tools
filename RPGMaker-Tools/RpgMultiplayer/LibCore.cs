using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Rpg;
using RpgMulti.Server;

namespace RpgMulti
{
    public class LibCore
    {
        [RpgExport("Lib_Init")]
        public static string InitLib()
        {
            try
            {
                var p = LibFileUtil.Read();
                if (p != null)
                {
                    return Result.AsJson(true, false, "done", p);
                }
                throw new IOException("Cannot got properties from config file!");
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return Result.Exception(e);
            }
        }
    }
}
