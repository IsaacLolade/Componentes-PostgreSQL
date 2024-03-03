package cesur.accesodatos.postgresql;

/**
 * Interface to manage any kind of database connections.
 *
 * This interface is meant to be implemented in any piece of software that requires a database connection.
 * Its implementation will depend on the DBMS.
 *
 * @author Carlos Sánchez Recio.
 */
public interface ConnectionInterface {
    /**
     * Method to execute all connection process.
     * @return Boolean value to indicate of the connection was successful or not
     */
    public boolean connectDB();

    /**
     * Method to close connection with the database server
     */
    public void closeConnection();
}