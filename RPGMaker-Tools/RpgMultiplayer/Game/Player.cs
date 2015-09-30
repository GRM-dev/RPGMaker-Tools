using System;
using Rpg;
using RpgMulti.Database;

namespace RpgMulti.Game
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
                var existsI = cDB.ExecuteOneQuery(Query.PlayerExists.Get(), new QProps { { "name", name } });
                if (existsI == null)
                {
                    return "Player Not Exists";
                }
                else
                {
                    var id = (int)existsI;
                    return "Done[" + id + "]";
                }
            }
            catch (Exception e)
            {
                return ("Player.Exists" + e.Message);
            }
            finally
            {
                cDB?.Close();
            }
        }

        [RpgExport("Player_Password_Update")]
        public static string UpdatePassword(string name, string pswd)
        {
            try
            {
                cDB.ExecuteNonQuery(Query.PlayerUpdatePassword.Get(), new QProps { { "name", name }, { "pswd", pswd } });
                return "Done!";
            }
            catch (Exception e)
            {
                return ("Player.UpdPswd" + e.Message);
            }
            finally
            {
                cDB?.Close();
            }
        }

        public static int GetId(string name)
        {
            var retExists = Exists(name);
            if (!retExists.Contains("Done[")) return 0;
            var id = GetIdFromBrackets(retExists);
            if (id != 0)
            {
                return id;
            }
            return 0;
        }

        private static int GetIdFromBrackets(string idS)
        {
            var iS = idS.IndexOf("[");
            var iE = idS.IndexOf("]");
            var id = 0;
            var idP = idS.Substring(iS+1, iE - (iS+1));
            int.TryParse(idP, out id);
            return id;
        }
    }
}
