package okhttp3;

import config.Provider;
import dto.ErrorDto;
import dto.MessageDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;


public class DeleteContactByID {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";


    @Test
    public void deleteContactsByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/697fe049-9184-4424-a5a8-161a7a70bd0a")
                .addHeader("Authorization", token)
                .delete()
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        MessageDto message = Provider.getInstance().getGson().fromJson(response.body().string(), MessageDto.class);
        System.out.println("Message: " + message.getMessage());
        Assert.assertEquals(message.getMessage(), "Contact was deleted!");

    }
    @Test
    public void deleteContactsByIdContactAnyFormatError() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/69")
                .addHeader("Authorization", token)
                .delete()
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
        Assert.assertEquals(message,"Contact with id: 69 not found in your contacts!");
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getStatus(),400);
    }
    @Test
    public void deleteContactsByIdUnauthorized() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/ee61731a-8f26-4a96-9baa-95b5e574d18d")
                .addHeader("Authorization", "muuusc")
                .delete()
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
    @Test(enabled = false,description = "BUG - 400 code instead of 404")
    public void deleteContactsByIdContactNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/697fe049-9184-4424-a5a8-161a7a70bd0a")
                .addHeader("Authorization", token)
                .delete()
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 404);

        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
        Assert.assertEquals(message,"?");
        Assert.assertEquals(errorDto.getError(),"?");
        Assert.assertEquals(errorDto.getStatus(),404);
    }
}
