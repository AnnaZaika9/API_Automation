package config;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Provider {
    private static final Provider instance = new Provider();
    private static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    private Gson gson = new Gson();
    private OkHttpClient client;

    private HttpLoggingInterceptor logging;

    private Provider() {
        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    public static Provider getInstance() {
        return instance;
    }
    public OkHttpClient getClient(){
        return client;
    }
    public  Gson getGson(){
        return gson;
    }
    public MediaType getJson(){
        return JSON;
    }

}
