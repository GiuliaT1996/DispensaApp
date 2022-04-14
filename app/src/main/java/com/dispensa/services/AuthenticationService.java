package com.dispensa.services;

import android.util.Log;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class AuthenticationService {

    public void registerUser() {
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), "gtincano@gmail.com")
                .build();

        confirmation();

        Amplify.Auth.signUp("admin", "admin123", options,
                result -> Log.i("Amplify", "Result: " + result),
                error -> Log.e("Amplify", "Sign up failed", error)
        );
    }

    private void confirmation() {
        Amplify.Auth.confirmSignUp(
                "admin",
                "540461",
                result -> Log.i("Amplify", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                error -> Log.e("Amplify", error.toString())
        );
    }

    public void signIn() {
        Amplify.Auth.signIn(
                "admin",
                "admin123",
                result -> Log.i("Amplify", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                error -> Log.e("Amplify", error.toString())
        );
    }

    public void checkConnection() {
        Amplify.Auth.fetchAuthSession(
                result -> Log.i("Amplify", result.toString()),
                error -> Log.e("Amplify", error.toString())
        );
    }
}
