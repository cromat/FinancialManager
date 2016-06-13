package com.example.mat.financialmanager.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.activity.invoice.MainActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends Activity implements Validator.ValidationListener{

    @NotEmpty
    @Email
    private AutoCompleteTextView email;

    @NotEmpty
    @Password
    private EditText password;

    private Button signInButton;
    String usernametxt;
    String passwordtxt;

    private Validator validator;
    private boolean validated;
    private DialogInterface.OnClickListener dialogClickListener;
    private AlertDialog.Builder builder;


    private void findViews() {
        email = (AutoCompleteTextView)findViewById( R.id.email );
        password = (EditText)findViewById( R.id.password );
        signInButton = (Button)findViewById( R.id.sign_in_button );
    }

    protected void onCreate(Bundle savedInstanceState) {
        try {
            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId(AppConfig.APP_ID)
                    .server(AppConfig.PARSE_LINK)
                    .build()
            );

        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        validator = new Validator(this);
        validator.setValidationListener(this);
        validated = false;



        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        ParseUser user = new ParseUser();
                        user.setUsername(usernametxt);
                        user.setPassword(passwordtxt);

                        user.signUpInBackground(new SignUpCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Show a simple Toast message upon successful registration
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Signed up.",
                                            Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Sign up Error", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setMessage("Do you want to register with these credentials?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);


        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

                if (AppConfig.isNetworkAvailable(getApplicationContext())) {
                    if (validated) {

                        usernametxt = email.getText().toString();
                        passwordtxt = password.getText().toString();

                        // Send data to Parse.com for verification
                        ParseUser.logInInBackground(usernametxt, passwordtxt,
                                new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            // If user exist and authenticated, send user to Welcome.class
                                            Intent intent = new Intent(
                                                    LoginActivity.this,
                                                    MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(),
                                                    "Successfully Logged in",
                                                    Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "No such user exist!",
                                                    Toast.LENGTH_LONG).show();

                                            builder.show();
                                        }
                                    }
                                });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"No internet connection!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onValidationSucceeded() {
        validated = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getApplicationContext(), "Inputs are not valid or user already exists!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



