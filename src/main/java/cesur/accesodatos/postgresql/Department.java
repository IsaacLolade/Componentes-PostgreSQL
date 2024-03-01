package cesur.accesodatos.postgresql;

import java.util.Objects;

/**
 * Department class implements the logic
 * necessary to manipulate everything
 * consign in the Departments
 */
public class Department {

    // Class variables
    /**
     * Variable that serves as the identifier of the Department object.
     */
    private Integer depno;

    /**
     *  Variable that serves as the name of the Department object.
     */
    private String name;

    /**
     * Variable that serves as the location of the Department object.
     */
    private String location; //  Variable that serves as the location of the Department object.


    // Constructors
    /**
     *
     * @param depno  Identification of each Department
     * @param name   Name which each Department is going to have
     * @param location Location which each Department is going to have
     */
    public Department(Integer depno, String name, String location) {
        this.depno = depno;
        this.name = name;
        this.location = location;
    }

    /**
     *  Default constructor to initialize the Department without parameters.
     */
    public Department() {
    }

    // GETTERS //

    /**
     *
     * @return Return identification of the Department
     */
    public int getDepno() {
        return depno;
    }

    /**
     *
     * @return Return Name of the Department
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return  Return Location of the Department
     */
    public String getLocation() {
        return location;
    }

    // SETTERS //

    /**
     *Set the department number of the Department
     *
     * @param depno  The Department number to be set
     */
    public void setDepno(int depno) {
        this.depno = depno;
    }

    /**
     *Set the department name of the Department
     *
     * @param name  The Department name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *Set the department location of the Department
     *
     * @param location  The Department location to be set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    // TO STRING //
    /**
     *
     * @return It's going to return us a string representation the specified object
     */
    @Override
    public String toString() {
        return "Department{" +
                "depno=" + depno +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}



