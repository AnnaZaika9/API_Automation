package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateContact {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";
    String id;
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        ContactDto contact = ContactDto.builder()
                .name("Mark" )
                .lastName("Mot" )
                .email("mark@ukr.net")
                .phone("0934445222")
                .address("Kiev")
                .description("brother")
                .build();
        String message = given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");

        String[] all = message.split("ID: ");
        id = all[1];
        System.out.println("ID: "+id);
    }
    @Test
    public void updateContactEmailSuccess(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Mark" )
                .lastName("Mot" )
                .email("markmot@mail.ru")
                .phone("0934445222")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));
    }
    @Test
    public void updateContactPhoneSuccess(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Mark" )
                .lastName("Mot" )
                .email("markmot@mail.ru")
                .phone("0934445444")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was updated"));
    }
    @Test
    public void updateContactNameFormatError(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("")
                .lastName("Mot" )
                .email("markmot@mail.ru")
                .phone("0934445444")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.name",containsString("must not be blank"));
    }
    @Test
    public void updateContactEmailFormatError(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Mark")
                .lastName("Mot" )
                .email("markmail.ru")
                .phone("0934445444")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.email",containsString("must be a well-formed email address"));
    }
    @Test
    public void updateContactPhoneFormatError(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Mark")
                .lastName("Mot" )
                .email("mark@ukr.net")
                .phone("09344454448888888")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message.phone",containsString("Phone number must contain only digits! And length min 10, max 15!"));
    }
    @Test
    public void updateContactUnauthorized(){
        ContactDto contact = ContactDto.builder()
                .id(id)
                .name("Maks")
                .lastName("Mot" )
                .email("mark@ukr.net")
                .phone("09344454441")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization","AFF")
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));
    }
    @Test(enabled = false,description = "BUG - 400 code instead of 404")
    public void updateContactNotFound(){
        ContactDto contact = ContactDto.builder()
                .id("3240fc1a-2ba5-4170-a695-fbfa45e269f5")
                .name("Maks")
                .lastName("Mot" )
                .email("mark@ukr.net")
                .phone("09344454441")
                .address("Kiev")
                .description("brother")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(404)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message",containsString("Contact with id: 3240fc1a-2ba5-4170-a695-fbfa45e269f5 not found in your contacts!"));
    }
}
