package com.example.pharmacymauzoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class additemActivity extends AppCompatActivity {
    private EditText itemname,itemsellprice,itembuyingprice,itemquantity;
    private EditText itembarcode;
    private FirebaseAuth firebaseAuth;
    public static TextView resulttextview;
    Button scanbutton, additemtodatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReferencecat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencecat = FirebaseDatabase.getInstance().getReference("Users");
        resulttextview = findViewById(R.id.barcodeview);
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);
        itemname = findViewById(R.id.edititemname);
        itemsellprice= findViewById(R.id.editsellprice);
        itemquantity= findViewById(R.id.editquantity);
        itembuyingprice = findViewById(R.id.editprice);
        itembarcode= findViewById(R.id.barcodeview);



       // String result = finaluser.substring(0, finaluser.indexOf("@"));


        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            }
        });

        additemtodatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additem();
            }
        });



    }


// addding item to databse
public  void additem(){
        String itemnameValue = itemname.getText().toString();
        String itemsellpriceValue = itemsellprice.getText().toString();
        String itemquantityValue = itemquantity.getText().toString();
        String itembuyingpriceValue = itembuyingprice.getText().toString();
        String itembarcodeValue = itembarcode.getText().toString();
         final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
         String resultemail = finaluser.replace(".","");
    if (itembarcodeValue.isEmpty()) {
        itembarcode.setError("It's Empty");
        itembarcode.requestFocus();
        return;
    }


    if(!TextUtils.isEmpty(itemnameValue)&&!TextUtils.isEmpty(itemsellpriceValue)&&!TextUtils.isEmpty(itembuyingpriceValue)){
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");


       databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.hasChild(itembarcodeValue)) {
                   // The root node of the snapshot has children

                   android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(additemActivity.this);
                   builder.setTitle("REGISTRATION PROCESS");
                   builder.setMessage("Are you sure you want to record this payment?");
                   builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                       }
                   });
                   builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                          // startActivity(new Intent(getApplicationContext(),AddmedicinequantityActivity.class));

                       }

                       });
               builder.create().show();

               }else{

//do something if not exists

                   Items items = new Items(itemnameValue, itembuyingpriceValue, itemsellpriceValue, itemquantityValue, itembarcodeValue);
                   databaseReference.child(resultemail).child("Items").child(itembarcodeValue).setValue(items);
                   //databaseReferencecat.child(resultemail).child("ItemByCategory").child(itemcategoryValue).child(itembarcodeValue).setValue(items);
                   itemname.setText("");
                   itembarcode.setText("");
                   itembuyingprice.setText("");
                   itembarcode.setText("");
                   Toast.makeText(additemActivity.this, itemnameValue + " Added", Toast.LENGTH_SHORT).show();
                   message("SUCCESS",itembarcodeValue+" -"+itemnameValue+" successfully registered!");

               }



           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {


           }
       });
      }
    else {
        Toast.makeText(additemActivity.this,"Please Fill all the fields",Toast.LENGTH_SHORT).show();
    }
}



    public void message(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }












    // logout below
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(additemActivity.this,MainActivity.class));
        Toast.makeText(additemActivity.this,"LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.logoutMenu:{
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
