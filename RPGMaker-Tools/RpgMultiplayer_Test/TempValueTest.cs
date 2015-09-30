using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using MySql.Data.MySqlClient;
using RpgMulti.Database;

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
            try
            {
                conn.ExecuteNonQuery(Query.TempValueAdd.Get(), new QProps { { "key", key }, { "value", value } });
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }

        [TestMethod]
        public void UpdateTempValue()
        {
            try
            {
                conn.ExecuteNonQuery(Query.TempValueUpdate.Get(), new QProps { { "key", key }, { "value", value } });
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }

        [TestMethod]
        public void DeleteTempValue()
        {
            try
            {
                conn.ExecuteNonQuery(Query.TempValueDelete.Get(), new QProps { { "key", key } });
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }

        [TestMethod]
        public void ListTempValues()
        {
            try
            {
                var list = conn.ExecuteQuery(Query.TempValuesGet.Get(), null);
                foreach (var tV in list)
                {
                    Console.WriteLine(tV.Key + ": " + tV.Value);
                }
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }

        [TestMethod]
        public void Close()
        {
            try
            {
                conn.Close();
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }
    }
}
