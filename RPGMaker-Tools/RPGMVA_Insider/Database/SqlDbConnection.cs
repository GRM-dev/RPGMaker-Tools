using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MySql.Data.MySqlClient;
using Rpg;

namespace RPGMVA_Insider.Database
{
    public class SqlDbConnection
    {
        private MySqlConnection _conn;
        private bool open;

        public static SqlDbConnection GetConnection()
        {
            var server = "localhost";
            var database = "rpgmaker";
            var uid = "rpgmaker";
            var password = "JFMRmrT4ZmNGXUZB";
            var connectionString = "SERVER=" + server + ";" + "DATABASE=" +
                                      database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";
            var connection = new MySqlConnection(connectionString);
            var dbConn = new SqlDbConnection(connection);
            return dbConn;
        }

        private SqlDbConnection(MySqlConnection conn)
        {
            _conn = conn;
        }

        public void ExecuteNonQuery(string query, QProps props)
        {
            if (!open)
            {
                Open();
            }
            Console.WriteLine("Non-Result Query: " + query);
            var cmd = new MySqlCommand(query, _conn) { CommandText = query };
            if (props != null)
            {
                cmd.Prepare();
                foreach (var prop in props)
                {
                    cmd.Parameters.AddWithValue(prop.Key, prop.Value);
                }
            }
            cmd.ExecuteNonQuery();
            Close();
        }

        public Dictionary<string, string> ExecuteQuery(string query, QProps props)
        {
            if (!open)
            {
                Open();
            }
            Console.WriteLine("Query: "+query);
            var cmd = new MySqlCommand(query, _conn);
            if (props != null)
            {
                cmd.Prepare();
                foreach (var prop in props)
                {
                    cmd.Parameters.AddWithValue(prop.Key, prop.Value);
                }
            }
            var list = new Dictionary<string, string>();
            var rdr = cmd.ExecuteReader();
            while (rdr.Read())
            {
                var key = rdr.GetString(1);
                var value = rdr.GetString(2);
                list.Add(key, value);
            }
            Close();
            return list;
        }

        public void Open()
        {
            if (_conn == null) return;
            _conn.Open();
            if (_conn.State == ConnectionState.Open)
            {
                open = true;
            }
        }

        public void Close()
        {
            _conn?.Close();
            open = false;
        }
    }
}
