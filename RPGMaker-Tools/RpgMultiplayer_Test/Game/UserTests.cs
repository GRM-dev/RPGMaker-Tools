using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Newtonsoft.Json.Linq;
using RpgMulti.Game;

namespace RpgMulti.Game.Tests
{
    [TestClass]
    public class UserTests
    {
        [TestMethod]
        public void TestExists()
        {
            var existsResStr = User.Exists("adam");
            var jo = JObject.Parse(existsResStr);
            var success = (bool)jo["Success"];
            Assert.IsTrue(success);
            Assert.IsFalse((bool)jo["Response"]);
            existsResStr = User.Exists("user4test");
            jo = JObject.Parse(existsResStr);
            success = (bool)jo["Success"];
            Assert.IsTrue(success);
            Assert.IsTrue((bool)jo["Response"]);
        }

        [TestMethod]
        public void TestRegister()
        {
            const string un = "user1231Test";
            var exists = User.Exists(un);
            Assert.IsNotNull(exists);
            var objExists = JObject.Parse(exists);
            Assert.IsTrue((bool)objExists["Success"]);
            if (objExists["Response"] != null)
            {
                Assert.IsFalse((bool) objExists["Response"]);
            }
            else
            {
                Assert.Fail("Response is null");
            }
            var regResp = User.Register(un, "g3qwfgqFG");
            Assert.IsNotNull(regResp);
            var objResp = JObject.Parse(regResp);
            Assert.IsTrue((bool) objResp["Success"]);
            exists = User.Exists(un);
            Assert.IsNotNull(exists);
            objExists = JObject.Parse(exists);
            Assert.IsTrue((bool) objExists["Success"]);
            Assert.IsTrue((bool) objExists["Response"]);
        }
    }
}
