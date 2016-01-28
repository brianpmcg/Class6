package com.example.b.class6;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private Recipe recipe;
    ItemEntryAdapter customAdapter;
    public final String gsonFilename = "Class6.json";
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

        Button showPrefB = (Button) this.findViewById(R.id.showPref);
        showPrefB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreference(view);
            }
        });

        buildList();

        //This uses the custom adapter in ItemEntryAdapter.java
        /*ListView lv = (ListView) findViewById(R.id.recipeListView);
        customAdapter = new ItemEntryAdapter(this, R.layout.item_entry_row, recipe.recipeItems);//List<yourItem>);
        lv.setAdapter(customAdapter);
        */
        //this section just creates a list using the ItemEntry.toString() method
       /* ListView lv = (ListView) findViewById(R.id.recipeListView);
        ArrayAdapter<ItemEntry> arrayAdapter = new ArrayAdapter<ItemEntry>(
                this,
                android.R.layout.simple_list_item_1,
                recipe.recipeItems );

        lv.setAdapter(arrayAdapter);*/
    }

    public void addItemToRecipe(View view){
        TextView i=(TextView) this.findViewById(R.id.itemName);
        TextView q=(TextView) this.findViewById(R.id.quantity);
        Item item=new Item(i.getText().toString());
        try {
            ItemEntry itemEntry = new ItemEntry(item, Double.parseDouble(q.getText().toString()), ItemEntry.Units.other);
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
        customAdapter = new ItemEntryAdapter(this, R.layout.item_entry_row, recipe.recipeItems);//List<yourItem>);
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
        /*Iterator<ItemEntry> recipeIterator = this.recipe.recipeItems.iterator();
        while (recipeIterator.hasNext()) {
            s=s+ gson.toJson(recipeIterator.next());
        }*/
        

        Toast.makeText(this,"saveGSON["+s+"]",Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(gsonFilename, this.MODE_PRIVATE);
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
            fis= this.openFileInput(gsonFilename);
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
}
