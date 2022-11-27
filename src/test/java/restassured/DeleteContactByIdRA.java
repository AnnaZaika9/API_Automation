package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class DeleteContactByIdRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";
    String id;
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        ContactDto contact = ContactDto.builder()
                .name("Anna" )
                .lastName("Fox" )
                .email("anna@ukr.net")
                .phone("0534445222")
                .address("Tel Aviv")
                .description("friend")
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
        System.out.println(id);

    }
    @Test
    public void deleteContactById(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message",containsString("Contact was deleted!"));
    }
    @Test
    public void deleteContactByIdContactAnyFormatError(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/"+"69")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",containsString("Bad Request"))
                .assertThat().body("message",containsString("Contact with id: 69 not found in your contacts!"));
    }
    @Test
    public void deleteContactsByIdUnauthorized(){
        given()
                .header("Authorization","frrrf")
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("error",containsString("Unauthorized"))
                .assertThat().body("message",containsString("JWT strings must contain exactly 2 period characters. Found: 0"));
    }
    @Test(enabled = false,description = "BUG - 400 code instead of 404")
    public void deleteContactsByIdContactNotFound(){
        given()
                .header("Authorization",token)
                .when()
                .delete("contacts/3240fc1a-2ba5-4170-a695-fbfa45e269f5")
                .then()
                .assertThat().statusCode(404)
                .assertThat().body("error",containsString(""))
                .assertThat().body("message",containsString(""));

    }
}
