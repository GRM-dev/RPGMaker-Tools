using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using MySql.Data.MySqlClient;

namespace RpgMulti.Database
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

        public object ExecuteOneQuery(string query, QProps props)
        {
            if (!open)
            {
                Open();
            }
            Console.WriteLine("One-Result Query: " + query);
            var cmd = new MySqlCommand(query, _conn) { CommandText = query };
            if (props != null)
            {
                cmd.Prepare();
                Console.WriteLine("with params: ");
                foreach (var prop in props)
                {
                    cmd.Parameters.AddWithValue(prop.Key, prop.Value);
                    Console.WriteLine("- " + prop.Key + ": " + prop.Value);
                }
            }
            var obj = cmd.ExecuteScalar();
            Close();
            return obj;
        }

        public Dictionary<int, Dictionary<string, object>> ExecuteQuery(string query, QProps props)
        {
            if (!open)
            {
                Open();
            }
            Console.WriteLine("Dict-Query: " + query);
            var cmd = new MySqlCommand(query, _conn);
            if (props != null)
            {
                cmd.Prepare();
                Console.WriteLine("with params: ");
                foreach (var prop in props)
                {
                    cmd.Parameters.AddWithValue(prop.Key, prop.Value);
                    Console.WriteLine("- " + prop.Key + ": " + prop.Value);
                }
            }
            var list = new Dictionary<int, Dictionary<string, object>>();
            var rdr = cmd.ExecuteReader();
            while (rdr.Read())
            {
                var key = rdr.GetInt32(0);
                var record = new Dictionary<string, object>();
                for (int i = 1; i < rdr.FieldCount; i++)
                {
                    var cName = rdr.GetName(i);
                    var type = rdr.GetFieldType(i);
                    var v=typeof(MySqlDataReader).GetMethod("GetFieldValue").MakeGenericMethod(type).Invoke(rdr, new object[] {i});
                    record.Add(cName, v);
                }
                list.Add(key, record);
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
