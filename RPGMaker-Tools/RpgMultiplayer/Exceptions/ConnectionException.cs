using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Exceptions
{
    public class ConnectionException : IOException
    {
        public ConnectionException(string message) : base("CE! "+message)
        {
            
        }

        public ConnectionException(string message, Exception e) : base("CE! " + message, e)
        {
            
        }
    }
}
