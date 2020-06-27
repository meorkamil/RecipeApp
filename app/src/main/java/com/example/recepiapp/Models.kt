package com.example.recepiapp

import android.telecom.Call

class HomeRecipe(val recipe: List<Recipe>)

class Recipe(val recepi_id: String, val ingredient: String, val name: String, val steps: String, val image: String, val type: String)

