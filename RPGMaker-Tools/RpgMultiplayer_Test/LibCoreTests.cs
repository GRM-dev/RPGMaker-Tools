using Microsoft.VisualStudio.TestTools.UnitTesting;
using RpgMulti;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UITest.Common;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace RpgMulti.Tests
{
    [TestClass()]
    public class LibCoreTests
    {
        [TestMethod()]
        public void InitLibTest()
        {
            try
            {
                var resp = LibCore.InitLib();
                Assert.IsNotNull(resp);
                var jo = JObject.Parse(resp);
                var s = (bool)jo["Success"];
                var e = (bool)jo["Error"];
                Assert.IsTrue(s);
                Assert.IsFalse(e);
                var r = (JObject)jo["Response"];
                Assert.IsNotNull(r);
                var p = JsonConvert.DeserializeObject<Dictionary<string, string>>(r.ToString());
                Assert.IsNotNull(p);
                Console.WriteLine(p);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                Assert.Fail(e.Message);
            }
        }
    }
}