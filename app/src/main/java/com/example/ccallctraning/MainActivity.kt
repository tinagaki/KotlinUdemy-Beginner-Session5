package com.example.ccallctraning

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val arrayAdapter= ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item)
//        arrayAdapter.add(10)
//        arrayAdapter.add(20)
//        arrayAdapter.add(30)
//
        val arrayAdapter =
            ArrayAdapter.createFromResource(this, R.array.number_of_question, android.R.layout.simple_spinner_item)
        spinner.adapter = arrayAdapter
        button.setOnClickListener {
            val numberOfQuestion:Int = spinner.selectedItem.toString().toInt()

            val intent = Intent(this@MainActivity,TestActivity::class.java)
            intent.putExtra("numberOfQuestion",numberOfQuestion)
            startActivity(intent)

        }
    }
}
