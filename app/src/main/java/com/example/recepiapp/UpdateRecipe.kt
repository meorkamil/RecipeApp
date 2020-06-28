package com.example.recepiapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add_recipe.*
import kotlinx.android.synthetic.main.update_layout.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Paths.get

class UpdateRecipe : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_layout)

        val name = intent.getStringExtra(CustomViewHolder.RECIPE_NAME_KEY)
        val ingredient = intent.getStringExtra(CustomViewHolder.RECIPE_INGREDIENT_KEY)
        val id = intent.getStringExtra(CustomViewHolder.RECIPE_ID_KEY)
        val steps = intent.getStringExtra(CustomViewHolder.RECIPE_STEPS_KEY)
        val imageUrl = intent.getStringExtra(CustomViewHolder.RECIPE_IMAGE_KEY)

        Picasso.with(this).load(imageUrl).into(image_update_display)
        update_name.setText(name)
        update_ingredient.setText(ingredient)
        update_steps.setText(steps)

        image_update_display.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,90)
        }

        button_delete.setOnClickListener {
            val id_delete = id.toString()
            println("delete")
            deleteDb(id_delete)
        }
        button_update.setOnClickListener {
            val id_update = id.toString()
            val name_update = update_name.text.toString()
            val type_update = spinner_update.selectedItem.toString()
            val ingredient_update = update_ingredient.text.toString()
            val steps_update = update_steps.text.toString()
            upload(id_update, name_update, type_update, ingredient_update, steps_update)
            println("update")
        }



    }
    fun randomName(): String {
        val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var nameWord = ""
        for (i in 0..31) {
            nameWord += chars[Math.floor(Math.random() * chars.length).toInt()]
        }
        return nameWord
    }

    private fun upload(id: String, name: String, type: String, ingredient: String, steps: String) {
        val c = OkHttpClient()
        val name = name
        val type = type
        val ingredient =ingredient
        val steps = steps
        val id = id
        val stream = ByteArrayOutputStream()
        b!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("recipeid",id)
            .addFormDataPart("namerecipe", name)
            .addFormDataPart("ingredient", ingredient)
            .addFormDataPart("steps",steps)
            .addFormDataPart("type", type)
            .addFormDataPart("photo",randomName() +"fn.jpg",
                RequestBody.create("image/*jpg".toMediaTypeOrNull(),byteArray)).build()
        val r = Request.Builder().url("https://mk97team.com/recipe/update.php").post(requestBody).build()

        c.newCall(r).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.stackTrace
            }

            override fun onResponse(call: Call, response: Response) {

                val intent = Intent(this@UpdateRecipe, MainActivity::class.java)
                startActivity(intent)

            }
        })
    }

    var b: Bitmap?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==90 && resultCode == Activity.RESULT_OK && data!=null){
            b = data.extras?.get("data") as Bitmap
            image_update_display.setImageBitmap(b)
        }
    }

    fun deleteDb(id: String){
        val id = id
        val url ="https://mk97team.com/recipe/delete.php?recipeid=" + id
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("failed")
            }

            override fun onResponse(call: Call, response: Response) {
                println("success")
                runOnUiThread {
                    val intent = Intent(this@UpdateRecipe, MainActivity::class.java)
                    startActivity(intent)
                }

            }


        })
    }
}