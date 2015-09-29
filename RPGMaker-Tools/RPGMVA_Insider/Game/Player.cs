using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using Rpg;
using RPGMVA_Insider.Database;

namespace RPGMVA_Insider.Game
{
    public static class Player
    {
        private static SqlDbConnection cDB = MmoClientHandler.Cdb;

        [RpgExport("Player_Add")]
        public static string Add(string name, string pswd)
        {
            try
            {
                cDB.ExecuteNonQuery(Query.PlayerAdd.Get(), new QProps { { "name", name }, { "pswd", pswd }, { "salt", null } });
                return "Done!";
            }
            catch (Exception e)
            {
                return ("Player.Add" + e.Message);
            }
            finally
            {
                cDB?.Close();
            }
        }

        [RpgExport("Player_Exists")]
        public static string Exists(string name)
        {
            try
            {
                object existsI=cDB.ExecuteOneQuery(Query.PlayerExists.Get(), new QProps { { "name", name } });
                if (existsI == null)
                {
                    return "Done[0]";
                }
                else
                {
                    return "Done[1]";
                }                
            }
            catch (Exception e)
            {
                return ("Player.Add" + e.Message);
            }
            finally
            {
                cDB?.Close();
            }
        }
    }
}
