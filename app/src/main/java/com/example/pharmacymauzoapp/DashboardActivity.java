package com.example.pharmacymauzoapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener  {
    private FirebaseAuth firebaseAuth;
    TextView firebasenameview;
    Button toast;
    long ledger = System.currentTimeMillis();
    String customer_id ="customer"+ledger;

    private CardView addItems, deleteItems, scanItems, viewInventory,sellItem,viewsalesreport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebasenameview = findViewById(R.id.firebasename);

        // this is for username to appear after login

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");
        firebasenameview.setText("Welcome, "+resultemail);
//        toast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(dashboardActivity.this, users.getEmail(), Toast.LENGTH_SHORT).show();
//            }
//        });


        addItems =findViewById(R.id.addItems);
        deleteItems =findViewById(R.id.deleteItems);
        scanItems =findViewById(R.id.scanItems);
        viewInventory = findViewById(R.id.viewInventory);
        sellItem = findViewById(R.id.sellItem);
        viewsalesreport = findViewById(R.id.viewsalesreport);

        addItems.setOnClickListener(this);
        deleteItems.setOnClickListener(this);
        scanItems.setOnClickListener(this);
        viewInventory.setOnClickListener(this);
        sellItem.setOnClickListener(this);
        viewsalesreport.setOnClickListener(this);

        /*
        sellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 =new Intent(getApplicationContext(),SellingActivity.class);
                intent1.putExtra("customer_id",customer_id);
            }
        });

         */

    }


    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.addItems : i = new Intent(this,additemActivity.class); startActivity(i); break;
            case R.id.deleteItems : i = new Intent(this,deleteItemsActivity.class);startActivity(i); break;
            case R.id.scanItems : i = new Intent(this,scanItemsActivity.class);startActivity(i); break;
            case R.id.viewInventory : i = new Intent(this,viewInventoryActivity.class);startActivity(i); break;
            case R.id.sellItem : i = new Intent(this,SellingActivity.class);
                i.putExtra("customer_id",customer_id);
            startActivity(i);
            break;
            case R.id.viewsalesreport : i = new Intent(this,GenerateReportActivity.class);startActivity(i); break;

            default: break;
        }
    }





    // logout below
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(DashboardActivity.this,MainActivity.class));
        Toast.makeText(DashboardActivity.this,"LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();

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
