using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Rpg;

namespace RpgMulti.Game
{
    public class Character
    {
        [RpgExport("Char_Name")]
        public static string GetName(int id)
        {
            return "not-implemented";
        }
    }
}
