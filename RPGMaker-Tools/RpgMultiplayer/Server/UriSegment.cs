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
        public static UriSegment UserRegPost { get; }=new UriSegment("api/user/{username}");
        public static UriSegment TokenGetPost { get; }=new UriSegment("api/token");

        public string Uri { get; private set; }
    }
}
