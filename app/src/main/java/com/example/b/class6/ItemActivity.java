package com.example.b.class6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Bundle extras = getIntent().getExtras();
        ItemEntry theItemEntry=(ItemEntry)extras.getSerializable("ItemEntry");
        Toast.makeText(getApplicationContext(), "ItemActivity.onCreate: " + theItemEntry.theItem.itemName + " " + theItemEntry.itemQuantity, Toast.LENGTH_SHORT).show();

        TextView i = (TextView) this.findViewById(R.id.itemName);
        TextView q = (TextView) this.findViewById(R.id.itemQuantity);
        i.setText(theItemEntry.theItem.itemName);
        q.setText(theItemEntry.itemQuantity+"");


    }
}
