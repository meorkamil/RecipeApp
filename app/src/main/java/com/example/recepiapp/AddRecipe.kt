package com.example.recepiapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddRecipe : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)


        button_add_recepi.setOnClickListener {
            val type = spinner_type.selectedItem.toString()
            val name = add_name.text.toString()
            val ingredient = add_ingredient.text.toString()
            val steps = add_step.text.toString()
            //addDb(name, type, ingredient, steps)
            upload(name, type, ingredient, steps)
        }
        imageView_add.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,90)
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

    private fun upload(name: String, type: String, ingredient: String, steps: String) {
        val c = OkHttpClient()
        //val r = Request.Builder().url("").build()
        val name = name
        val type = type
        val ingredient = ingredient
        val steps = steps
        val stream = ByteArrayOutputStream()
        b!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("namerecipe", name)
            .addFormDataPart("ingredient", ingredient)
            .addFormDataPart("steps",steps)
            .addFormDataPart("type", type)
            .addFormDataPart("photo",randomName() +"fn.jpg",
                RequestBody.create("image/*jpg".toMediaTypeOrNull(),byteArray)).build()
        val r = Request.Builder().url("https://mk97team.com/recipe/upload.php").post(requestBody).build()

        c.newCall(r).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HI", e.message)
                e.stackTrace
            }

            override fun onResponse(call: Call, response: Response) {

                val intent = Intent(this@AddRecipe, MainActivity::class.java)
                startActivity(intent)

            }
        })
    }

    var b: Bitmap?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==90 && resultCode == Activity.RESULT_OK && data!=null){
            b = data.extras?.get("data") as Bitmap
            imageView_add.setImageBitmap(b)
        }
    }
}