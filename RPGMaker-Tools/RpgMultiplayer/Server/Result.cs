using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using JsonSerializer = RestSharp.Serializers.JsonSerializer;

namespace RpgMulti.Server
{
    public class Result
    {
        public bool Success { get; }
        public bool Error { get; }
        public string Msg { get; }
        public object Response { get; }

        private Result(bool success, bool error, string msg, object response)
        {
            this.Success = success;
            this.Error = error;
            this.Msg = msg;
            this.Response = response;
        }

        public static string AsJson(bool success, bool error, string msg, object obj = null)
        {
            var res = new Result(success, error, msg.Replace("\"","'"), obj);
            return res.ToJson();
        }

        public static string Exception(Exception e)
        {
            return AsJson(false, true, e.Message, e);
        }

        private string ToJson()
        {
            var json = JsonConvert.SerializeObject(this);
            return json;
        }
    }
}
