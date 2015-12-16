using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;
using System.Xml.Serialization;

namespace RpgMulti.Server
{
     static class LibFileUtil
    {
        private const string Filename = "config.xml";

        public static Dictionary<string, string> Read()
        {
            if (!File.Exists(Filename))
            {
                SaveFile(Property.ToDictionary());
            }
            try
            {
                var doc = XDocument.Load(Filename);
                var rootNodes = doc.Root.DescendantNodes().OfType<XElement>();
                var d = rootNodes.ToDictionary(n => n.Name.ToString(), n => n.Value);
                d = VerifyAllNodes(d);
                return d;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            return Property.ToDictionary();
        }

        private static Dictionary<string, string> VerifyAllNodes(Dictionary<string, string> dictionary)
        {
            var dDefault = Property.ToDictionary();
            var changed = false;
            foreach (var prop in dDefault.Where(prop => !dictionary.ContainsKey(prop.Key)))
            {
                dictionary.Add(prop.Key, prop.Value);
                changed = true;
            }
            var toDelete= dictionary.Where(node => !dDefault.ContainsKey(node.Key)).Select(node => node.Key).ToList();
            foreach (var v in toDelete)
            {
                dictionary.Remove(v);
            }
            if (changed)
            {
                SaveFile(dictionary);
            }
            return dictionary;
        }

        private static void SaveFile(Dictionary<string, string> dictionary)
        {
            var doc = new XDocument(new XElement("root"));
            foreach (var prop in dictionary)
            {
                doc.Root?.Add(new XElement(prop.Key, prop.Value));
            }
            doc.Save(Filename);
        }

        public static void ChangeValue(string name, string newValue)
        {
            var d = Read();
            if (!d.ContainsKey(name))
            {
                return;
            }
            d[name] = newValue;
            SaveFile(d);
        }
    }
}
