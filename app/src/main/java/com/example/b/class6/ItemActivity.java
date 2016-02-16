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
        ItemEntry item= new ItemEntry(new Item(extras.getString("ItemName")),extras.getDouble("ItemQuantity"));
        Toast.makeText(getApplicationContext(), "ItemActivity.onCreate: " + item.theItem.itemName + " " + item.itemQuantity, Toast.LENGTH_SHORT).show();

        TextView i = (TextView) this.findViewById(R.id.itemName);
        TextView q = (TextView) this.findViewById(R.id.itemQuantity);
        i.setText(item.theItem.itemName);
        q.setText(item.itemQuantity+"");
    }
}
