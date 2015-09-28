using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using RPGMVA_Insider.Database;

namespace RPGMVA_Insider.Game
{
    public static class Player
    {
        public static string Add(SqlDbConnection conn,string name, string pswd)
        {
            try
            {
                conn.ExecuteQuery(Query.AddPlayer.Get(), new QProps { { "name", name }, { "pswd", pswd }, { "salt", null } });
                return "Done!";
            }
            catch (SqlException e)
            {
               throw new Exception("Player.Add",e);
            }
            finally
            {
                conn?.Close();
            }
        }
    }
}
