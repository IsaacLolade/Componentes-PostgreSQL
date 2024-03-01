package cesur.accesodatos.postgresql;

public interface ConnectionInterface {
    public boolean connectDB();
    public void closeConnection();
}