package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RegistrationTestsRA {
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }
    @Test
    public void registrationSuccess(){
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto auth = AuthRequestDto.builder().username("silver"+i+"@ukr.net").password("111#Silver"+i).build();

       String token = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .path("token");
        System.out.println("Token: " +token);
    }
    @Test
    public void registrationWrongUserName(){

        AuthRequestDto auth = AuthRequestDto.builder().username("silverukr.net").password("111#Silver").build();

       given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
               .assertThat().body("message.username", containsString("must be a well-formed email address"));
    }
    @Test
    public void registrationWrongPassword(){

        AuthRequestDto auth = AuthRequestDto.builder().username("silver@ukr.net").password("111#silver").build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error", containsString("Bad Request"))
                .assertThat().body("message.password", containsString("At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]"));
    }
    @Test
    public void unsuccessRegistrationDuplicateUser(){
        AuthRequestDto auth = AuthRequestDto.builder().username("silver1524@ukr.net").password("111#Silver1524").build();
        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("error",containsString("Conflict"))
                .assertThat().body("message",containsString("User already exists"));
    }
}
