using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;
using Rpg;

namespace RPGMVA_Insider
{
    public static class Calc
    {

        [RpgExport("AddNa")]
        public static int AddNa(int n1, int n2)
        {
            return 2 * n1 + n2;
        }


        [RpgExport("SaveValStr")]
        public static string SaveValStr(string name, string value)
        {
            try
            {
                var server = "localhost";
                var database = "rpgmaker";
                var uid = "rpgmaker";
                var password = "JFMRmrT4ZmNGXUZB";
                var connectionString = "SERVER=" + server + ";" + "DATABASE=" +
                                          database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";
                var connection = new MySqlConnection(connectionString);
                try
                {
                    connection.Open();
                    var query = "INSERT INTO temp (`name`, `valueS`) VALUES ('" + name + "', '" + value + "')";
                    var cmd = new MySqlCommand(query, connection);
                    cmd.ExecuteNonQuery();
                    connection.Close();
                    return "Success";
                }
                catch (Exception ex)
                {
                    try
                    {
                        connection.Close();
                    }
                    catch (Exception e)
                    {
                    }
                    return ex.Message;
                }
            }
            catch (Exception ex2)
            {
                return ex2.Message;
            }
        }
    }
}
