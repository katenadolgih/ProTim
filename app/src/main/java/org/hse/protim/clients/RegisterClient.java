package org.hse.protim.clients;

import static org.hse.protim.clients.ClientsConst.baseUrl;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.hse.protim.DTO.LoginDTO;
import org.hse.protim.DTO.RegisterDTO;
import org.hse.protim.DTO.TokensDTO;
import org.hse.protim.TokenStorage;
import org.hse.protim.pages.LoginPage;
import org.hse.protim.pages.RegisterPage;
import org.hse.protim.pages.RegistrationCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterClient {
    private final Context context;
    public RegisterClient(Context context) {
        this.context = context;
    }
    private static final String register = "/register";
    private static final String signIn = "/sign-in";

    private static final String test = "/test";
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    public void register(String surname, String name, String phoneNumber, String email,
                         RegistrationCallback callback) {
        Gson gson = new Gson();
        String json = gson.toJson(new RegisterDTO(surname, name, phoneNumber, email));

        RequestBody requestBody = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(baseUrl + register)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    callback.onFailure("Server error: " + response.code());
                } else {
                    callback.onSuccess(email);
                }
            }
        });
    }

    public void signIn(String email, String password, RegistrationCallback callback) {
        TokenStorage tokenStorage = new TokenStorage(context);
        Gson gson = new Gson();
        String json = gson.toJson(new LoginDTO(email, password));

        RequestBody requestBody = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(baseUrl + signIn)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Server error: " + response.code());
                } else {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    TokensDTO tokensDTO = gson.fromJson(responseBody, TokensDTO.class);

                    tokenStorage.saveTokens(tokensDTO.accessToken(), tokensDTO.refreshToken());
                    callback.onSuccess("52");
                }
            }
        });
    }

    public void test(RegistrationCallback callback) {
        TokenStorage tokenStorage = new TokenStorage(context);
        String accessToken = tokenStorage.getAccessToken();

        Request request = new Request.Builder()
                .url(baseUrl + test)
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Server error: " + response.code());
                    return;
                }
                try {
                    String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                } catch (Exception e) {
                    callback.onFailure("Parsing error: " + e.getMessage());
                }
            }
        });
    }
}
