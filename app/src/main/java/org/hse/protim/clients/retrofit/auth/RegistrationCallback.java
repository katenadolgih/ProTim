package org.hse.protim.clients.retrofit.auth;

public interface RegistrationCallback {
    void onSuccess(String email);
    void onFailure(String error);
}
