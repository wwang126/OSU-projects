package com.example.steammarketapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steammarketapp.data.LoginDatabase;

public class ProfileActivity extends AppCompatActivity {
    LoginDatabase db;
    TextView mProfileUsername;
    TextView mProfilePassword;
    TextView mProfileBalance;
    Button mButtonEdit;
    TextView meditText;
    TextView meditText2;
    TextView meditText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new LoginDatabase(this);
        db.getAllData();
        mProfileUsername = (TextView) findViewById(R.id.ProfileName);
        mProfilePassword = (TextView) findViewById(R.id.ProfilePassword);
        mProfileBalance = (TextView) findViewById(R.id.ProfileBalance);

        mButtonEdit= (Button) findViewById(R.id.EditProfile);

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditProfileIntent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(EditProfileIntent);

            }
        });

        //Displaying the username, passowrd and balance content
 //       meditText = (TextView) findViewById(R.id.editText);
 //       meditText2 = (TextView) findViewById(R.id.editText2);
 //       meditText3 = (TextView) findViewById(R.id.editText3);

        viewAll();

    }

public void viewAll(){

       Cursor res = db.getAllData();
       if (res.getCount() == 0){
           //show message
           showMessage("Error","No Data dound");
            return;
       }

       StringBuffer buffer = new StringBuffer();
       while (res.moveToNext()){
           buffer.append("Id : " + res.getString(0)+"\n");

           buffer.append("Name : " + res.getString(1)+"\n");

           buffer.append("Password : " + res.getString(2)+"\n");

           buffer.append("Balance : " + res.getString(3)+"\n\n");
       }
        showMessage("Data",buffer.toString());
       //Show all data
}


public void showMessage(String title,String Message){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setCancelable(true);
    builder.setTitle(title);
    builder.setMessage(Message);
    builder.show();
}

}
