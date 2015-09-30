using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using RpgMulti.Database;
using RpgMulti.Game;

namespace RpgMultiTest
{
    [TestClass]
    public class SaveTest
    {
        private static SqlDbConnection conn = SqlDbConnection.GetConnection();
        private static readonly string name = "TestName1";
        private static readonly Save saveTest = new Save(0, 34, 52, 76);

        [TestMethod]
        public void GetSave()
        {
            try
            {
                var saveS = Save.Get(name);
                Assert.AreNotEqual(saveS, "Error!");
                if (saveS.Contains("{") && saveS.Contains("}"))
                {
                    var save = JsonConvert.DeserializeObject<Save>(saveS);
                    Assert.IsNotNull(save);
                    Assert.AreEqual(save, saveTest);
                }
                else
                {
                    Assert.Fail("GetSave Fail Detail: " + saveS);
                }

            }
            catch (Exception e)
            {
                Assert.Fail("Error! Details: " + e);
            }
        }

        [TestMethod]
        public void CreateSave()
        {
            try
            {
                var res = Save.Create(name, saveTest.MapId, saveTest.PosX, saveTest.PosY);
                Assert.AreEqual(res, "Done!");
            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }

        [TestMethod]
        public void AddMapId()
        {
            try
            {

            }
            catch (Exception e)
            {
                Assert.Fail("Error! " + e.Message);
            }
        }
    }
}
