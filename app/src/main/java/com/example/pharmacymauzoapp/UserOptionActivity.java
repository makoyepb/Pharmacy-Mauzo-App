package com.example.pharmacymauzoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserOptionActivity extends AppCompatActivity {
    Button edtCustom,edtSupplier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);
edtCustom=findViewById(R.id.customer);
edtSupplier=findViewById(R.id.supplier);

edtSupplier.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }
});

    }
}