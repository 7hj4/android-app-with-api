package com.example.login_and_register_with_api;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView login_in ;
    public Button regitser;
    EditText editTextUsername, editTextEmail, editTextPassword , Re_enter_password;
    public ProgressBar progressBar;
    public static final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login_in = (TextView) findViewById(R.id.login_in);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        editTextUsername = (EditText) findViewById(R.id.Name);
        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPassword = (EditText) findViewById(R.id.Password);
        Re_enter_password = (EditText) findViewById(R.id.Re_enter_password);
        regitser = (Button) findViewById(R.id.regitser);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }


        regitser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();

            }
        });

        login_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

        private void registerUser() {
            final String username = editTextUsername.getText().toString().trim();
            final String email = editTextEmail.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            final String Re_password = Re_enter_password.getText().toString().trim();

            //first we will do the validations

            if (TextUtils.isEmpty(username)) {
                editTextUsername.setError("Please enter username");
                editTextUsername.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Please enter your email");
                editTextEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Enter a valid email");
                editTextEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Enter a password");
                editTextPassword.requestFocus();
                return;
            }

            if (password.equals(Re_password)) {

                //if it passes all the validations

                @SuppressLint("StaticFieldLeak")
                class RegisterUser extends AsyncTask<Void, Void, String> {

                    @Override
                    protected String doInBackground(Void... voids) {
                        //creating request handler object
                        request requestHandler = new request();

                        //creating request parameters
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("email", email);
                        params.put("password", password);

                        //returing the response
                        return requestHandler.sendPostRequest(services.url_register, params);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        //displaying the progress bar while user registers on the server

                        regitser.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.GONE);
                        regitser.setVisibility(View.VISIBLE);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(s);

                            //if no error in response
                            if (!obj.getBoolean("errors")) {

                                Toast.makeText(getApplicationContext(), "You have been successfully registered", Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("username"),
                                        userJson.getString("email")

                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                // starting after register to profileAectivity
                                finish();
                                Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //executing the async task
                RegisterUser ru = new RegisterUser();
                ru.execute();
            } else {
                Re_enter_password.setError("Password not match");
                Re_enter_password.requestFocus();
            }

        }
}


