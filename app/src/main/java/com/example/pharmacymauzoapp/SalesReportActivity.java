package com.example.pharmacymauzoapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;;
import androidx.annotation.NonNull;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SalesReportActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    private TextView totalnoofitem, totalnoofsum;
    private int counttotalnoofitem =0;

    String datechild1;
    CustomAdapter adapter;
    ArrayList<Medicine> items;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        totalnoofitem= findViewById(R.id.totalnoitem);
        totalnoofsum = findViewById(R.id.totalsum);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");

        Intent intent=getIntent();
        datechild1=intent.getStringExtra("customerdate");
        String datechild=datechild1.replace("/","");


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");

        Log.d("result", "Result email: "+resultemail);
        Log.d("result", "Children count: "+"0"+datechild);


       mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail)
               .child("Transactions/"+"0"+datechild);

        items = new ArrayList();

        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);

        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //insert into database


        adapter = new CustomAdapter(this,items);


  /*      mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    counttotalnoofitem = (int) dataSnapshot.getChildrenCount();
                    totalnoofitem.setText(Integer.toString(counttotalnoofitem));
                }else{
                    totalnoofitem.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get(map);
                    int pValue = Integer.parseInt(String.valueOf(price));
                    sum += pValue;
                    totalnoofsum.setText(String.valueOf(sum));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


 */
        //message("CUSTOMER ID","we found"+datechild);


/*
        mdatabaseReference.orderByChild(datechild).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //items.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Map<String,Object> map = (Map<String, Object>) snap.getValue();
                    Object customer_id1 = map.get(map);
                    String customer_id=String.valueOf(customer_id1);

                    //Toast.makeText(SalesReportActivity.this, "customer id is"+customer_id, Toast.LENGTH_SHORT).show();
                    message("CUSTOMER ID","we found"+customer_id1);

                    mdatabaseReference.orderByChild(customer_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Map<String, Object> map = (Map<String, Object>) snap.getValue();
                                Object itembarcode1 = map.get(map);
                                String itembarcode = String.valueOf(itembarcode1);

                                mdatabaseReference.child(customer_id).orderByChild(itembarcode).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                            Medicine item = snap.getValue(Medicine.class);
                                            items.add(item);
                                        }
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                               // mrecyclerview.setAdapter(adapter);
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
                message("DATABASE LOCKED","Sorry, we couldn't access the DB. Contact you app developer for assistance");
            }
        });
        mrecyclerview.setAdapter(adapter);
*/

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                items.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String custom1=snap.getKey().toString();
                     mdatabaseReference.child(custom1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            double sum=0;
                            double sumq=0;

                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Medicine item = snap.getValue(Medicine.class);

                                double quantity=Double.parseDouble(item.getItemquantity());
                                double amount=Double.parseDouble(item.getAmount());
                                sumq+=quantity;
                                sum += amount;
                                totalnoofsum.setText(String.valueOf(sum));
                                totalnoofitem.setText(String.valueOf(sumq));
                                items.add(item);
                                Log.d("result", "Barcode: "+item.getItembarcode());
                                Log.d("result", "item name: "+item.getItemname());
                                items.add(item);

                            }
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                mrecyclerview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





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
