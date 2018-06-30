package com.example.user.journalapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private CoordinatorLayout
            layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout = (CoordinatorLayout) findViewById(R.id.sign_in_rel_layout);
        mAuth = FirebaseAuth.getInstance();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        startFirebaseUI();


    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);



            try {
                FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
                if (resultCode == RESULT_OK) {

                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                } else {

                    if (response == null) {

                        Snackbar.make(layout, "Sign in cancelled", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startFirebaseUI();

                                    }
                                }).show();
                    }

                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {

                        Snackbar.make(layout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startFirebaseUI();

                                    }
                                }).show();

                    }

                    if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                        Snackbar.make(layout, "Unknown error", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startFirebaseUI();

                                    }
                                }).show();
                    }

                }

            } catch (Exception e) {
                Log.v("Exception: ",e.toString());
                Snackbar.make(layout, "Unknown error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startFirebaseUI();

                            }
                        }).show();

            }


        }
    }
    public void startFirebaseUI() {
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials*/, true /* hints*/)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        startFirebaseUI();


    }
}
