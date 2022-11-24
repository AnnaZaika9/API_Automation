package okhttp3;

import config.Provider;
import dto.ErrorDto;
import dto.MessageDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;


public class DeleteAllContacts {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiZGFzaGFAdWtyLm5ldCIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjY5NjU0NDM0LCJpYXQiOjE2NjkwNTQ0MzR9.ALFSYzBSTaYJGdQZJiyF1ywt9L2jt0p8lnD8nBGn8oU";

    @Test
    public void deleteAllContactsSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/clear")
                .addHeader("Authorization", token)
                .delete()
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        MessageDto message = Provider.getInstance().getGson().fromJson(response.body().string(), MessageDto.class);
        System.out.println("Message: " + message.getMessage());
        Assert.assertEquals(message.getMessage(),"All contacts was deleted!");
    }
    @Test
    public void deleteAllContactsUnauthorized() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/clear")
                .addHeader("Authorization", "gr")
                .delete()
                .build();
        Response response = Provider.getInstance().getClient().newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);
        ErrorDto errorDto = Provider.getInstance().getGson().fromJson(response.body().string(),ErrorDto.class);
        Object message =  errorDto.getMessage();
        System.out.println("Message: " + message);
        Assert.assertEquals(message,"JWT strings must contain exactly 2 period characters. Found: 0");
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Assert.assertEquals(errorDto.getStatus(),401);

    }
}

