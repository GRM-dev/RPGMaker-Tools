using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Server
{
    public class UriSegment
    {
        private UriSegment(string uri)
        {
            Uri = uri;
        }

        public static UriSegment UsersGetAll { get; }=new UriSegment("api/users");
        public static UriSegment UserGet { get; }=new UriSegment("api/user/{username}");
        public static UriSegment UserAdd { get; }=new UriSegment("api/user/{username}");

        public string Uri { get; private set; }
    }
}
