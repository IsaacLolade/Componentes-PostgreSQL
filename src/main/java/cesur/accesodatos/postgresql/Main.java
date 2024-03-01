package cesur.accesodatos.postgresql;

public class Main {
    public static void main(String[] args) {
        PostgreSQLDAO test = new PostgreSQLDAO();
        if (test.connectDB()) test.executeMenu();
    }
}

