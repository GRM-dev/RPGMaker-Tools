using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;

namespace RPGMVA_Insider_Test
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Out.WriteLine("Starting tests ...");
            Console.Out.WriteLine(DBC("gaga","testV"));

            Console.Out.WriteLine("Press any key to close ...");
            Console.ReadKey();
        }

        private static string DBC(string name, string value)
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
