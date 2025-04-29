package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.auth.AuthChecker;
import org.hse.protim.utils.auth.TokenStorage;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenStorage tokenStorage = new TokenStorage(this);
        if (tokenStorage.getAccessToken() == null) {
            goToLogin();
            return;
        }

        new AuthChecker(new RetrofitProvider(this)).checkAuth(new AuthChecker.AuthCallback() {
            @Override
            public void onAuthorized() {
                startActivity(new Intent(SplashActivity.this, HomePage.class));
                finish();
            }

            @Override
            public void onUnauthorized() {
                goToLogin();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginPage.class));
        finish();
    }
}
