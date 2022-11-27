package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddNewContactTestsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }
    @Test(invocationCount = 3)
    public void addNewContactSuccess() {
        int i = new Random().nextInt(1000) + 1000;

        ContactDto contact = ContactDto.builder()
                .name("Bob" + i)
                .lastName("Gai" + i)
                .email("bob" + i + "@mail.ru")
                .phone("0444445" + i)
                .address("Holon")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was added!"));
    }
    @Test
    public void addNewContactWithInvalidName(){
        ContactDto contact = ContactDto.builder()
                .name("")
                .lastName("Snow")
                .email("igor@gmail.com")
                .phone("0534445477")
                .address("Rehovot")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.name",containsString("must not be blank"));
    }
    @Test
    public void addNewContactWithInvalidLastName(){
        ContactDto contact = ContactDto.builder()
                .name("David")
                .lastName("")
                .email("dav@gmail.com")
                .phone("0534445477")
                .address("Rehovot")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.lastName",containsString("must not be blank"));
    }
    @Test
    public void addNewContactWithInvalidEmailFormat(){
        ContactDto contact = ContactDto.builder()
                .name("David")
                .lastName("Row")
                .email("@gmail.com")
                .phone("0534445477")
                .address("Rehovot")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.email",containsString("must be a well-formed email address"));
    }
    @Test
    public void addNewContactWithInvalidPhoneFormat(){
        ContactDto contact = ContactDto.builder()
                .name("David")
                .lastName("Row")
                .email("daw@gmail.com")
                .phone("05344458")
                .address("Rehovot")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.phone",containsString("Phone number must contain only digits! And length min 10, max 15!"));
    }
    @Test
    public void addNewContactWithInvalidAddress(){
        ContactDto contact = ContactDto.builder()
                .name("David")
                .lastName("Row")
                .email("daw@gmail.com")
                .phone("053444589987")
                .address(" ")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.address",containsString("must not be blank"));
    }
    @Test
    public void addNewContactUnauthorized(){
        ContactDto contact = ContactDto.builder()
                .name("David")
                .lastName("Row")
                .email("daw@gmail.com")
                .phone("053444589987")
                .address("Sderot")
                .description("work")
                .build();
        given()
                .header("Authorization","dhvsiy")
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));
    }


    @Test(enabled = false,description = "BUG - it is possible to create a contact with the same data || code 200 --> 409")
    public void addNewContactDublicateContact(){
        ContactDto contact = ContactDto.builder()
                .name("David")
                .lastName("Row")
                .email("daw@gmail.com")
                .phone("053444589987")
                .address("Sderot")
                .description("work")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("error",containsString(""))
                .assertThat().body("message",containsString(""));

    }

}
