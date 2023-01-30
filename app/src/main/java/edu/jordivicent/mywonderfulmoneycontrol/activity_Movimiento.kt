package edu.jordivicent.mywonderfulmoneycontrol

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.jordivicent.mywonderfulmoneycontrol.Utils.DatePickerFragment
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ActivityMovimientoBinding
import java.io.ByteArrayOutputStream
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
            val img = binding.imgFoto
            //Poner la imagen hecha en el ImageView
            img.setImageBitmap(imageBitmap)
        }
    }//end_camaraLauncher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)

        binding=ActivityMovimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moneyControlDB = miSQLiteHelper(this)
        cargarCate()

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        //Funciones botones
        binding.btnLocate.setOnClickListener{locateMe()}//End_locate
        binding.btnFoto.setOnClickListener{
            //Crear Intent camara
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //Ejecutar camaraLauncher
            camaraLauncher.launch(intent)
        }//end_foto
        binding.ptFecha.setOnClickListener{selectorFecha()}//end_fecha
        binding.btnAddCate.setOnClickListener{addCategoria()}
       // binding.btnCrear.setOnClickListener{crearMovimiento()}//end_crear

    }//end_OnCreate
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

        /*
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            //Comprobar si location es null
            if (location != null) {
                //Pone la latitud
               binding.ptLat.setText(location.latitude.toString())
                //Pone la Longitud
                binding.ptLong.setText((location.longitude.toString()))
            }
        }//end_fusedLocation
        */

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

    /*
        private fun crearMovimiento(){
        //Comprobar que los campos principales no esten vacios
        if(!binding.ptGasto.text.isNullOrBlank() && !binding.ptFecha.text.isNullOrBlank() && !binding.ptImporte.text.isNullOrBlank()){

            if(binding.radioGroup.checkedRadioButtonId==-1){
               //Comprobar que los campos Latitud y longitud no esten vacio
               if(binding.ptLat.text.isNullOrBlank()){
                    binding.ptLat.setText(null)
                    binding.ptLong.setText(null)
               }


               //Convertir ImageView en una array de bytes para poder guardar en SQLite
               val bitmap = (binding.imgFoto.drawable as BitmapDrawable).bitmap
               val stream = ByteArrayOutputStream()

               bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
               val img = stream.toByteArray()

               //Crear sentencia SQLlite dependiendo del radioButton.Checked
               if(binding.rbGasto.isChecked){
                    moneyControlDB.anyaditMovi(binding.ptGasto.text.toString(), binding.ptFecha.toString(), binding.rbGasto.text.toString(),
                        binding.spCategoria.toString(), binding.ptImporte.text.toString(), binding.ptLat.text.toString(),
                        binding.ptLong.text.toString(), img)
               }else{
                   moneyControlDB.anyaditMovi(binding.ptGasto.text.toString(), binding.ptFecha.toString(), binding.rbIngreso.text.toString(),
                       binding.spCategoria.toString(), binding.ptImporte.text.toString(), binding.ptLat.text.toString(),
                       binding.ptLong.text.toString(), img)
               }

           }//if_radioGroup
        }//if_titulo/fecha/importe

        clearAll()
        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)

    }//end_crearMovimiento
    * */

    fun clearAll(){
        binding.ptGasto.text.clear()
        binding.ptFecha.text.clear()
       // binding.ptLat.text.clear()
       // binding.ptLong.text.clear()
        binding.ptImporte.text.clear()
        binding.rbGasto.isChecked = false
        binding.rbIngreso.isChecked = false
        binding.imgFoto.setImageDrawable(null)
    }

}//end_activityMovimiento