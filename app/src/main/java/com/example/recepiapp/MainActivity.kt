package com.example.recepiapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_recipe.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycleViewMain.layoutManager = LinearLayoutManager(this)
        spinner_search.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type =  parent?.getItemAtPosition(position).toString()
                fetchJson(type)
            }
        }

    }

    fun fetchJson(type: String){
        val url ="https://mk97team.com/recipe/listrecipe.php?type=" + type
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
               val gson = GsonBuilder().create()
               val homeRecipe = gson.fromJson(body, HomeRecipe::class.java)

                runOnUiThread {
                    recycleViewMain.adapter = MainAdapter(homeRecipe)
                }
            }
        })
    }
}
