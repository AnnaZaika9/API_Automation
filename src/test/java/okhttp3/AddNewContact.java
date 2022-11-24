package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.ErrorDto;
import dto.MessageDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class AddNewContact {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";

    @Test(invocationCount = 5)
    public void addNewContactSuccess() throws IOException {
        int i = new Random().nextInt(1000)+1000;

        ContactDto contact = ContactDto.builder()
                .name("Anna"+i)
                .lastName("Fox"+i)
                .email("anna"+i+"@ukr.net")
                .phone("0534445"+i)
                .address("Tel Aviv")
                .description("friend")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        MessageDto message = Provider.getInstance().getGson().fromJson(response.body().string(), MessageDto.class);
        System.out.println("Message: " + message.getMessage());
        Assert.assertTrue(message.getMessage().contains("Contact was added!"));

        String ID = Arrays.stream(message.getMessage().split(" ")).reduce((f, l) -> l).get();
        System.out.println("ID: " + ID);
        Assert.assertEquals(message.getMessage(),"Contact was added! ID: "+ ID );

    }
    @Test
    public void addNewContactWithInvalidName() throws IOException {

        ContactDto contact = ContactDto.builder()
                .name("")
                .lastName("Snow")
                .email("igor@gmail.com")
                .phone("0534445477")
                .address("Holon")
                .description("work")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
     //   Assert.assertEquals(message,"must not be blank");
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
    @Test
    public void addNewContactWithInvalidLastName() throws IOException {

        ContactDto contact = ContactDto.builder()
                .name("Ira")
                .lastName("")
                .email("ira@gmail.com")
                .phone("0534445477")
                .address("Rehovot")
                .description("spa")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
        //   Assert.assertEquals(message,"must not be blank");
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
    @Test
    public void addNewContactWithInvalidEmail() throws IOException {

        ContactDto contact = ContactDto.builder()
                .name("Ira")
                .lastName("Bra")
                .email("iragmail.com")
                .phone("0534445477")
                .address("Holon")
                .description("home")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
        //   Assert.assertEquals(message,"must not be blank");
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
    @Test
    public void addNewContactUnauthorized() throws IOException {

        ContactDto contact = ContactDto.builder()
                .name("Ira")
                .lastName("Bra")
                .email("ira@gmail.com")
                .phone("0534445477")
                .address("Holon")
                .description("home")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", "rrr")
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
        Assert.assertEquals(message,"JWT strings must contain exactly 2 period characters. Found: 0");
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Assert.assertEquals(errorDto.getStatus(),401);
    }

    @Test(enabled = false,description = "BUG - it is possible to create a contact with the same data")
    public void addNewContactDublicateContact() throws IOException {

        ContactDto contact = ContactDto.builder()
                .name("Anna1294")
                .lastName("Fox1294")
                .email("anna1294@ukr.net")
                .phone("05344451294")
                .address("Tel Aviv")
                .description("friend")
                .build();
        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 409);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
       // Assert.assertEquals(message,"");
        //Assert.assertEquals(errorDto.getError(),"");
        Assert.assertEquals(errorDto.getStatus(),409);
    }

}
