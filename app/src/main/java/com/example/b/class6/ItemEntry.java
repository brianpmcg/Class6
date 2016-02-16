package com.example.b.class6;

/**
 * Created by b on 1/27/2016.
 */
public class ItemEntry {
    public Item theItem;
    public double itemQuantity;
    public Units itemQuantityUnits;

    public enum Units {cup, tbsp,tsp,other };

    public ItemEntry(Item item, double quantity, Units unit){
        theItem=item;
        itemQuantity=quantity;
        itemQuantityUnits=unit;

    }
    public ItemEntry(Item item){
        theItem=item;
        itemQuantity=1.0;
        itemQuantityUnits=Units.other;

    }

    public String toString()
    {
        return theItem.itemName+" "+itemQuantity+" "+itemQuantityUnits.name();
    }

}
