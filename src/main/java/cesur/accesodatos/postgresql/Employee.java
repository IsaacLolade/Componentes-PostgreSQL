package cesur.accesodatos.postgresql;

import java.util.Objects;

public class Employee {
    // Class variables
    private Integer empno;
    private String name;
    private String position;
    private Integer depno;

    // Constructors
    public Employee(Integer empno, String name, String position, Integer depno) {
        this.empno = empno;
        this.name = name;
        this.position = position;
        this.depno = depno;
    }

    public Employee() {
    }

    // GETTERS //
    public int getEmpno() {
        return this.empno;
    }

    public String getName() {
        return this.name;
    }

    public String getPosition() {
        return this.position;
    }

    public Integer getDepno() {
        return this.depno;
    }

    // SETTERS //
    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepno(Integer depno) {
        this.depno = depno;
    }

    // TO STRING //
    @Override
    public String toString() {
        return "Employee{" +
                "empno=" + empno +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", depno=" + depno +
                '}';
    }
}
