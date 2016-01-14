using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using RestSharp;
using Rpg;
using RpgMulti.Server;

namespace RpgMulti.Game
{
    public class Token
    {
        [RpgExport("Token_Get")]
        public static string Get(string username, string password)
        {
            try
            {
                var r = RestConnection.Instance;
                var request = r.Post(UriSegment.TokenGetPost);
                request.RequestFormat = DataFormat.Json;
                var jsonReq = new JsonObject
                {
                    {"username", username},
                    {"password", password}
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
                    var resp = jobj["response"];
                    var token = (string)resp["token"];
                    if (token != null && token.Length > 100)
                    {
                        return Result.AsJson(true, false, $"User '{username}' token generated", token);
                    }
                    else
                    {
                        throw new Exception("Received empty/wrong token! \n" + resp["msg"]);
                    }
                }
                else
                {
                    if ((bool)jobj["error"])
                    {
                        throw new Exception((string)jobj["error_msg"]);
                    }
                    else
                    {
                        return Result.AsJson(false, true, $"User '{username}' token generation failed!");
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
