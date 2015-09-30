using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Rpg;
using RpgMulti.Database;

namespace RpgMulti.Game
{
    public class Save
    {
        private static SqlDbConnection cDB = MmoClientHandler.Cdb;

        public Save()
        {

        }

        public Save(int playerId)
        {
            PlayerId = playerId;
        }

        public Save(int playerId, int mapId, int x, int y) : this(playerId)
        {
            MapId = mapId;
            PosX = x;
            PosY = y;
        }

        [JsonConstructor]
        public Save(int id, int playerId, int mapId, int x, int y) : this(playerId, mapId, x, y)
        {
            Id = id;
        }

        [JsonProperty]
        public int Id { get; private set; }
        [JsonProperty]
        public int PlayerId { get; private set; }
        [JsonProperty]
        public int MapId { get; set; }
        [JsonProperty]
        public int PosX { get; set; }
        [JsonProperty]
        public int PosY { get; set; }


        [RpgExport("Save_Get")]
        public static string Get(string name)
        {
            try
            {
                var id = Player.GetId(name);
                if (id != 0)
                {
                    var qRes = cDB.ExecuteQuery(Query.SaveGet.Get(), new QProps { { "playerId", id } }).FirstOrDefault();
                    var saveObj = qRes.Value;
                    if (saveObj != null && saveObj.Count != 0)
                    {
                        var save = new Save(id);
                        var mapId = 0;
                        var x = 0;
                        var y = 0;
                        var sId = qRes.Key;
                        int.TryParse(saveObj["f_pos_map_id"].ToString(), out mapId);
                        int.TryParse(saveObj["f_pos_x"].ToString(), out x);
                        int.TryParse(saveObj["f_pos_y"].ToString(), out y);
                        save.PosX = x;
                        save.PosY = y;
                        save.MapId = mapId;
                        var objS = save.ToJson();
                        return objS;
                    }
                }
                else
                {
                    return Player.Exists(name);
                }
            }
            catch (Exception e)
            {
                return "Error in Save.class" + e.Message;
            }
            finally
            {
                cDB?.Close();
            }
            return "Error";
        }

        [RpgExport("Save_Create")]
        public static string Create(string name, int mapId, int posX, int posY)
        {
            try
            {
                var id = Player.GetId(name);
                var save = new Save(id, mapId, posX, posY);
                cDB.ExecuteNonQuery(Query.SaveCreate.Get(),
                    new QProps { { "playerId", id }, { "mapId", mapId }, { "posX", posX }, { "posY", posY } });
                return "Done!";
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return e.Message;
            }
        }

        public override bool Equals(object obj)
        {
            if (obj == null)
            {
                return false;
            }

            var p = obj as Save;
            if ((object)p == null)
            {
                return false;
            }

            return (Id == p.Id) && (MapId == p.MapId && PosY == p.PosY && PosX == p.PosX);
        }

        public string ToJson()
        {
            var result = JsonConvert.SerializeObject(this);
            return result;
        }
    }
}
