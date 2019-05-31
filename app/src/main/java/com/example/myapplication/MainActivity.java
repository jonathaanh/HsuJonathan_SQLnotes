package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.myapplication.DatabaseHelper.COLUMN_NAME_CONTACT;
import static com.example.myapplication.DatabaseHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_Phone);
        editAddress = findViewById(R.id.editText_address);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper");

    }

    public void addData(View view)
    {
        //add Log.d
        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString());

        if (isInserted == true) {
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();

        }
        else {

        }
    }

    public void searchData(View view){
        Log.d("MyContactApp", "MainActivity: launching search");
        Cursor curs = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        if (editName.getText().toString().isEmpty() && editPhone.getText().toString().isEmpty()
                && editAddress.getText().toString().isEmpty()) {
            showMessage("Error", "Nothing to search for!");
            return;
        }

        while (curs.moveToNext()){
            if ((editName.getText().toString().isEmpty() || editName.getText().toString().equals(curs.getString(1)))
                    && (editPhone.getText().toString().isEmpty() || editPhone.getText().toString().equals(curs.getString(2)))
                    && (editAddress.getText().toString().isEmpty() || editAddress.getText().toString().equals(curs.getString(3))))
            {
                buffer.append("ID: " + curs.getString(0) + "\n");
                buffer.append("Name: " + curs.getString(1) + "\n");
                buffer.append("Phone: " + curs.getString(2) + "\n");
                buffer.append("Address: " + curs.getString(3) + "\n\n");
            }
        }
        if (buffer.toString().isEmpty()) {
            showMessage("Error", "No matches found");
            return;
        }
        showMessage("Search results", buffer.toString());
    }

    public void viewData(View view) {
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: received cursor");

        if(res.getCount() == 0) {
            showMessage("Error", "No data found in database");
            Log.d("MyContactApp", "MainActivity: no data in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            //append res columns to the buffer - see StringBuffer and Cursor API's
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Phone: " + res.getString(2) + "\n");
            buffer.append("Address: " + res.getString(3) + "\n\n");
        }
        showMessage("Data", buffer.toString());
    }
    public void clearData(View view){
        myDb.deleteData();
        Toast.makeText(MainActivity.this, "contacts deleted", Toast.LENGTH_LONG).show();
    }

    public void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: alertDialog setup");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}