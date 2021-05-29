package com.example.login_and_register_with_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView name , email , id , logout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        name = (TextView) findViewById(R.id.Name);
        email = (TextView) findViewById(R.id.Email);
        id = (TextView) findViewById(R.id.user_id);
        logout = (TextView)findViewById(R.id.logout);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        email.setText(user.getEmail());
        name.setText(user.getUsername());
        id.setText(String.valueOf(user.getId()));

        //when the user presses logout button
        //calling the logout method

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
}

