using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using RPGMVA_Insider.Database;

namespace RpgMultiTest
{
    [TestClass]
    public class PlayerTest
    {
        private static SqlDbConnection conn = SqlDbConnection.GetConnection();
        private static readonly string name = "TestName1";
        private static readonly string password = "TestPswd1";
        private static readonly string salt = "TestSalt1";

        [TestMethod]
        public void PlayerAdd()
        {
            try
            {
                conn.ExecuteNonQuery(Query.PlayerAdd.Get(), new QProps { { "name", name }, { "pswd", password }, { "salt", salt } });
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }

        [TestMethod]
        public void UpdatePlayer()
        {
            try
            {

            }
            catch (Exception e)
            {

            }
        }

        [TestMethod]
        public void PlayerExists()
        {
            try
            {
                var obj = conn.ExecuteOneQuery(Query.PlayerExists.Get(), new QProps { { "name", name } });
                Assert.IsNotNull(obj);
            }
            catch (Exception e)
            {
                Assert.Fail(e.Message);
            }
        }

        [TestMethod]
        public void DeletePlayer()
        {
            try
            {

            }
            catch (Exception e)
            {

            }
        }

        [TestMethod]
        public void Close()
        {
            conn.Close();
        }
    }
}
