package com.example.pharmacymauzoapp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class scanItemsActivity extends AppCompatActivity {
    public static EditText resultsearcheview;
    private FirebaseAuth firebaseAuth;
    ImageButton scantosearch;
    Button searchbtn;
    //Adapter adapter;

    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    DatabaseReference databaseReference,databaseReferencetransaction;
    Button btnPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_items);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        databaseReference =  FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        databaseReferencetransaction =  FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items").child("Transactions");

        resultsearcheview = findViewById(R.id.searchfield);
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);

        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);

        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        manager.setOrientation(LinearLayoutManager.VERTICAL);




        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivitysearch.class));
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = resultsearcheview.getText().toString();
                firebasesearch(searchtext);
            }
        });
    }

    public void firebasesearch(String searchtext){


        Query firebaseSearchQuery = mdatabaseReference.orderByChild("itembarcode").startAt(searchtext).endAt(searchtext+"\uf8ff");
        FirebaseRecyclerAdapter<Items, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, UsersViewHolder>
                (  Items.class,
                        R.layout.list_layout,
                        UsersViewHolder.class,
                        firebaseSearchQuery )
        {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Items model,int position){

                viewHolder.setDetails(getApplicationContext(),model.getItembarcode(),model.getItembuyingprice(),model.getItemname(),model.getItemsellprice(),model.getItemquantity());
            }
        };

        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
            public UsersViewHolder(View itemView){
            super(itemView);
            mView =itemView;
        }

    public void setDetails(Context ctx,String itembarcode, String itembuyingprice, String itemname, String itemprice, String itemquantity){
                TextView item_barcode =  mView.findViewById(R.id.viewitembarcode);
                TextView item_name =  mView.findViewById(R.id.viewitemname);
                TextView item_buyingprice =  mView.findViewById(R.id.viewitembuyingprice);
                TextView item_price = mView.findViewById(R.id.viewitemsellgprice);
        TextView item_quantity = mView.findViewById(R.id.viewitemquantity);

                item_barcode.setText(itembarcode);
                item_buyingprice.setText(itembuyingprice);
                item_name.setText(itemname);
                item_price.setText(itemprice);
        item_quantity.setText(itemquantity);


    }

    }
}
