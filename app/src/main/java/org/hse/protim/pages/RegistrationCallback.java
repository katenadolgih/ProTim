package org.hse.protim.pages;

public interface RegistrationCallback {
    void onSuccess(String email);
    void onFailure(String error);
}
