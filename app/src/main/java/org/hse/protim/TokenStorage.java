package org.hse.protim;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenStorage {
    private final Context context;
    private final String sharedPrefsFile = "secure_tokens";
    private MasterKey masterKey;

    public TokenStorage(Context context) {
        this.context = context;
        try {
            masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to initialize MasterKey", e);
        }
    }

    public void saveTokens(String accessToken, String refreshToken) {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
            sharedPreferences.edit()
                    .putString("access_token", accessToken)
                    .putString("refresh_token", refreshToken)
                    .apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken() {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
            return sharedPreferences.getString("access_token", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRefreshToken() {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
            return sharedPreferences.getString("refresh_token", null);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearTokens() {
        try {
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();
            sharedPreferences.edit()
                    .remove("access_token")
                    .remove("refresh_token")
                    .apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private SharedPreferences getEncryptedSharedPreferences()
            throws GeneralSecurityException, IOException {
        return EncryptedSharedPreferences.create(
                context,
                sharedPrefsFile,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }
}
