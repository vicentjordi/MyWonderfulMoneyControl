package edu.jordivicent.mywonderfulmoneycontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btAdd.setOnClickListener{
            val myIntent = Intent(this, activity_Movimiento::class.java)

            startActivity(myIntent)
        }
    }
}