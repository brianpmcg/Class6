package com.example.b.class6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 1/27/2016.
 */
public class ItemEntryAdapter extends ArrayAdapter<ItemEntry> {
    public interface BtnClickListener {
        public abstract void onDeleteClick(int position);
        public abstract void onEditClick(int position);
    }

    public BtnClickListener clickListener;

        public ItemEntryAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ItemEntryAdapter(Context context, int resource, List<ItemEntry> items) {
            super(context, resource, items);
        }

    public ItemEntryAdapter(Context context, int resource, List<ItemEntry> items,BtnClickListener listener ) {
        super(context, resource, items);
        clickListener = listener;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.item_entry_row, null);
            }

            ItemEntry i = this.getItem(position);

            if (i != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.column1);
                TextView tt2 = (TextView) v.findViewById(R.id.column2);
                TextView tt3 = (TextView) v.findViewById(R.id.column3);

                if (tt1 != null) {
                    tt1.setText(i.theItem.itemName);
                }

                if (tt2 != null) {
                    tt2.setText(""+i.itemQuantity);
                }

                if (tt3 != null) {
                    tt3.setText(i.itemQuantityUnits.name());
                }
            }

            Button deleteB = (Button) v.findViewById(R.id.deleteButton);
            deleteB.setTag(position); //For passing the list item index
            deleteB.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(clickListener != null)
                        clickListener.onDeleteClick((Integer) v.getTag());
                }
            });

            Button editB = (Button) v.findViewById(R.id.editButton);
            editB.setTag(position); //For passing the list item index
            editB.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(clickListener != null)
                        clickListener.onEditClick((Integer) v.getTag());
                }
            });

            return v;
        }

    }