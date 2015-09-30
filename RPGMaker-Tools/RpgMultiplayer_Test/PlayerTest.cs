using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using RpgMulti.Database;
using RpgMulti.Game;

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
                var res = Player.Add(name, password);
                Assert.AreEqual(res, "Done!");
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
                var res = Player.UpdatePassword(name, password);
                Assert.AreEqual(res, "Done!");
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
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
