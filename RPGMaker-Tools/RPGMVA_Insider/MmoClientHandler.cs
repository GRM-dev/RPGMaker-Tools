using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using Rpg;
using RPGMVA_Insider.Database;
using RPGMVA_Insider.Game;

namespace RPGMVA_Insider
{
    public static class MmoClientHandler
    {
        private static SqlDbConnection _conn;

        static MmoClientHandler()
        {
            _conn = SqlDbConnection.GetConnection();

        }

        public static SqlDbConnection Cdb
        {
            get
            {
                return _conn;
            }
        }
    }
}
