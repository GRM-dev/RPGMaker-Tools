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

        public Save(int playerId)
        {
            PlayerId = playerId;
        }

        public Save(int playerId, int mapId, int x, int y, int dir) : this(playerId)
        {
            MapId = mapId;
            PosX = x;
            PosY = y;
            Dir = dir;
        }

        public Save(int id, int playerId, string playerName, int mapId, int x, int y, int dir) : this(playerId, mapId, x, y, dir)
        {
            Id = id;
            PlayerName = playerName;
        }

        [JsonConstructor]
        public Save(string playerName, int mapId, int posX, int posY, int dir) : this(0, 0, playerName, mapId, posX, posY, dir)
        {

        }

        [JsonProperty]
        public int Id { get; private set; }
        [JsonProperty]
        public int PlayerId { get; private set; }

        public string PlayerName { get; private set; }
        [JsonProperty]
        public int MapId { get; set; }
        [JsonProperty]
        public int PosX { get; set; }
        [JsonProperty]
        public int PosY { get; set; }
        [JsonProperty]
        public int Dir { get; set; }


        [RpgExport("Save_Exists")]
        public static string Exists(string name)
        {
            return "";
        }

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
                        Console.WriteLine("Got save: " + qRes.Key + ".");
                        var mapId = 0;
                        var x = 0;
                        var y = 0;
                        var dir = 0;
                        var sId = qRes.Key;
                        int.TryParse(saveObj["f_pos_map_id"].ToString(), out mapId);
                        int.TryParse(saveObj["f_pos_x"].ToString(), out x);
                        int.TryParse(saveObj["f_pos_y"].ToString(), out y);
                        int.TryParse(saveObj["f_direction"].ToString(), out dir);
                        var save = new Save(id)
                        {
                            PlayerName = name,
                            PosX = x,
                            PosY = y,
                            MapId = mapId,
                            Dir = dir
                        };
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
                return "Error in Save.class! \n" + e.Message;
            }
            finally
            {
                cDB?.Close();
            }
            return "Error";
        }

        [RpgExport("Save_Create")]
        public static string Create(string jSObj)
        {
            Console.WriteLine("Save_Create| " + jSObj);
            Console.WriteLine("Correct expl " + JsonConvert.SerializeObject(new Save(5, 8, "", 7, 6, 2, 8)));
            try
            {
                var save = JsonConvert.DeserializeObject<Save>(jSObj);
                var name = save.PlayerName;
                Console.WriteLine("Save_Create Got From " + name);
                var id = Player.GetId(name);
                Console.Write("with id: " + id);
                save.PlayerId = id;
                var mapId = save.MapId;
                var posX = save.PosX;
                var posY = save.PosY;
                var dir = save.Dir;
                cDB.ExecuteNonQuery(Query.SaveCreate.Get(),
                    new QProps { { "playerId", id }, { "mapId", mapId }, { "posX", posX }, { "posY", posY }, { "dir", dir } });
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
