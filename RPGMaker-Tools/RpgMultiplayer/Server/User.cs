using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Rpg;

namespace RpgMulti.Server
{
    public class User
    {
        [RpgExport("User.Exists")]
        public bool Exists(string username)
        {
            return true;
        }
    }
}
