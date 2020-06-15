package com.example.steammarketapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.steammarketapp.data.LoginDatabase;

public class EditProfileActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextBalance;
    EditText mTextID;
    Button ButtonUpdate;
    Button ButtonDelete;
    Button ButtonView;
    LoginDatabase db;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        db = new LoginDatabase(this);
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        //  View v = this.getLayoutInflater().inflate(R.layout.progressbar,null);
        //  dialog.setContentView(v);
        //  dialog.show();
        mTextID = (EditText) findViewById(R.id.editID);
        mTextUsername = (EditText) findViewById(R.id.editName);
        mTextPassword = (EditText) findViewById(R.id.editPassword);
        mTextBalance = (EditText) findViewById(R.id.editBalance);
        ButtonView= (Button) findViewById(R.id.button_view);
        ButtonUpdate= (Button) findViewById(R.id.button_update);
        ButtonDelete= (Button) findViewById(R.id.button_delete);
        viewAll();
        UpdateData();
        DeleteData();
    }
    public void DeleteData (){
        ButtonDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Integer deleteRows = db.deleteData(mTextID.getText().toString());
                        if(deleteRows > 0){
                            Toast.makeText(EditProfileActivity.this, "Data Deleted",Toast.LENGTH_LONG).show();
                        }else
                            Toast.makeText(EditProfileActivity.this, "Data Not Deleted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void UpdateData(){
        ButtonUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = db.updateData(mTextID.getText().toString(),mTextUsername.getText().toString(),mTextPassword.getText().toString(),mTextBalance.getText().toString());
                    if(isUpdate == true){
                        Toast.makeText(EditProfileActivity.this, "Data Updated",Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(EditProfileActivity.this, "Data Not Updated",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public void viewAll(){
        ButtonView.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor res = db.getAllData();
                    if (res.getCount() == 0) {
                        //show message
                        showMessage("Error", "No Data dound");
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while (res.moveToNext()) {
                        buffer.append("Id : " + res.getString(0) + "\n");

                        buffer.append("Name : " + res.getString(1) + "\n");

                        buffer.append("Password : " + res.getString(2) + "\n");

                        buffer.append("Balance : " + res.getString(3) + "\n\n");
                    }
                    showMessage("Data", buffer.toString());
                    //Show all data

                }
            }
            );
    }


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}
