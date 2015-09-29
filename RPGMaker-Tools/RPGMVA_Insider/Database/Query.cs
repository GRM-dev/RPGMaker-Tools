using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RPGMVA_Insider.Database
{
    public class Query
    {
        public string QueryString { get; private set; }

        public static readonly Query AddTempValue = new Query("INSERT INTO temp (`key`, `value`) VALUES (@key, @value)");
        public static readonly Query UpdateTempValue = new Query("UPDATE temp SET `value` = @value WHERE `key` = @key");
        public static readonly Query GetTempValues = new Query("SELECT * FROM temp");
        public static readonly Query DeleteTempValue = new Query("DELETE FROM temp WHERE `key` = @key");
        public static readonly Query PlayerAdd = new Query("INSERT INTO players (`f_name`, `f_pswd`, `f_salt`) VALUES (@name, @pswd, @salt)");
        public static readonly Query PlayerExists = new Query("SELECT id FROM players WHERE `f_name`= @name");


        private Query(string queryString)
        {
            QueryString = queryString;
        }

        public string Get()
        {
            return QueryString;
        }
    }
}

