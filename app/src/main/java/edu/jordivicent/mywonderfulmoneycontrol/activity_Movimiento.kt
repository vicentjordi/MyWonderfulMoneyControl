package edu.jordivicent.mywonderfulmoneycontrol

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.jordivicent.mywonderfulmoneycontrol.Utils.DatePickerFragment
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ActivityMovimientoBinding
import java.io.ByteArrayOutputStream
import java.util.*

class activity_Movimiento : AppCompatActivity(), OnMapReadyCallback {//end_activityMovimiento
    lateinit var moneyControlDB: miSQLiteHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var binding: ActivityMovimientoBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    var currentLatLng: String = ""

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

        moneyControlDB = miSQLiteHelper(this, null)
        cargarCate()

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        binding.mapView.getMapAsync(this)

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
        binding.btnCrear.setOnClickListener{crearMovimiento()}//end_crear
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

        //Marcamos nuestra posción actual
       // mMap.isMyLocationEnabled = true

        //Va a la última ubicación conocida
        fusedLocation.lastLocation.addOnSuccessListener(this) { location ->
            if(location != null){
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude).toString()
                println(currentLatLng.toString())
                //mMap.animateCamera(
               //     CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f)
               // )
            }
        }
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
                category.add(cursor.getString(1).toString())
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
            setPositiveButton(R.string.btnAceptar){dialog, wich ->
                if(!etNuevaCat.text.isNullOrBlank()){
                    //si el etNuevaCat no esta vacia se añade a la base de datos
                    moneyControlDB.addCategoria(etNuevaCat.text.toString())
                    //Acutalizamos el spinner de categoria
                    cargarCate()
                }
            }
            setNegativeButton(R.string.btnCacelar){dialog, wich ->
                Log.d("Movimiento", R.string.CancelarCate.toString())
            }
            //Mostrar el dialog
            setView(dialogLayout)
            show()
        }//endWith
    }//end_addCategoria

    private fun crearMovimiento(){
        //Comprobar que los campos principales no esten vacios
        if(!binding.ptGasto.text.isNullOrBlank() && !binding.ptFecha.text.isNullOrBlank() && !binding.ptImporte.text.isNullOrBlank()){

            //Convertir ImageView en una array de bytes para poder guardar en SQLite
            val bitmap = (binding.imgFoto.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val img = stream.toByteArray()

            //Crear sentencia SQLlite dependiendo del radioButton.Checked
            if(binding.rbGasto.isChecked){
                moneyControlDB.addMovimiento(binding.ptGasto.text.toString(), binding.ptFecha.text.toString(), binding.rbGasto.text.toString(),
                    binding.spCategoria.selectedItem.toString(), binding.ptImporte.text.toString(), currentLatLng, img)

                clearAll()
                val myIntent = Intent(this, MainActivity::class.java)
                startActivity(myIntent)

            }else if(binding.rbIngreso.isChecked){
                moneyControlDB.addMovimiento(binding.ptGasto.text.toString(), binding.ptFecha.text.toString(), binding.rbIngreso.text.toString(),
                    binding.spCategoria.selectedItem.toString(), binding.ptImporte.text.toString(), currentLatLng, img)

                clearAll()
                val myIntent = Intent(this, MainActivity::class.java)
                startActivity(myIntent)

            }
        }//if_titulo/fecha/importe
        else{
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.errorGuardar)
            builder.setMessage(R.string.errorProblema)

            builder.setPositiveButton(R.string.btnAceptar) { dialog, which ->
                Toast.makeText(applicationContext,
                    R.string.btnAceptar, Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }
    }//end_crearMovimiento

    fun clearAll(){
        binding.ptGasto.text.clear()
        binding.ptFecha.text.clear()
        binding.ptImporte.text.clear()
        binding.rbGasto.isChecked = false
        binding.rbIngreso.isChecked = false
        binding.imgFoto.setImageDrawable(null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        locateMe()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

}