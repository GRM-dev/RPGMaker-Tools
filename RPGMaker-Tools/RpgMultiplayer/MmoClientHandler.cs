using RpgMulti.Database;

namespace RpgMulti
{
    public static class MmoClientHandler
    {
        private static SqlDbConnection _conn;

        static MmoClientHandler()
        {
            _conn = SqlDbConnection.GetConnection();

        }

        public static SqlDbConnection Cdb
        {
            get
            {
                return _conn;
            }
        }
    }
}
