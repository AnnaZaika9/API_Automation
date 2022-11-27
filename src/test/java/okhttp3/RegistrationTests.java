package okhttp3;

import config.Provider;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrationTests {
    @Test
    public void registrationSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        AuthRequestDto auth = AuthRequestDto.builder().username("silver"+i+"@ukr.net").password("111#Silver"+i).build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        AuthResponseDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(),AuthResponseDto.class);
        System.out.println("token: "+ responseDto.getToken());
    }
    @Test
    public void unsuccessRegistrationWrongEmail() throws IOException {

        AuthRequestDto auth = AuthRequestDto.builder().username("silverukr.net").password("111#Silver").build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);

        Object message =  errorDto.getMessage();
        System.out.println("Message:" + message);

        Assert.assertTrue(message.toString().contains("username=must be a well-formed email address"));

        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
    @Test
    public void unsuccessRegistrationWrongPassword() throws IOException {

        AuthRequestDto auth = AuthRequestDto.builder().username("marta@ukr.net").password("1#Ma").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message:" + message);

        Assert.assertTrue(message.toString().contains("password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]"));
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
    @Test
    public void unsuccessRegistrationDuplicateUser() throws IOException {

        AuthRequestDto auth = AuthRequestDto.builder().username("silver1524@ukr.net").password("111#Silver1524").build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth),Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),409);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message:" + message);

        Assert.assertEquals(message,"User already exists");
        Assert.assertEquals(errorDto.getError(),"Conflict");
        Assert.assertEquals(errorDto.getStatus(),409);
    }
}
