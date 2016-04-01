package com.example.alexandreroussiere.parser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via login/password.
 */
public class Login extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    int tries_left=3;
    private TextView triesView;
    private CheckBox showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("LOGIN process: ", "Login screen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);
        triesView = (TextView) findViewById(R.id.tries);
        showPassword = (CheckBox) findViewById(R.id.password_visibility);

        Button loginButton = (Button) findViewById(R.id.login_button);

        //Will be active if the user click on the checkbox
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if it's checked then we want to show in PlainText the password to the user
                if(isChecked){
                    mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //Otherwise, we just see stars
                }else{
                    mPasswordView.setInputType(129);
                }

            }
        });

        //Will be active when the user clicks on the login button
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //We try to login
                if (tries_left != 0) {
                    Log.i("LOGIN process: ","Attempt to login");
                    attemptLogin();
                }
                //If the user made 3 mistakes, he can't login anymore
                else {
                    Log.i("LOGIN process: ", "No more tries left");
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.noTriesLeft, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
    
    /**
     * Attempts to sign in
     */
    private void attemptLogin() {


        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        Context context = getApplicationContext();


        // Check for a valid login (not null nor wrong) .
        if (TextUtils.isEmpty(login)) {
            Log.i("LOGIN process: ","Login is empty");
            Toast toast = Toast.makeText(context,R.string.error_login_required,Toast.LENGTH_SHORT);
            toast.show();
            focusView = mLoginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            Log.i("LOGIN process: ","Login is wrong");
            Toast toast = Toast.makeText(context,R.string.error_invalid_login,Toast.LENGTH_SHORT);
            toast.show();
            tries_left--;
            focusView = mLoginView;
            cancel = true;
        }

        // Check for a valid password (not null nor wrong) .
        else if(TextUtils.isEmpty(password)){
            Log.i("LOGIN process: ","Password is empty");
            Toast toast = Toast.makeText(context,R.string.error_password_required,Toast.LENGTH_SHORT);
            toast.show();
            focusView = mLoginView;
            cancel = true;
        }
        else if(!isPasswordValid(password)){
            Log.i("LOGIN process: ","Password is wrong");

            Toast toast = Toast.makeText(context,R.string.error_incorrect_password,Toast.LENGTH_SHORT);
            toast.show();
            tries_left--;
            mPasswordView.setText("");
            focusView = mLoginView;
            cancel = true;
        }

        // There was an error; don't attempt login and focus the first
        // form field with an error.
        if (cancel) {
            Log.i("LOGIN process: ","FAILED");
            //We display the number of tries left
            triesView.setText("Tries left: " + String.valueOf(getTries()));
            focusView.requestFocus();
        }
        //If it's OK, we start the main activity and finish this one
        else {
            Log.i("LOGIN process: ","SUCCESSFUL");
            Toast toast = Toast.makeText(context, R.string.success,Toast.LENGTH_SHORT);
            toast.show();
            Intent i = new Intent(context,MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    /**
     * Check if the login is good
     * @param login
     * @return
     */
    private boolean isLoginValid(String login) {
        //If the login enterred is the good one
        return login.equalsIgnoreCase(getString(R.string.login_correct));
        //try return getString(R.string.login_correct).equalsIgnoreCase(login)
    }

    /**
     * Check is the password is good
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {

        return password.equalsIgnoreCase(getString(R.string.password_correct));
    }

    //return the number of tries left
    public int getTries(){
        return tries_left;
    }
}



