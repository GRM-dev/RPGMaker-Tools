using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Exceptions
{
    public class NoConnectionException : IOException
    {
        public NoConnectionException(string message) : base("NCE! "+message)
        {
            
        }

        public NoConnectionException(string message, Exception e) : base("NCE! " + message, e)
        {
            
        }
    }
}
