package edu.jordivicent.mywonderfulmoneycontrol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper

class activity_Movimiento : AppCompatActivity() {
    lateinit var moneyControlDB: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)

        moneyControlDB = miSQLiteHelper(this)

        var crear = findViewById(R.id.btnCrear) as Button

    }
}