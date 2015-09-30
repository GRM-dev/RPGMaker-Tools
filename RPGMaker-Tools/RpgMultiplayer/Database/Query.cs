namespace RpgMulti.Database
{
    public class Query
    {
        public string QueryString { get; private set; }

        public static readonly Query TempValueAdd = new Query("INSERT INTO temp (`key`, `value`) VALUES (@key, @value)");
        public static readonly Query TempValueUpdate = new Query("UPDATE temp SET `value` = @value WHERE `key` = @key");
        public static readonly Query TempValuesGet = new Query("SELECT * FROM temp");
        public static readonly Query TempValueDelete = new Query("DELETE FROM temp WHERE `key` = @key");
        public static readonly Query PlayerAdd = new Query("INSERT INTO players (`f_name`, `f_pswd`, `f_salt`) VALUES (@name, @pswd, @salt)");
        public static readonly Query PlayerExists = new Query("SELECT id FROM players WHERE `f_name`= @name");
        public static readonly Query PlayerUpdatePassword = new Query("UPDATE id FROM players WHERE `f_name`= @name");
        public static readonly Query PlayerGetMapId = new Query("SELECT id FROM players WHERE `f_name`= @name");
        public static readonly Query SaveGet = new Query("SELECT * FROM saves WHERE `player_id`= @playerId");
        public static readonly Query SaveCreate = new Query("INSERT INTO saves (`player_id`, `f_pos_map_id`, `f_pos_x`, `f_pos_y`) VALUES (@playerId, @mapId, @posX, @posY)");
        public static readonly Query SaveUpdate = new Query("UPDATE saves SET `player_id` = @playerId, `f_pos_map_id` = @mapId, `f_pos_x` = @poxX, `f_pos_y` = @posY WHERE `id`= @id");


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

