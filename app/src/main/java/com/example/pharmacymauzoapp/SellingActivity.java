package com.example.pharmacymauzoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SellingActivity extends AppCompatActivity {

    java.util.Date dNow = new java.util.Date();
    SimpleDateFormat ftb = new SimpleDateFormat("dd/MM/yyyy");
    String date=ftb.format(dNow);
String customer_id;
    CustomAdapter adapter;
    ArrayList<Medicine> items;


    public static EditText resultsearcheview;

    private FirebaseAuth firebaseAuth;
    ImageButton scantosearch;
    Button searchbtn,btnPayment;

    //Adapter adapter;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference,databaseReference,databaseReferencePay;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        Intent intent=getIntent();
        customer_id=intent.getStringExtra("customer_id");



        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        String datechild=date.replace("/","");
        databaseReference =  FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child(customer_id);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        databaseReferencePay = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Transactions").child(datechild);
        resultsearcheview = findViewById(R.id.searchfield);
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);
        btnPayment=findViewById(R.id.paybutton);


        items = new ArrayList();

        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);

        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //insert into database


        adapter = new CustomAdapter(this,items);




        // Do payment


        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SellingActivity.this);
                builder.setTitle("PAYING PROCESS");
                builder.setMessage("Are you sure you want to record this payment?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                items.clear();
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    Medicine medicine = snap.getValue(Medicine.class);
                                     String itembarcode=medicine.getItembarcode();
                                    String quantity1=medicine.getItemquantity();
                                    double quantity2=Double.parseDouble(quantity1);

                                   databaseReferencePay.child(customer_id).child(itembarcode).setValue(medicine);


                                    message("SUCCESS","Payment successfully recorded!");
                                    databaseReference.removeValue();
                                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                message("DATABASE LOCKED","Sorry, we couldn't access the DB. Contact you app developer for assistance");
                            }
                        });



                    }
                });
                builder.create().show();

            }
        });



        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivitysell.class));
            }
        });


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String searchtext = resultsearcheview.getText().toString();
                  // searching and insert into temporary child

                databaseReference.orderByChild("itembarcode").startAt(searchtext).endAt(searchtext + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(searchtext)){
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Medicine medicine = snap.getValue(Medicine.class);
                                String quantity1=medicine.getItemquantity();
                                double quantity2=Double.parseDouble(quantity1)+1;

                                String itemname=medicine.getItemname();
                                String itemsellingprice=medicine.getItemsellprice();
                                String itembarcode=medicine.getItembarcode();
                               double amount2=Double.parseDouble(itemsellingprice)*quantity2;
                                String amount=String.valueOf(amount2);
                                String quantity=String.valueOf(quantity2);


                                Medicine medicine1 = new Medicine(itemname, itemsellingprice, quantity, itembarcode,amount,datechild);
                                databaseReference.child(itembarcode).setValue(medicine1);

                            }

                        }else{

                           // not in customer order


                            Query firebaseSearchQuery1 = mdatabaseReference.orderByChild("itembarcode").startAt(searchtext).endAt(searchtext + "\uf8ff");
                            firebaseSearchQuery1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot snap : snapshot.getChildren()) {

                                        Medicine medicine=snap.getValue(Medicine.class);

                                        String itemname=medicine.getItemname();
                                        String itemsellingprice=medicine.getItemsellprice();
                                        String itembarcode=medicine.getItembarcode();
                                        double quantity2=1;
                                        double amount2=Double.parseDouble(itemsellingprice)*quantity2;
                                        String amount=String.valueOf(amount2);
                                        String quantity=String.valueOf(quantity2);


                                        Medicine medicine1 = new Medicine(itemname, itemsellingprice, quantity, itembarcode,amount,datechild);
                                        databaseReference.child(itembarcode).setValue(medicine1);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Medicine item = snap.getValue(Medicine.class);
                    items.add(item);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                message("DATABASE LOCKED","Sorry, we couldn't access the DB. Contact you app developer for assistance");
            }
        });
        mrecyclerview.setAdapter(adapter);

/*
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setBackgroundColor(Color.BLUE);

        TextView tv0 = new TextView(this);
        tv0.setText(" Barcode ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(" Medicine Name ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(" Price ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" Quantity ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" Amount ");
        tv4.setTextColor(Color.WHITE);
        tbrow0.addView(tv4);

        stk.addView(tbrow0);





        databaseReference.addValueEventListener(new ValueEventListener() {

            //double sumamount=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Medicine medicine=snap.getValue(Medicine.class);

                    String itemname=medicine.getItemname();
                    String itemsellingprice=medicine.getItemsellprice();
                    String itembarcode=medicine.getItembarcode();
                    String quantity=medicine.getItemquantity();
                    String amount=medicine.getAmount();
                    //sumamount+=Double.parseDouble(amount);
                    //message("FAILED","There is no data"+itembarcode+" "+itemname);

                    TableRow tbrow = new TableRow(getApplicationContext());

                    TextView t1v = new TextView(getApplicationContext());
                    t1v.setText("" + itembarcode);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    tbrow.addView(t1v);

                    TextView t2v = new TextView(getApplicationContext());
                    t2v.setText("" + itemname);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.CENTER);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(getApplicationContext());
                    t3v.setText("" + itemsellingprice);
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(getApplicationContext());
                    t4v.setText("" + quantity);
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(getApplicationContext());
                    t5v.setText("" + amount);
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.CENTER);
                    tbrow.addView(t5v);
                    stk.addView(tbrow);




                }
             }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                message("DATABASE LOCKED","Sorry, we couldn't access the DB. Contact you app developer for assistance");
            }
        });



 */



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


}
