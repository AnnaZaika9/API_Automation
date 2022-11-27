package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteAllContactRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";
    String id;

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
        ContactDto contact = ContactDto.builder()
                .name("Fred" )
                .lastName("Fox" )
                .email("fred@ukr.net")
                .phone("0534445222")
                .address("Tel Aviv")
                .description("university")
                .build();
        given()
                .header("Authorization",token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("message");

    }
    @Test
    public void deleteAllContactSuccess(){

            given()
                    .header("Authorization",token)
                    .when()
                    .delete("contacts/clear")
                    .then()
                    .assertThat().statusCode(200)
                    .assertThat().body("message",containsString("All contacts was deleted!"));
        }
    @Test
    public void deleteAllContactsUnauthorized(){
        given()
                .header("Authorization","grr")
                .when()
                .delete("contacts/clear")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));

    }

    }

