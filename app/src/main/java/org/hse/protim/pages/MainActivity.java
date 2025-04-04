package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.hse.protim.R;
import org.hse.protim.clients.RegisterClient;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private Button loginButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        handle();
    }

    private void init() {
        loginButton = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void handle() {
        loginButton.setOnClickListener(v -> registrationButtonHandler());
        cancelButton.setOnClickListener(v -> loginButtonHandler());
    }

    private void loginButtonHandler() {
        Intent intent = new Intent(MainActivity.this, LoginPage.class);
        startActivity(intent);
    }

    private void registrationButtonHandler() {
        Intent intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }


//    public void post(){
//        Gson gson = new Gson();
//        String json = gson.toJson(new messageDTO("kirill_shulzhik@mail.ru", "kukukuku"));  // Убедитесь, что MessageDTO - правильное имя класса
//
//        // 2. Создаем RequestBody с JSON (используем RequestBody.create вместо FormBody.create)
//        RequestBody requestBody = RequestBody.create(
//                json,
//                MediaType.parse("application/json; charset=utf-8")
//        );
//
//        // 3. Настраиваем таймауты (если нужно)
//        OkHttpClient client = okHttpClient.newBuilder()
//                .connectTimeout(10, TimeUnit.SECONDS)  // Таймаут подключения
//                .readTimeout(10, TimeUnit.SECONDS)     // Таймаут чтения
//                .writeTimeout(10, TimeUnit.SECONDS)    // Таймаут записи
//                .build();
//
//        // 4. Формируем запрос
//        Request request = new Request.Builder()
//                .url(baseUrl + endPoint)
//                .post(requestBody)
//                .build();
//
//        // 5. Отправляем запрос
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                // Обработка ошибок сети/таймаута
//                runOnUiThread(() ->
//                        Toast.makeText(MainActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
//                );
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                // Обработка ответа сервера
//                if (!response.isSuccessful()) {
//                    runOnUiThread(() ->
//                            Toast.makeText(MainActivity.this,
//                                    "Server error: " + response.code() + " " + response.message(),
//                                    Toast.LENGTH_SHORT).show()
//                    );
//                } else {
//                    // Успешный ответ
//                    try {
//                        String responseBody = response.body().string();
//                        runOnUiThread(() -> Toast.makeText(MainActivity.this, responseBody, Toast.LENGTH_LONG).show());
//
//                        // Обработка успешного ответа
//                    } catch (IOException e) {
//                        runOnUiThread(() ->
//                                Toast.makeText(MainActivity.this,
//                                        "Response parsing error",
//                                        Toast.LENGTH_SHORT).show()
//                        );
//                    }
//                }
//            }
//        });
//    }
}