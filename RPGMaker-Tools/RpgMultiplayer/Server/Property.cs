using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RpgMulti.Server
{
    public class Property
    {
        public string Name { get; }
        public string Value { get; set; }

        private Property(string name, string value)
        {
            Name = name;
            Value = value;
            All.Add(this);
        }

        public static Dictionary<string,string> ToDictionary()
        {
            return All.ToDictionary(property => property.Name, property => property.Value);
        }

        public static List<Property> All { get; }=new List<Property>(); 
        public static Property Url { get; } = new Property("URL","localhost");
        public static Property Port { get; } = new Property("PORT","8080");
        public static Property Username { get; }=new Property("USERNAME","");
        public static Property Token { get; }=new Property("TOKEN","");
    }
}
