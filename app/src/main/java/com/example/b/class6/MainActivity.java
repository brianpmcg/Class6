package com.example.b.class6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity implements ItemEntryAdapter.BtnClickListener {

    private Recipe recipe;
    ItemEntryAdapter customAdapter;
    public final String gsonRecipeFilename = "com.example.b.class6.Recipe.json";
    public final String preferencesFileName="com.example.b.class6.SharedPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "onCreate" , Toast.LENGTH_SHORT).show();
        recipe=new Recipe("Test Recipe","Example Instructions");

        Button addItem = (Button) this.findViewById(R.id.addItemButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToRecipe(view);
            }
        });
        Button updateItem = (Button) this.findViewById(R.id.updateItemButton);
        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRecipeItem(view);
            }
        });
        updateItem.setEnabled(false);

        Button saveGsonB = (Button) this.findViewById(R.id.saveGson);
        saveGsonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGson(view);
            }
        });



        Button loadGsonB = (Button) this.findViewById(R.id.loadGson);
        loadGsonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGson(view);
            }
        });

        Button savePrefB = (Button) this.findViewById(R.id.savePref);
        savePrefB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreference(view);
            }
        });

        this.findViewById(android.R.id.content).getRootView();

        ListView myListView = (ListView) this.findViewById(R.id.recipeListView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
            long arg3)
            {
                ItemEntry value = (ItemEntry)adapter.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "onItemClick: "+value.theItem.itemName+" "+value.itemQuantity, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),ItemActivity.class);
                i.putExtra("ItemEntry",value);
                startActivity(i);
            }

        });

        Button showPrefB = (Button) this.findViewById(R.id.showPref);
        showPrefB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreference(view);
            }
        });

        buildList();


    }

    public void addItemToRecipe(View view){
        TextView i=(TextView) this.findViewById(R.id.itemName);
        TextView q=(TextView) this.findViewById(R.id.quantity);

        for (Iterator<ItemEntry> iter = recipe.recipeItems.listIterator(); iter.hasNext(); ) {
            ItemEntry item = iter.next();
            if (item.theItem.itemName.equalsIgnoreCase(i.getText().toString())) {
                //the item is already in the list
                Toast.makeText(getApplicationContext(), "addItemToRecipe: Warning DUPLICATE ITEM", Toast.LENGTH_SHORT).show();
            }
        }

        try {

            ItemEntry itemEntry = new ItemEntry(new Item(i.getText().toString()), Double.parseDouble(q.getText().toString()), ItemEntry.Units.other);
            recipe.recipeItems.add(itemEntry);

            this.refreshList();
            Toast.makeText(getApplicationContext(), "addItemToRecipe", Toast.LENGTH_SHORT).show();
        }
        catch(NumberFormatException e)
        {
            Toast.makeText(getApplicationContext(), "addItemToRecipe: Quantity not a number", Toast.LENGTH_SHORT).show();
        }
    }

    public void savePreference(View view){
        TextView i=(TextView) this.findViewById(R.id.itemName);
        TextView q=(TextView) this.findViewById(R.id.quantity);
        SharedPreferences userPrefs = getApplicationContext().getSharedPreferences(preferencesFileName, this.MODE_PRIVATE);
        SharedPreferences.Editor edit = userPrefs.edit();
        edit.putString(i.getText().toString(), q.getText().toString());
        edit.commit();
        Toast.makeText(getApplicationContext(), "savePreference:"+i.getText().toString()+"="+q.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void showPreference(View view){
        TextView i=(TextView) this.findViewById(R.id.itemName);
        SharedPreferences userPrefs = this.getApplicationContext().getSharedPreferences(preferencesFileName, this.MODE_PRIVATE);
        String testPref = userPrefs.getString(i.getText().toString(), "");
        Toast.makeText(getApplicationContext(), "showPreference:"+i.getText().toString()+"="+testPref, Toast.LENGTH_SHORT).show();
    }

    public void buildList()
    {
        //This uses the custom adapter in ItemEntryAdapter.java
        ListView lv = (ListView) findViewById(R.id.recipeListView);
        customAdapter = new ItemEntryAdapter(this, R.layout.item_entry_row, recipe.recipeItems,this);//List<yourItem>);
        lv.setAdapter(customAdapter);

        Toast.makeText(this, "buildList", Toast.LENGTH_SHORT).show();
    }
    public void refreshList()
    {
        //this forces the list of items to be updated to show the newly added item
        //if we don't do this, the item will be there, but not shown until the screen refreshes
        final ArrayAdapter adapter = (ArrayAdapter)customAdapter;
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void saveGson(View view){

        Gson gson = new Gson();
        ItemEntry temp;
        String s="";
        Type listType = new TypeToken<ArrayList<ItemEntry>>() {
        }.getType();
        s=gson.toJson(this.recipe.recipeItems,listType);


        Toast.makeText(this,"saveGSON["+s+"]",Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(gsonRecipeFilename, this.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(this, "saveGson file output exception", Toast.LENGTH_SHORT).show();
        }
        //this.refreshList();
        //this.buildList();
        Toast.makeText(this,"saveGSON"+this.recipe.recipeItems.size(),Toast.LENGTH_SHORT).show();
    }

    public void loadGson(View view){

        FileInputStream fis=null;
        StringBuilder sb = new StringBuilder();
        try{
            fis= this.openFileInput(gsonRecipeFilename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch(java.io.FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"loadGSON file not found exception",Toast.LENGTH_SHORT).show();
        }
        catch(java.io.IOException e){
            Toast.makeText(getApplicationContext(),"loadGSON IO exception",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),"Other exception:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        String json = sb.toString();
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<ItemEntry>>() {
        }.getType();
        List<ItemEntry> yourClassList = gson.fromJson(json, listType);


        if(yourClassList != null) {
            this.recipe.recipeItems=yourClassList;
            this.refreshList();
            this.buildList();
            Toast.makeText(getApplicationContext(), "loadGSON" + this.recipe.recipeItems.size(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "loadGSON: Nothing to load", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDeleteClick(int position) {
        recipe.recipeItems.remove(position);
        Toast.makeText(getApplicationContext(), "onDeleteClick: removing item:"+position, Toast.LENGTH_SHORT).show();
        this.refreshList();
        this.buildList();
    }
    public void onEditClick(int position) {
        ItemEntry toEdit=recipe.recipeItems.get(position);
        Toast.makeText(getApplicationContext(), "onEditClick: edit item:"+position, Toast.LENGTH_SHORT).show();
        TextView i=(TextView) this.findViewById(R.id.itemName);
        i.setText(toEdit.theItem.itemName);
        TextView q=(TextView) this.findViewById(R.id.quantity);
        q.setText(toEdit.itemQuantity + "");
        Button updateItem = (Button) this.findViewById(R.id.updateItemButton);
        updateItem.setTag(position);
        updateItem.setEnabled(true);
        this.refreshList();
        this.buildList();
    }

    public void updateRecipeItem(View v){
        Button updateItem = (Button) this.findViewById(R.id.updateItemButton);
        int position=(int)updateItem.getTag();
        ItemEntry toEdit=recipe.recipeItems.get(position);
        TextView i=(TextView) this.findViewById(R.id.itemName);
        TextView q=(TextView) this.findViewById(R.id.quantity);
        toEdit.theItem.itemName=i.getText().toString();
        Toast.makeText(getApplicationContext(), "updateRecipeItem:"+position+"["+i.getText().toString()+"["+q.getText().toString()+"]", Toast.LENGTH_SHORT).show();
        try {
            toEdit.itemQuantity=Double.parseDouble(q.getText().toString());
            updateItem.setEnabled(false);
        }
        catch(NumberFormatException e)
        {
            Toast.makeText(getApplicationContext(), "updateRecipeItem: Quantity not a number", Toast.LENGTH_SHORT).show();
        }
        this.refreshList();
        this.buildList();
    }

}
