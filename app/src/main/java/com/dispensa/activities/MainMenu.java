package com.dispensa.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Dispensa;
import com.dispensa.R;
import com.dispensa.queries.Query;
import com.dispensa.services.AuthenticationService;
import com.dispensa.utilities.Constants;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        configureAmplify();

        Dispensa d = Dispensa.builder()
                .name("Acqua")
                .section("Armadio Esterno")
                .trigger(3)
                .quantity(6)
                .build();

        Query q = new Query();
        q.create(d);
    }

    private void configureAmplify() {
        try {

            if (!Constants.getConfigured()) {
                Amplify.addPlugin(new AWSApiPlugin()); // UNCOMMENT this line once backend is deployed
                Amplify.addPlugin(new AWSDataStorePlugin());
                Amplify.addPlugin(new AWSCognitoAuthPlugin());
                Amplify.configure(getApplicationContext());

                Log.i("Amplify", "Initialized Amplify");

                Constants.setConfigured(true);
            }

            AuthenticationService authenticationService = new AuthenticationService();

            authenticationService.signIn();
            authenticationService.checkConnection();

        } catch (AmplifyException error) {
            Log.e("Amplify", "Could not initialize Amplify", error);
        }
    }
}