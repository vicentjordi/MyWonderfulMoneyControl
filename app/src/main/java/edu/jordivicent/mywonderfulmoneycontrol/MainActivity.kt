package edu.jordivicent.mywonderfulmoneycontrol

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import edu.jordivicent.mywonderfulmoneycontrol.Adapters.AdapterMovimiento
//import edu.jordivicent.mywonderfulmoneycontrol.Utils.AdapterMovimiento
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var moneyControlDB: miSQLiteHelper
    private lateinit var db: SQLiteDatabase

    private var category: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moneyControlDB = miSQLiteHelper(this, null)

        binding.btnAdd.setOnClickListener{
            val myIntent = Intent(this, activity_Movimiento::class.java)

            startActivity(myIntent)
        }//btAdd_setOnClickListener

        verPermisos()
        compCategoriaSql()
       // movimientoSql()
        filtroCate()
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
        val filtroCat = binding.spFiltroCat
        db = moneyControlDB.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM categoria", null)

        //Si la tabla tiene datos, mostrara los datos
        if(cursor.moveToFirst()){
            println("NO Empty")
            category.add("Todos")
            do{
                //Añade la string donde esta el cursor a la lista
                category.add(cursor.getString(1).toString())
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
            moneyControlDB.addCategoria("Ropa")
            moneyControlDB.addCategoria("Comida")
            moneyControlDB.addCategoria("Accesorios")
            moneyControlDB.addCategoria("Facturas")
            moneyControlDB.addCategoria("Libros")

            if(cursor.moveToFirst()) {
                do {
                    category.add(cursor.getString(0))
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

    private fun filtroCate() {
        val adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, category)
        binding.spFiltroCat.adapter = adapter

        binding.spFiltroCat.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    if (adapter.getItem(p2).toString().equals("Todos")){
                        println("Todos")
                        movimientoSql()
                    }else{
                        println(adapter.getItem(p2).toString())
                        filtroSql(adapter.getItem(p2).toString())
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
    }

    private fun movimientoSql() {
        //Instancia la BD en modo lectura y se aplica el SELECT
        db = moneyControlDB.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${miSQLiteHelper.TABLA_MOVIMIENTO};", null
        )
        //Se crea el adaptador con el resultado del cursor
        val adaptador = AdapterMovimiento()
        adaptador.MyRecyclerViewAdapter(this, cursor)

        //Montamos el RecyclerView
        binding.rvMovimientos.setHasFixedSize(true)
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        binding.rvMovimientos.adapter=adaptador
    }

    private fun filtroSql(cate: String){
        db = moneyControlDB.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${miSQLiteHelper.TABLA_MOVIMIENTO} WHERE " +
                    miSQLiteHelper.COLUMNA_CATEGORIA + " LIKE ?;", arrayOf(cate)
        )
        //Se crea el adaptador con el resultado del cursor
        val adaptador = AdapterMovimiento()
        adaptador.MyRecyclerViewAdapter(this, cursor)

        //Montamos el RecyclerView
        binding.rvMovimientos.setHasFixedSize(true)
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        binding.rvMovimientos.adapter=adaptador
    }


    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

}