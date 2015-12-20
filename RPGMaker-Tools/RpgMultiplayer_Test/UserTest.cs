using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Newtonsoft.Json.Linq;
using RpgMulti.Game;

namespace RpgMultiTest
{
    [TestClass]
    public class UserTest
    {
        [TestMethod]
        public void TestExists()
        {
            var existsResStr = User.Exists("adam");
            Console.WriteLine(existsResStr);
            var jo = JObject.Parse(existsResStr);
            var success = (bool)jo["Success"];
            Assert.IsTrue(success);
            Assert.IsFalse((bool)jo["Response"]);

            existsResStr = User.Exists("user4test");
            Console.WriteLine(existsResStr);
            jo = JObject.Parse(existsResStr);
            success = (bool)jo["Success"];
            Assert.IsTrue(success);
        }
    }
}
