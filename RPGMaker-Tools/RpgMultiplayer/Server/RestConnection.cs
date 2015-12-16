using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

namespace RpgMulti.Server
{
    public class RestConnection
    {
        private static RestConnection _restC;
        private RestClient _client;

        private RestConnection()
        {
            Properties = LibFileUtil.Read();
            _client = new RestClient(Properties[Property.Url.Name] + ":" + Properties[Property.Port.Name]);
        }

        public RestRequest Get(string uri)
        {
            var request = new RestRequest(uri, Method.GET);
            return request;
        }

        public RestRequest Post(string uri)
        {
            var request = new RestRequest(uri, Method.POST);
            return request;
        }

        public RestRequest Put(string uri)
        {
            var request = new RestRequest(uri, Method.PUT);
            return request;
        }

        public RestRequest Delete(string uri)
        {
            var request = new RestRequest(uri, Method.DELETE);
            return request;
        }

        public void ChangeProperty(Property property, string newValue)
        {
            LibFileUtil.ChangeValue(property.Name, newValue);
        }
        private Dictionary<string, string> Properties { get; set; }
        public static RestConnection Instance
        {
            get { return _restC ?? (_restC = new RestConnection()); }
            set { _restC = value; }
        }
    }
}
