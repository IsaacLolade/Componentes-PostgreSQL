package cesur.accesodatos.postgresql;

public interface Menu {
    public void executeMenu();
    public void executeFindAllEmployees();
    public void executeFindEmployeeByID();
    public void executeAddEmployee();
    public void executeUpdateEmployee();
    public void executeDeleteEmployee();
    public void executeFindAllDepartments();
    public void executeFindDepartmentByID();
    public void executeAddDepartment();
    public void executeUpdateDepartment();
    public void executeDeleteDepartment();
    public void executeFindEmployeesByDept();
}