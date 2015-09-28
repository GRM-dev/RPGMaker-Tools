using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;
using RPGMVA_Insider.Database;

namespace RPGMVA_Insider_Test
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Out.WriteLine("Starting tests ...");
            var conn = SqlDbConnection.GetConnection();
            try
            {
                AddTempValue(conn, "key2drj6r6j", "firstCv");
                UpdateTempValue(conn, "key2drj6r6j", "seconfV");
                ListTempValues(conn);
                DeleteTempValue(conn, "key2drj6r6j");
                //AddPlayer(conn, "player1", "pswd123", new Random().Next(10000, 50000) + "slt");
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            finally
            {
                conn.Close();
            }

            Console.Out.WriteLine("Press any key to close ...");
            Console.ReadKey();
        }

        private static void DeleteTempValue(SqlDbConnection conn, string key)
        {
            Console.WriteLine("Delete Temp");
            conn.ExecuteNonQuery(Query.DeleteTempValue.Get(), new QProps { { "key", key } });
        }

        private static void AddPlayer(SqlDbConnection conn, string name, string password, string salt)
        {
            Console.WriteLine("Add Player");
            conn.ExecuteNonQuery(Query.AddPlayer.Get(), new QProps { { "name", name }, { "pswd", password }, { "salt", salt } });
        }

        private static void AddTempValue(SqlDbConnection conn, string key, string value)
        {
            Console.WriteLine("Add Temp");
            conn.ExecuteNonQuery(Query.AddTempValue.Get(), new QProps { { "key", key }, { "value", value } });
        }

        private static void UpdateTempValue(SqlDbConnection conn, string key, string value)
        {
            Console.WriteLine("Update Temp");
            conn.ExecuteNonQuery(Query.UpdateTempValue.Get(), new QProps { { "key", key }, { "value", value } });
        }

        private static void ListTempValues(SqlDbConnection conn)
        {
            Console.WriteLine("List Temp");
            var list = conn.ExecuteQuery(Query.GetTempValues.Get(), null);
            foreach (var tV in list)
            {
                Console.WriteLine(tV.Key + ": " + tV.Value);
            }
        }

    }
}
