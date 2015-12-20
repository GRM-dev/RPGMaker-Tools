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
        private readonly RestClient _client;

        private RestConnection()
        {
            Properties = LibFileUtil.Read();
            _client = new RestClient(Properties[Property.Url.Name] + ":" + Properties[Property.Port.Name]);
        }

        public IRestResponse Execute(RestRequest request)
        {
            return _client.Execute(request);
        }

        public IRestResponse<T> Execute<T>(RestRequest request) where T : new()
        {
            return _client.Execute<T>(request);
        }

        public RestRequest Get(UriSegment uri)
        {
            var request = new RestRequest(Properties["APP"] + "/" + uri.Uri, Method.GET);
            return request;
        }

        public RestRequest Post(UriSegment uri)
        {
            var request = new RestRequest(uri.Uri, Method.POST);
            return request;
        }

        public RestRequest Put(UriSegment uri)
        {
            var request = new RestRequest(uri.Uri, Method.PUT);
            return request;
        }

        public RestRequest Delete(UriSegment uri)
        {
            var request = new RestRequest(uri.Uri, Method.DELETE);
            return request;
        }

        public void ChangeProperty(Property property, string newValue)
        {
            LibFileUtil.ChangeValue(property.Name, newValue);
        }
        private Dictionary<string, string> Properties { get; set; }
        public static RestConnection Instance => _restC ?? (_restC = new RestConnection());
    }
}
