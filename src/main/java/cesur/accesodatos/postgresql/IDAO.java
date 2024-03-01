package cesur.accesodatos.postgresql;

import java.util.List;

public interface IDAO {
    public List<Employee> findAllEmployees();

    public Employee findEmployeeById(Object id);

    public void addEmployee(Employee employee);

    public Employee updateEmployee(Object id);


    public Employee deleteEmployee(Object id);

    public List<Department> findAllDepartments();

    public Department findDepartmentById(Object id);

    public void addDepartment(Department department);

    public Department updateDepartment(Object id);

    public Department deleteDepartment(Object id);

    public List<Employee> findEmployeesByDept(Object idDept);
}
