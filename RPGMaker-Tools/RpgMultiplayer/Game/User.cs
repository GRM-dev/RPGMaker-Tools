using System;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using RestSharp;
using Rpg;
using RpgMulti.Server;

namespace RpgMulti.Game
{
    public class User
    {
        [RpgExport("User_Exists")]
        public static string Exists(string username)
        {
            try
            {
                var r = RestConnection.Instance;
                var request = r.Get(UriSegment.UserGet);
                request.AddUrlSegment("username", username);
                var response = r.Execute(request);
                var content = response.Content;
                var jobj = JObject.Parse(content);
                if (response.StatusCode == HttpStatusCode.NotFound)
                {
                    if ((bool)jobj["success"])
                    {
                        return Result.AsJson(true, false, "User: " + username + " not found", false);
                    }
                    else
                    {
                        return Result.AsJson(false, true, "Couldn't  determine if " + username + " exists");
                    }
                }
                var id = (int)jobj["id"];
                return id > 0 ? Result.AsJson(true, false, "User: " + username + " exists", true) : Result.AsJson(false, false, "Something went wrong cause got id: " + id);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return Result.Exception(e);
            }
        }

        [RpgExport("User_Register")]
        public static string Register(string username, string password)
        {

            return Result.AsJson(false, false, "not implemented");
        }
    }
}
