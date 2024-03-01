package cesur.accesodatos.postgresql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 +PostgresSQLDAO class implements the logic
 * necessary to execute each action that users will be using
 * depending on their needs
 * interactions for {@link IDAO} interface methods.
 *
 *
 * @author Isaac Lolade Kehinde Adekeye
 */

public class PostgreSQLDAO implements IDAO, ConnectionInterface, Menu {

    // Terminal outputs and colors
    static final String BLACK_FONT = "\u001B[30m";
    static final String GREEN_FONT = "\u001B[32m";
    static final String WHITE_BG = "\u001B[47m";
    static final String RESET = "\u001B[0m";
    static final String USER_INPUT = String.format("%s%s>%s ", BLACK_FONT, WHITE_BG, RESET);

    /**
     * Regular expression to check if ip address introduced It's valid or not
     */
    private final Pattern ipPattern = Pattern.compile("(localhost)|(\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b)"); // Regular expression to identify if the user either localhost or ip address the right way

    /**
     * Input reader for what the user introduces at the console
     */
    private final InputStreamReader isr = new InputStreamReader(System.in);

    /**
     * Tool to get connected to the database
     */
    private Connection conn;

    /**
     * Determine if the connection was established with the database
     */

    private boolean connectionFlag = false;

    /**
     *
     */
    private boolean executionFlag = true;


    /**
     * Method to retrieve a list of the employees
     * @return List of employees if it  exists on the DB otherwise it returns null
     */
    @Override
    public List<Employee> findAllEmployees() {

        if (this.connectionFlag == false) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            ArrayList<Employee> employees = new ArrayList<>(); // Initialized an Arraylist

            try {
                PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM empleado"); // It performs the necessary consultation to obtain the employees
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    employees.add(new Employee(rs.getInt("empno"), rs.getString("nombre"), rs.getString("puesto"), rs.getInt("depno"))); // It adds into the ArrayList the employees retrieve from the consultation
                }
                ps.close();
                rs.close();

                return employees;
            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Method to find an Employee by is ID
     * @param id Identification of a specific employee
     * @return Returns an Employee
     */
    @Override
    public Employee findEmployeeById(Object id) {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            try {
                PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM empleado where empno = ?");  //  It performs the necessary consultation to obtain a specific Employee
                Integer numero = Integer.parseInt(id.toString()); // It converts the id in case it's not an Integer
                ps.setInt(1, numero);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    return new Employee(rs.getInt("empno"), rs.getString("nombre"), rs.getString("puesto"), rs.getInt("depno")); // It returns the specific Employee
                }
                ps.close();
                rs.close();

            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberFormatException " + exception.getMessage());
            }
        }

        return null;
    }

    /**
     * Method to add an Employee to the database
     * @param employee Object that we'll add to the parameter that was created early, from this we'll be extract the Name, ID and the Role
     */
    @Override
    public void addEmployee(Employee employee) {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            try {
                PreparedStatement ps = this.conn.prepareStatement("INSERT INTO empleado values(?,?,?,?)"); //  It performs the necessary consultation to create an Employee
                ps.setInt(1, employee.getEmpno());
                ps.setString(2, employee.getName());
                ps.setString(3, employee.getPosition());
                ps.setInt(4, employee.getDepno());
                ps.executeUpdate();
                ps.close();

            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberException error reported." + exception.getMessage());
            }
        }
    }

    /**
     * Method that get a specific Employee updated
     * @param id Identification of a specific employee
     * @return Returns the Employee with info updated
     */
    @Override
    public Employee updateEmployee(Object id) {

        BufferedReader br = new BufferedReader(this.isr);

        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {
            try {
                System.out.println("SET the name from the employee, please");
                String name = br.readLine();
                System.out.println("SET the position from the employee, please");
                String position = br.readLine();
                System.out.println("SET the Department number from the employee, please");

                int numero = Integer.parseInt(id.toString());
                int depno = Integer.parseInt(br.readLine());

                PreparedStatement ps = this.conn.prepareStatement("UPDATE empleado set nombre =?, puesto=?, depno=? where empno=?"); //  It performs the necessary consultation to update the information about an Employee
                ps.setString(1, name);
                ps.setString(2, position);
                ps.setInt(3, depno);
                ps.setInt(4, numero);
                ps.executeUpdate();
                ps.close();

                PreparedStatement preparedStatement = this.conn.prepareStatement("SELECT * FROM empleado where empno = ?"); //   It gets the specific Employee
                preparedStatement.setInt(1, numero);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    return new Employee(rs.getInt("empno"), rs.getString("nombre"), rs.getString("puesto"), rs.getInt("depno"));  //   It returns the specific Employee
                }

                preparedStatement.close();
                rs.close();


            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberException error reported." + exception.getMessage());
            } catch (IOException e) {
                System.out.println("ERROR: IOException error reported." + e.getMessage());

            }
        }
        return null;

    }

    /**
     * Method that deletes a specific Employee
     * @param id  Identification of a specific employee
     * @return Returns the object of the Employee deleted
     */
    @Override
    public Employee deleteEmployee(Object id) {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {
            try {
                Employee employee = null; // Initialize Employee in null to be able to add later when we retrieve the specific employee
                Integer numero = Integer.parseInt(id.toString());
                PreparedStatement preparedStatement = this.conn.prepareStatement("SELECT * FROM empleado where empno = ?"); //   It gets the specific Employee
                preparedStatement.setInt(1, numero);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                   employee= new Employee(rs.getInt("empno"), rs.getString("nombre"), rs.getString("puesto"), rs.getInt("depno")); // Add the employee to object Employee initialized previously
                }

                preparedStatement.close();
                rs.close();


                PreparedStatement ps = this.conn.prepareStatement("DELETE FROM empleado where empno = ?");  //   It gets the specific Employee deleted
                ps.setInt(1, numero);
                ps.executeUpdate();
                ps.close();

                return employee;

            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberFormatException " + exception.getMessage());
            }

        }

        return null;
    }

    /**
     *Method to retrieve a list of the departments
     * @return List of departments if it exists on the DB otherwise it returns null
     */
    @Override
    public List<Department> findAllDepartments() {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            ArrayList<Department> departments = new ArrayList<>(); // Initialized an ArrayList where we'll be adding the departments

            try {
                PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM departamento"); // It performs the necessary consultation to obtain the departments
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    departments.add(new Department(rs.getInt("depno"), rs.getString("nombre"), rs.getString("ubicacion"))); // It adds into the ArrayList the departments retrieve from the consultation
                }
                ps.close();
                rs.close();

                return departments;
            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Method to find a Department by is ID
     * @param id Identification of a specific department
     * @return Returns a Department
     */
    @Override
    public Department findDepartmentById(Object id) {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            try {
                PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM departamento where depno = ?"); //  It performs the necessary consultation to obtain a specific Department
                Integer numero = Integer.parseInt(id.toString());// It converts the id in case it's not an Integer
                ps.setInt(1, numero);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    return new Department(rs.getInt("depno"), rs.getString("nombre"), rs.getString("ubicacion")); // It returns the specific Department
                }
                ps.close();
                rs.close();

            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberFormatException " + exception.getMessage());
            }
        }

        return null;
    }


    /**
     * Method to add an Employee to the database
     * @param department Object that we'll add to the parameter that was created early, from this we'll be extract the Name, ID and the Location
     */



    @Override
    public void addDepartment(Department department) {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            try {
                PreparedStatement ps = this.conn.prepareStatement("INSERT INTO departamento values(?,?,?)"); //  It performs the necessary consultation to create a Department
                ps.setInt(1, department.getDepno());
                ps.setString(2, department.getName());
                ps.setString(3, department.getLocation());
                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberException error reported." + exception.getMessage());
            }
        }

    }

    /**
     * Method that get a specific Department updated
     * @param id Identification of a specific department
     * @return Returns the Department with info updated
     */

    @Override
    public Department updateDepartment(Object id) {

        BufferedReader br = new BufferedReader(this.isr);

        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {
            try {
                System.out.println("SET the name from the department, please");
                String name = br.readLine();
                System.out.println("SET the ubication from the department, please");
                String position = br.readLine();

                Integer depno = Integer.parseInt(id.toString());

                PreparedStatement ps = this.conn.prepareStatement("UPDATE departamento set nombre =?, ubicacion=? where depno=?");  //  It performs the necessary consultation to create a Department
                ps.setString(1, name);
                ps.setString(2, position);
                ps.setInt(3, depno);
                ps.executeUpdate();
                ps.close();

                PreparedStatement preparedStatement = this.conn.prepareStatement("SELECT * FROM departamento where depno = ?"); // It performs the necessary consultation to obtain a specific Department
                preparedStatement.setInt(1, depno);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    return new Department(rs.getInt("depno"), rs.getString("nombre"), rs.getString("ubicacion")); // It returns the specific Department
                }

                preparedStatement.close();
                rs.close();


            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberException error reported." + exception.getMessage());
            } catch (IOException e) {
                System.out.println("ERROR: IOException error reported." + e.getMessage());

            }
        }
        return null;
    }


    /**
     * Method that deletes a specific Department
     * @param id  Identification of a specific department
     * @return Returns the object of the Department deleted
     */
    @Override
    public Department deleteDepartment(Object id) {
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            try {
                Department d = null; // Initialized object Department in null to be able to add the one retrieved
                Integer numero = Integer.parseInt(id.toString());
                PreparedStatement preparedStatement = this.conn.prepareStatement("SELECT * FROM departamento where depno = ?"); //   It gets the specific Department
                preparedStatement.setInt(1, numero);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                   d = new Department(rs.getInt("depno"), rs.getString("nombre"), rs.getString("ubicacion")); //   It assigns the department retrieved to the object d
                }

                preparedStatement.close();
                rs.close();

                PreparedStatement ps = this.conn.prepareStatement("DELETE FROM departamento where depno = ?");  //   It gets the specific Department deleted

                ps.setInt(1, numero);
                ps.executeUpdate();
                ps.close();
                return d ;
            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberFormatException " + exception.getMessage());
            }
        }

        return null;
    }

    /**
     * Method to get employees from the same Department
     * @param idDept Identification for a specific Department
     * @return A list of employees from specific Department
     */

    @Override
    public List<Employee> findEmployeesByDept(Object idDept) {

        ArrayList<Employee> employeeArrayList = new ArrayList<>(); // Initialize an ArrayList to be able to add the
        if (!this.connectionFlag) {
            System.out.println("ERROR, there's no connection to the Database. Please try using method connectDB() before trying any other thing");
        } else {

            try {
                PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM empleado where depno = ?"); // It performs the necessary consultation to obtain a specific employees for a specific Department
                Integer numero = Integer.parseInt(idDept.toString());
                ps.setInt(1, numero);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    employeeArrayList.add(new Employee(rs.getInt("empno"), rs.getString("nombre"), rs.getString("puesto"), rs.getInt("depno"))); // Adds the employees to the ArrayList employeeArrayList
                }
                ps.close();
                rs.close();

                return employeeArrayList;
            } catch (SQLException e) {
                System.out.println("ERROR: SQLException error reported " + e.getMessage());
            } catch (NumberFormatException exception) {
                System.out.println("ERROR: NumberFormatException " + exception.getMessage());
            }
        }

        return null;
    }

    /**
     * Method to get access to the database by introducing the values needed to make that possible
     * @return True if the connection was made in case it wasn't you won't get access to the database
     */
    @Override
    public boolean connectDB() {
        BufferedReader reader = new BufferedReader(this.isr);

        try {

            // StringBuilder for String connection
            StringBuilder connectionBuilder = new StringBuilder();
            connectionBuilder.append("jdbc:postgresql://"); // Append DB DRIVER

            System.out.println("Insert the Database server IP:"); // DB Server IP
            System.out.print(USER_INPUT);
            String serverIp = reader.readLine();
            if (!ipPattern.matcher(serverIp).matches()) {
                System.err.println("ERROR: The provided IP address is not valid");
                return false;
            }
            connectionBuilder.append(serverIp);
            System.out.println("Insert the Database server PORT:"); // DB Server PORT
            System.out.print(USER_INPUT);
            String serverPort = reader.readLine();
            connectionBuilder.append(":").append(serverPort).append("/");
            System.out.println("Insert the Database NAME (case sensitive!):"); // DB Name
            System.out.print(USER_INPUT);
            String dbName = reader.readLine();
            connectionBuilder.append(dbName);
            System.out.println("LOGIN: indicate the username and password");
            System.out.print("username> "); // DB Username
            String username = reader.readLine();
            System.out.print("password> "); // DB Password
            String passwd = reader.readLine();


            conn = DriverManager.getConnection(connectionBuilder.toString(), username, passwd); // add URL that will make the connection to the database possible

            if (conn != null) { // Check if the connection was actually successful or not
                this.connectionFlag = true;
                return true;
            } else {
                System.out.println("ERROR trying to get connected to database, some of your entries may not be good");
                return false;
            }

        } catch (IOException e) {
            System.err.println("ERROR: IOException error reported: " + e.getMessage());
        } catch (SQLException exception) {
            System.out.println("ERROR: SQLException error reported " + exception.getMessage());
        }
        return false;
    }

    /**
     * Method to close the connection to the database
     */
    @Override
    public void closeConnection() {
        try {
            if (this.connectionFlag) {
                this.conn.close();
                System.out.printf("%s- Database connection closed -%s", GREEN_FONT, RESET);
            }
        } catch (SQLException exception) {
            System.out.println("Error: SQLException error reported: " + exception.getMessage());
        }
    }

    @Override
    public void executeMenu() {
        BufferedReader reader = new BufferedReader(this.isr); // At this point the Stream is still opened -> At finally block I'll close it
        try {
            while (this.executionFlag) {
                System.out.printf("%s%s- WELCOME TO THE COMPANY -%s\n", "\u001B[46m", BLACK_FONT, RESET);
                System.out.println("Select an option:" +
                        "\n\t1) List all Employees" +
                        "\n\t2) Find Employee by its ID" +
                        "\n\t3) Add new Employee" +
                        "\n\t4) Update Employee" +
                        "\n\t5) Delete Employee" +
                        "\n\t6) List all Departments" +
                        "\n\t7) Find Department by its ID" +
                        "\n\t8) Add new Department" +
                        "\n\t9) Update Department" +
                        "\n\t10) Delete Department" +
                        "\n\t11) Find Employees by Department" +
                        "\n\t0) Exit program");
                System.out.print(USER_INPUT);
                String optStr = reader.readLine();
                if (optStr.isEmpty()) {
                    System.err.println("ERROR: Please indicate the option number");
                    continue;
                } else if (!optStr.matches("\\d{1,2}")) {
                    System.err.println("ERROR: Please provide a valid input for option! The input must be an Integer value");
                    continue;
                }
                int opt = Integer.parseInt(optStr);
                switch (opt) {
                    case 1 -> executeFindAllEmployees();
                    case 2 -> executeFindEmployeeByID();
                    case 3 -> executeAddEmployee();
                    case 4 -> executeUpdateEmployee();
                    case 5 -> executeDeleteEmployee();
                    case 6 -> executeFindAllDepartments();
                    case 7 -> executeFindDepartmentByID();
                    case 8 -> executeAddDepartment();
                    case 9 -> executeUpdateDepartment();
                    case 10 -> executeDeleteDepartment();
                    case 11 -> executeFindEmployeesByDept();
                    case 0 -> this.executionFlag = false;
                    default -> System.err.println("Please provide a valid option");
                }
            }
        } catch (IOException ioe) {
            System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error on reader close reported: " + ioe.getMessage());
            }
            closeConnection();
        }
        System.out.printf("%s%s- SEE YOU SOON -%s\n", "\u001B[46m", BLACK_FONT, RESET); // Program execution end
    }


    @Override
    public void executeFindAllEmployees() {

        if (this.connectionFlag) {
            String row = "+" + "-".repeat(7) + "+" + "-".repeat(16) + "+" + "-".repeat(16) + "+" + "-".repeat(7) + "+";
            List<Employee> employees = this.findAllEmployees();
            if (employees != null) {
                System.out.println(row);
                System.out.printf("| %-5s | %-14s | %-14s | %-5s |\n", "EMPNO", "NOMBRE", "PUESTO", "DEPNO");
                System.out.println(row);
                for (Employee e : employees) {
                    System.out.printf("| %-5s | %-14s | %-14s | %-5s |\n", e.getEmpno(), e.getName(), e.getPosition(), e.getDepno());
                }
                System.out.println(row);
            } else {
                System.out.println("There are currently no Employees stored");
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeFindEmployeeByID() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Employee's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Employee ID. Employee's ID are Integer values");
                    return;
                }
                Employee returnEmp = this.findEmployeeById(Integer.parseInt(input));
                if (returnEmp != null) {
                    System.out.println("Employee's information:");
                    System.out.println(returnEmp.toString());
                } else { // There is no Employee with the indicated ID
                    System.out.println("There is no Employee with EMPNO " + input);
                }
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeAddEmployee() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert new Employee's ID:");
                System.out.print(USER_INPUT);
                String id = reader.readLine();
                if(!id.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Employee ID. Employee's ID are Integer values");
                    return;
                } else if (findEmployeeById(Integer.parseInt(id)) != null) { // There is already an Employee with that ID
                    System.err.println("ERROR: There is already an Employee with the same ID");
                    return;
                }
                System.out.println("Insert new Employee's NAME:");
                System.out.print(USER_INPUT);
                String name = reader.readLine();
                if(name.isEmpty()) {
                    System.err.println("ERROR: You can't leave the information empty");
                    return;
                }
                System.out.println("Insert new Employee's ROLE:");
                System.out.print(USER_INPUT);
                String role = reader.readLine();
                if(role.isEmpty()) {
                    System.err.println("ERROR: You can't leave the information empty");
                    return;
                }
                System.out.println("Insert new Employee's DEPNO:");
                System.out.print(USER_INPUT);
                String depno = reader.readLine();
                if(!depno.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Department ID. Departments' ID are Integer values");
                    return;
                } else if (findDepartmentById(Integer.parseInt(depno)) == null) { // There is no Department with introduced DEPNO
                    System.err.println("ERROR: There is no Department with DEPNO " + depno);
                    return;
                }
                // Everything is good to execute the method
                Employee newEmployee = new Employee(Integer.parseInt(id), name, role, Integer.parseInt(depno)); // Create Employee object
                this.addEmployee(newEmployee);
                System.out.printf("%sNew Employee added successfully!%s\n", GREEN_FONT, RESET);
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeUpdateEmployee() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Employee's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Employee ID. Employee's ID are Integer values");
                    return;
                }
                Employee returnEmp = this.findEmployeeById(Integer.parseInt(input));
                if (returnEmp == null) { // Check if there is an Employee with the indicated ID
                    System.out.println("There is no Employee with EMPNO " + input);
                    return;
                }
                // Execute IDAO method
                Employee updated = updateEmployee(Integer.parseInt(input));
                System.out.println(updated.toString());
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeDeleteEmployee() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Employee's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Employee ID. Employee's ID are Integer values");
                    return;
                }
                Employee returnEmp = this.findEmployeeById(Integer.parseInt(input));
                if (returnEmp == null) { // Check if there is an Employee with the indicated ID
                    System.out.println("There is no Employee with EMPNO " + input);
                    return;
                }
                // Execute IDAO method
                Employee deleted = deleteEmployee(Integer.parseInt(input));
                System.out.println(deleted.toString());
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeFindAllDepartments() {
        if (this.connectionFlag) {
            String row = "+" + "-".repeat(7) + "+" + "-".repeat(20) + "+" + "-".repeat(16) + "+";
            List<Department> departments = this.findAllDepartments();
            if(departments != null) {
                System.out.println(row);
                System.out.printf("| %-5s | %-18s | %-14s |\n", "DEPNO", "NOMBRE", "UBICACION");
                System.out.println(row);
                for (Department d : departments) {
                    System.out.printf("| %-5s | %-18s | %-14s |\n", d.getDepno(), d.getName(), d.getLocation());
                }
                System.out.println(row);
            } else {
                System.out.println("There are currently no Department stored");
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeFindDepartmentByID() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Department's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Department ID. Department's ID are Integer values");
                    return;
                }
                Department returnDept = this.findDepartmentById(Integer.parseInt(input));
                if (returnDept != null) {
                    System.out.println("Department's information:");
                    System.out.println(returnDept.toString());
                } else { // There is no Employee with the indicated ID
                    System.out.println("There is no Department with DEPNO " + input);
                }
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeAddDepartment() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert new Department's ID:");
                System.out.print(USER_INPUT);
                String depno = reader.readLine();
                if(!depno.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Department ID. Department's ID are Integer values");
                    return;
                } else if (findDepartmentById(Integer.parseInt(depno)) != null) { // There is already an Employee with that ID
                    System.err.println("ERROR: There is already an Department with the same ID");
                    return;
                }
                System.out.println("Insert new Department's NAME:");
                System.out.print(USER_INPUT);
                String name = reader.readLine();
                if(name.isEmpty()) {
                    System.err.println("ERROR: You can't leave the information empty");
                    return;
                }
                System.out.println("Insert new Department's LOCATION:");
                System.out.print(USER_INPUT);
                String location = reader.readLine();
                if(location.isEmpty()) {
                    System.err.println("ERROR: You can't leave the information empty");
                    return;
                }
                // Everything is good to execute the method
                Department newDepartment = new Department(Integer.parseInt(depno), name, location); // Create Employee object
                this.addDepartment(newDepartment);
                System.out.printf("%sNew Department added successfully!%s\n", GREEN_FONT, RESET);
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeUpdateDepartment() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Department's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Department ID. Department's ID are Integer values");
                    return;
                }
                Department returnDept = this.findDepartmentById(Integer.parseInt(input));
                if (returnDept == null) { // Check if there is an Employee with the indicated ID
                    System.out.println("There is no Department with DEPNO " + input);
                    return;
                }
                // Execute IDAO method
                Department updated = updateDepartment(Integer.parseInt(input));
                System.out.println(updated.toString());
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeDeleteDepartment() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Department's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Department ID. Department's ID are Integer values");
                    return;
                }
                Department returnDept = this.findDepartmentById(Integer.parseInt(input));
                if (returnDept == null) { // Check if there is an Employee with the indicated ID
                    System.out.println("There is no Department with DEPNO " + input);
                    return;
                }
                // Execute IDAO method
                Department deleted = deleteDepartment(Integer.parseInt(input));
                System.out.println(deleted.toString());
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }
    }

    @Override
    public void executeFindEmployeesByDept() {
        if (this.connectionFlag) {
            BufferedReader reader = new BufferedReader(this.isr); // To read user input
            try {
                System.out.println("Insert Department's ID:");
                System.out.print(USER_INPUT);
                String input = reader.readLine();
                if(!input.matches("\\d+")) { // Check if the output is not numeric
                    System.err.println("ERROR: Please provide a valid Department ID. Department's ID are Integer values");
                    return;
                }
                Department returnDept = this.findDepartmentById(Integer.parseInt(input));
                if (returnDept == null) { // Check if there is an Employee with the indicated ID
                    System.out.println("There is no Department with DEPNO " + input);
                    return;
                }
                // Execute IDAO method
                ArrayList<Employee> departmentEmployees = (ArrayList<Employee>) findEmployeesByDept(Integer.parseInt(input));
                String row = "+" + "-".repeat(7) + "+" + "-".repeat(16) + "+" + "-".repeat(16) + "+";
                if(departmentEmployees == null || departmentEmployees.isEmpty()) { // No Employees in Department case
                    System.out.println("There are currently no Employees in the Department");
                } else {
                    System.out.println(row);
                    System.out.printf("| %-5s | %-14s | %-14s |\n", "EMPNO", "NOMBRE", "PUESTO");
                    System.out.println(row);
                    for (Employee e : departmentEmployees) {
                        System.out.printf("| %-5s | %-14s | %-14s |\n", e.getEmpno(), e.getName(), e.getPosition());
                    }
                    System.out.println(row);
                }
            } catch (IOException ioe) {
                System.err.println("ERROR: IOException error reported: " + ioe.getMessage());
            }
        } else {
            System.err.println("ERROR: You must first try to connect to the database with the method .connectDB()");
        }

    }
}
