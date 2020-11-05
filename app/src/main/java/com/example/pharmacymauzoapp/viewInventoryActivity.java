package com.example.pharmacymauzoapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class viewInventoryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
   private TextView totalnoofitem, totalnoofsum;
   private int counttotalnoofitem =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        //totalnoofitem= findViewById(R.id.totalnoitem);
        //totalnoofsum = findViewById(R.id.totalsum);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");
        mrecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        manager.setOrientation(LinearLayoutManager.VERTICAL);

/*
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
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
                Object price = map.get("itemsellprice");
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
    }



    @Override
    protected  void  onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Items, scanItemsActivity.UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Items, scanItemsActivity.UsersViewHolder>
                (  Items.class,
                        R.layout.list_layout,
                        scanItemsActivity.UsersViewHolder.class,
                        mdatabaseReference )
        {
            @Override
            protected void populateViewHolder(scanItemsActivity.UsersViewHolder viewHolder, Items model, int position){

                viewHolder.setDetails(getApplicationContext(),model.getItembarcode(),model.getItembuyingprice(),model.getItemname(),model.getItemsellprice(),model.getItemquantity());
            }
        };

        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
    /*public static class UsersViewHolder extends RecyclerView.ViewHolder{
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

     */

}
