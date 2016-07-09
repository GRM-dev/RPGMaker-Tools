using System;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using RestSharp;
using Rpg;
using RpgMulti.Exceptions;
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
                if (response.ErrorMessage == "Unable to connect to the remote server")
                {
                    throw new NoConnectionException("Unable to connect to the server");
                }
                var content = response.Content;
                if (string.IsNullOrEmpty(content))
                {
                    if (response.ErrorException != null)
                    {
                        throw response.ErrorException;
                    }
                    throw new ConnectionException("Received empty response without error. That shouldn't happen!");
                }
                JObject jobj;
                try
                {
                    jobj = JObject.Parse(content);
                }
                catch (Exception e)
                {
                    throw new ServerException("Couldn't parse server response.", e);
                }
                if (response.StatusCode == HttpStatusCode.NotFound)
                {
                    if ((bool)jobj["success"])
                    {
                        return Result.AsJson(true, false, $"User: {username} not found", false);
                    }
                    return Result.AsJson(false, true, $"Couldn't  determine if {username} exists");
                }
                var id = (int)jobj["id"];
                return id > 0 ? Result.AsJson(true, false, $"User: {username} exists", true) : Result.AsJson(false, false, "Something went wrong cause got id: " + id);
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
            try
            {
                var r = RestConnection.Instance;
                var request = r.Post(UriSegment.UserRegPost);
                request.AddUrlSegment("username", username);
                request.RequestFormat = DataFormat.Json;
                var jsonReq = new JsonObject
                {
                    {"username", username},
                    {"password", password},
                    {"email", "not_yet_implemented@test.env"}
                };
                request.AddBody(jsonReq);
                var response = r.Execute(request);
                var content = response.Content;
                if (string.IsNullOrEmpty(content))
                {
                    if (response.ErrorException != null)
                    {
                        throw response.ErrorException;
                    }
                    else
                    {
                        throw new Exception("Received empty response without error. That shouldn't happen!");
                    }
                }
                var jobj = JObject.Parse(content);
                if ((bool)jobj["success"])
                {
                    return Result.AsJson(true, false, $"User: {username} registered", false);
                }
                else
                {
                    if ((bool)jobj["error"])
                    {
                        throw new Exception((string) jobj["error_msg"]);
                    }
                    else
                    {
                        return Result.AsJson(false, true, $"User {username} registration failed!");
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return Result.Exception(e);
            }
        }
    }
}
