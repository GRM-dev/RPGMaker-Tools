using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Exceptions
{
    public class ServerException : IOException
    {
        public ServerException(string message) : base("NCE! "+message)
        {
            
        }

        public ServerException(string message, Exception e) : base("NCE! " + message, e)
        {
            
        }
    }
}
