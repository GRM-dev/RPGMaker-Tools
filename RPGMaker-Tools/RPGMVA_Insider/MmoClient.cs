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
    public static class MmoClient
    {
        private static SqlDbConnection _conn;

        static MmoClient()
        {
            _conn = SqlDbConnection.GetConnection();

        }

        [RpgExport("AddPlayer")]
        public static string AddPlayer(string name,string pswd)
        {
            try
            {
                return Player.Add(_conn, name, pswd);
            }
            catch (Exception e)
            {
                return e.Message;
            }
        }

    }
}
