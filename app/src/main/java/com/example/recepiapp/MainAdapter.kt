package com.example.recepiapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.listrecipe.view.*

class MainAdapter(val homeRecipe: HomeRecipe): RecyclerView.Adapter<CustomViewHolder>(){

    //Item list recipe
    override fun getItemCount(): Int {
    return homeRecipe.recipe.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.listrecipe, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val recipe = homeRecipe.recipe.get(position)
        holder?.view?.textView_Name?.text = recipe.name
        holder?.view?.textView_type.text = recipe.type

        val thumbnailImageview = holder?.view?.imageView_Image
        Picasso.with(holder?.view?.context).load(recipe.image).into(thumbnailImageview)

        holder?.recipe = recipe
    }
}

class CustomViewHolder(val view: View, var recipe: Recipe? = null): RecyclerView.ViewHolder(view){

    companion object{
        val RECIPE_NAME_KEY = "RECIPE_NAME"
        val RECIPE_ID_KEY = "RECIPE_ID"
        val RECIPE_INGREDIENT_KEY = "RECIPE_INGREDIENT"
        val RECIPE_STEPS_KEY = "RECIPE_STPES"
        val RECIPE_IMAGE_KEY = "RECIPE_IMAGE"
        val RECIPE_TYPE_KEY = "RECIPE_TYPE"

    }
    init {
        view.setOnClickListener {
            val intent = Intent(view.context, UpdateRecipe::class.java)
            intent.putExtra(RECIPE_NAME_KEY, recipe?.name)
            intent.putExtra(RECIPE_ID_KEY, recipe?.recepi_id)
            intent.putExtra(RECIPE_INGREDIENT_KEY, recipe?.ingredient)
            intent.putExtra(RECIPE_STEPS_KEY, recipe?.steps)
            intent.putExtra(RECIPE_IMAGE_KEY, recipe?.image)
            intent.putExtra(RECIPE_TYPE_KEY, recipe?.type)
            view.context.startActivity(intent)
        }


    }
}
