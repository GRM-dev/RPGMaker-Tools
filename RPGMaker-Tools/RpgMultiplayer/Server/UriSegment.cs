using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Server
{
    public class UriSegment
    {
        private readonly string _uri;

        private UriSegment(string uri)
        {
            _uri = uri;
        }

        public static UriSegment UsersGetAll { get; }=new UriSegment("api/users");
    }
}
