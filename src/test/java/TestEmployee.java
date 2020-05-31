import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;

/**
 * @author sercansensulun on 31.05.2020.
 */
public class TestEmployee {

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        System.setProperty("currenttime", dateFormat.format(new Date()));
    }

    public static final String END_POINT_FOR_EMPLOYEE = "employee";
    private static final String API_ROOT = "http://localhost:3000/";

    protected static RequestSpecification spec;

    private static final Logger LOGGER = Logger.getLogger(TestEmployee.class);

    @Before()
    public void before(){

        LOGGER.info("At the beginning of a test case, there should be no employees on the server.");

        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(API_ROOT)
                .build();
        Employee[] employees = given()
                .spec(spec)
                .when()
                .get(END_POINT_FOR_EMPLOYEE)
                .then()
                .statusCode(200)
                .extract().as(Employee[].class);

        Assert.assertEquals(0, employees.length);

        LOGGER.info("Add new employee, check the employee added successfully.");
        Employee newEmployee = Employee.build();

        given()
                .spec(spec)
                .body(newEmployee)
                .when()
                .post(END_POINT_FOR_EMPLOYEE)
                .then().statusCode(201);

        Employee[] employeesAfterAdded = given()
                .spec(spec)
                .when()
                .get(END_POINT_FOR_EMPLOYEE)
                .then()
                .statusCode(200)
                .extract().as(Employee[].class);

        Assert.assertEquals(1,employeesAfterAdded.length);
        Assert.assertEquals(newEmployee.getAge(), employeesAfterAdded[0].getAge());
        Assert.assertEquals(newEmployee.getName(), employeesAfterAdded[0].getName());
        Assert.assertEquals(newEmployee.getRole(), employeesAfterAdded[0].getRole());
        Assert.assertEquals(newEmployee.getSurname(), employeesAfterAdded[0].getSurname());

    }

    @After
    public void after(){
        LOGGER.info("Remove all new added employees from server.");

        Employee[] employees = given()
                .spec(spec)
                .when()
                .get(END_POINT_FOR_EMPLOYEE)
                .then()
                .statusCode(200)
                .extract().as(Employee[].class);

        for (Employee employee:employees) {
            given()
                    .spec(spec)
                    .when()
                    .delete(END_POINT_FOR_EMPLOYEE + "/" + employee.getId())
                    .then().statusCode(200);
        }
    }

    @Test
    public void test001(){
        LOGGER.info("Get first employee from server and send query for all fields.");

        Employee[] employees = given()
                .spec(spec)
                .when()
                .get(END_POINT_FOR_EMPLOYEE)
                .then()
                .statusCode(200)
                .extract().as(Employee[].class);

        Employee firstEmployee = employees[0];

        Employee[] queriedEmployee = given()
                .spec(spec)
                .when()
                .get(END_POINT_FOR_EMPLOYEE + "?" +
                        "name="+firstEmployee.getName() +
                        "&surname=" + firstEmployee.getSurname() +
                        "&age=" + firstEmployee.getAge() +
                        "&role=" + firstEmployee.getRole() +
                        "&id=" + firstEmployee.getId())
                .then().statusCode(200).extract().as(Employee[].class);

        Assert.assertEquals(1, queriedEmployee.length);

        Assert.assertEquals(firstEmployee.getAge(), queriedEmployee[0].getAge());
        Assert.assertEquals(firstEmployee.getName(), queriedEmployee[0].getName());
        Assert.assertEquals(firstEmployee.getRole(), queriedEmployee[0].getRole());
        Assert.assertEquals(firstEmployee.getSurname(), queriedEmployee[0].getSurname());

    }

    @Test
    public void test002(){
        LOGGER.info("employee age should be bigger than 17.");

        given()
                .spec(spec)
                .body(Employee.build().setAge(17))
                .when()
                .post(END_POINT_FOR_EMPLOYEE)
                .then().statusCode(400);
    }

    @Test
    public void test003(){
        LOGGER.info("Name, Surname and Role are required");

        given()
                .spec(spec)
                .body(Employee.build().setName(null).setRole(null).setSurname(null))
                .when()
                .post(END_POINT_FOR_EMPLOYEE)
                .then().statusCode(400);
    }
}
