using Microsoft.VisualStudio.TestTools.UnitTesting;
using RpgMulti.Game;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Game.Tests
{
    [TestClass()]
    public class TokenTests
    {
        [TestMethod()]
        public void GetTest()
        {
            var resp = Token.Get("aae", "AAAAF");
            Assert.IsNotNull(resp);

        }

        [TestMethod()]
        public void IsValidTest()
        {

            var resp = Token.IsValid();
        }
    }
}