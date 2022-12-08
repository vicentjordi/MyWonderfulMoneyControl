package edu.jordivicent.mywonderfulmoneycontrol

import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var moneyControlDB: miSQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moneyControlDB = miSQLiteHelper(this)

        binding.btAdd.setOnClickListener{
            val myIntent = Intent(this, activity_Movimiento::class.java)

            startActivity(myIntent)
        }//btAdd_setOnClickListener

        verPermisos()
        compCategoriaSql()

    }//end_OnCreate

    private fun verPermisos(){
        if ((ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    ) || (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    ) || (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED
                    )) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CAMERA),
                248
            )//end_requestPermissions
        }//end_if
    }//end_verPermisos

    private fun compCategoriaSql(){
        var category: MutableList<String> = ArrayList()
        val filtroCat = binding.spFiltroCat
        val db : SQLiteDatabase = moneyControlDB.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM categoria", null)

        //Si la tabla tiene datos, mostrara los datos
        if(cursor.moveToFirst()){
            println("NO Empty")
            do{
                //Añade la string donde esta el cursor a la lista
                category.add(cursor.getString(0).toString())
            }while (cursor.moveToNext())
            println(category.size)
            //Adaptador para que el spinner sea un dropdawn y carge los items de la lista
            val arrayAdapter = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, category)
            //Aplicamos el filtro sobre el adaptador
            filtroCat.adapter=arrayAdapter
        }
        //Si la tabla categoria esta vacia, La rellena con algunos datos y la muestra
        else{
            println("Empty")
            moneyControlDB.anyadirCategoria("Ropa")
            moneyControlDB.anyadirCategoria("Comida")
            moneyControlDB.anyadirCategoria("Accesorios")
            moneyControlDB.anyadirCategoria("Facturas")
            moneyControlDB.anyadirCategoria("Libros")

            if(cursor.moveToFirst()) {
                do {
                    category.add(cursor.getString(0).toString())
                } while (cursor.moveToNext())

                val arrayAdapter = ArrayAdapter<String>(
                    this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    category
                )
                filtroCat.adapter = arrayAdapter
            }//end_if
        }//end_else
    }//end_compCategSQL

}