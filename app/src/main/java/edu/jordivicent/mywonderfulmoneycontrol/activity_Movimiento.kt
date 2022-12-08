package edu.jordivicent.mywonderfulmoneycontrol

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.jordivicent.mywonderfulmoneycontrol.Utils.DatePickerFragment
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ActivityMovimientoBinding
import java.util.*

class activity_Movimiento : AppCompatActivity() {
    lateinit var moneyControlDB: miSQLiteHelper
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var binding : ActivityMovimientoBinding

    //Crear launcher para la activity de la camara
    private val camaraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
        if(activityResult.resultCode == RESULT_OK){
            //Guardar los datos de la imagen
            val intent = activityResult.data
            //Convertir los datos de intent en Bitmap
            val imageBitmap = intent?.extras?.get("data") as Bitmap
            val img = findViewById<ImageView>(R.id.imgFoto)
            //Poner la imagen hecha en el ImageView
            img.setImageBitmap(imageBitmap)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)

        binding=ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moneyControlDB = miSQLiteHelper(this)
        cargarCate()

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        //Funciones botones
        binding.btLocate.setOnClickListener{locateMe()}//End_locate
        binding.btnFoto.setOnClickListener{
            //Crear Intent camara
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //Ejecutar camaraLauncher
            camaraLauncher.launch(intent)
        }//end_foto
        binding.ptFecha.setOnClickListener{selectorFecha()}//end_fecha
        binding.btAddCate.setOnClickListener{addCategoria()}
        binding.btnCrear.setOnClickListener{crearMovimiento()}//end_crear

    }


    private fun locateMe(){
        //Comprovar perimisos de Localizacion
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocation.lastLocation.addOnSuccessListener { location ->
            //Comprobar si location es null
            if (location != null) {
                //Pone la latitud
                binding.ptLat.setText(location.latitude.toString())
                //Pone la Longitud
                binding.ptLong.setText((location.longitude.toString()))
            }
        }//end_fusedLocation
    }//end_locateMe

    private fun selectorFecha(){
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }//end_SelectorFecha

    fun onDateSelected(day:Int, month:Int, year:Int){
        binding.ptFecha.setText("$day/$month/$year")
    }//end_onDateSelected

    private fun cargarCate(){
        var category: MutableList<String> = ArrayList()
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
            binding.spCategoria.adapter=arrayAdapter
        }//end_if

    }//end_cargarCate

    private fun addCategoria(){
        //Crear dialog
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        //Usar la layout anyadir_categoria
        val dialogLayout = inflater.inflate(R.layout.anyadir_categoria, null)
        //obtener el editText de la layout anyadir_categoria
        val etNuevaCat = dialogLayout.findViewById<EditText>(R.id.et_nuevaCategoria)
        //Creamos el dialog con el builder
        with (builder) {
            setTitle("Nueva Categoria")
            setPositiveButton("Aceptar"){dialog, wich ->
                if(!etNuevaCat.text.isNullOrBlank()){
                    //si el etNuevaCat no esta vacia se añade a la base de datos
                    moneyControlDB.anyadirCategoria(etNuevaCat.text.toString())
                    //Acutalizamos el spinner de categoria
                    cargarCate()
                }
            }
            setNegativeButton("Cancel"){dialog, wich ->
                Log.d("Movimiento", "Se ha cancelado la operación ")
            }
            //Mostrar el dialog
            setView(dialogLayout)
            show()
        }//endWith
    }//end_addCategoria
    private fun crearMovimiento(){

    }//end_crearMovimiento

}//end_activityMovimiento