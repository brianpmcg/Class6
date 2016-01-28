package com.example.b.class6;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by b on 1/27/2016.
 */
public class Recipe {
    public List<ItemEntry> recipeItems=new ArrayList<ItemEntry>();
    public String recipeName;
    public String instructions;

    public Recipe(String name, String inst)
    {
        recipeName=name;
        instructions=inst;

    }


}
