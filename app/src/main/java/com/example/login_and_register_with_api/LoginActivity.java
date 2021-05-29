package com.example.login_and_register_with_api;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    public EditText email , password ;
    private Button login ;
    private TextView Regitser_here;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.regitser);
        Regitser_here = (TextView)findViewById(R.id.Register_here);


        // on click text Register here go to activity Register

        Regitser_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);


            }
        });


            //if user presses on login
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
            startActivity(intent);
            finish();
        }


            //calling the method login
           login.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   userLogin();
               }
           });
        }

        private void userLogin() {
            //first getting the values
            final String inputemail = email.getText().toString();
            final String inputpassword = password.getText().toString();

            //validating inputs

            if (TextUtils.isEmpty(inputemail)) {
                email.setError("Please enter your email");
                email.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputemail).matches()) {
                email.setError("Enter a valid email");
                email.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(inputpassword)) {
                password.setError("Please enter your password");
                password.requestFocus();
                return;
            }

            //if everything is fine

            class UserLogin extends AsyncTask<Void, Void, String> {

                ProgressBar progressBar;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressBar = (ProgressBar) findViewById(R.id.progressbar);
                    login.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    progressBar.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);


                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("errors")) {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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

                            //starting the profile activity
                            finish();
                            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    request requestHandler = new request();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", inputemail);
                    params.put("password", inputpassword);

                    //retuning the response
                    return requestHandler.sendPostRequest(services.url_login, params);
                }
            }

            UserLogin ul = new UserLogin();
            ul.execute();



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
