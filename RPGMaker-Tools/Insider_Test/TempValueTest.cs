using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MySql.Data.MySqlClient;
using RPGMVA_Insider.Database;

namespace RpgMultiTest
{
    [TestClass]
    public class TempValueTest
    {
        private static SqlDbConnection conn = SqlDbConnection.GetConnection();
        private static readonly string key = "TestKey1";
        private static readonly string value = "TestValue1";

        [TestMethod]
        public void AddTempValue()
        {
            Console.WriteLine("Add Temp");
            conn.ExecuteNonQuery(Query.AddTempValue.Get(), new QProps { { "key", key }, { "value", value } });
        }

        [TestMethod]
        public void UpdateTempValue()
        {
            Console.WriteLine("Update Temp");
            conn.ExecuteNonQuery(Query.UpdateTempValue.Get(), new QProps { { "key", key }, { "value", value } });
        }

        [TestMethod]
        public void DeleteTempValue()
        {
            Console.WriteLine("Delete Temp");
            conn.ExecuteNonQuery(Query.DeleteTempValue.Get(), new QProps { { "key", key } });
        }

        [TestMethod]
        public void ListTempValues()
        {
            Console.WriteLine("List Temp");
            var list = conn.ExecuteQuery(Query.GetTempValues.Get(), null);
            foreach (var tV in list)
            {
                Console.WriteLine(tV.Key + ": " + tV.Value);
            }
        }

        [TestMethod]
        public void Close()
        {
            conn.Close();
        }
    }
}
