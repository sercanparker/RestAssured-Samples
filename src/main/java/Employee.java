/**
 * @author sercansensulun on 31.05.2020.
 */
public class Employee {

    private int id;
    private String name;
    private String surname;
    private int age;
    private String role;


    public Employee(int id, String name, String surname, int age, String role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.role = role;
    }

    public Employee(){

    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getSurname() {
        return surname;
    }

    public void setId(int id) {
        this.id = id;

    }

    public Employee setAge(int age) {
        this.age = age;
        return this;
    }

    public Employee setName(String name) {
        this.name = name;
        return this;
    }

    public Employee setRole(String role) {
        this.role = role;
        return this;
    }

    public Employee setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public static Employee build(){
        return new Employee(0,"testname", "testsurname", 20, "STUDENT");
    }
}
