package edu.jordivicent.mywonderfulmoneycontrol

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import edu.jordivicent.mywonderfulmoneycontrol.Utils.DatePickerFragment
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper
import java.util.*

class activity_Movimiento : AppCompatActivity() {
    lateinit var moneyControlDB: miSQLiteHelper
    private lateinit var fusedLocation: FusedLocationProviderClient

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

        moneyControlDB = miSQLiteHelper(this)

        //Asignar UI
        var crear = findViewById(R.id.btnCrear) as Button
        var locate = findViewById(R.id.btLocate) as Button
        var foto = findViewById(R.id.btnFoto) as Button
        var fecha = findViewById(R.id.ptFecha) as EditText


        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        //Funciones botones
        locate.setOnClickListener{locateMe()}//End_locate
        foto.setOnClickListener{
            //Crear Intent camara
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //Ejecutar camaraLauncher
            camaraLauncher.launch(intent)
        }//end_foto
        fecha.setOnClickListener{selectorFecha()}//end_fecha
        crear.setOnClickListener{crearMovimiento()}//end_crear

    }
    private fun locateMe(){
        var longitud = findViewById(R.id.ptLong) as EditText
        var latiud = findViewById(R.id.ptLat) as EditText

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
                latiud.setText(location.latitude.toString())
                //Pone la Longitud
                longitud.setText((location.longitude.toString()))
            }
        }//end_fusedLocation
    }//end_locateMe

    private fun selectorFecha(){
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }//end_SelectorFecha

    fun onDateSelected(day:Int, month:Int, year:Int){
        var ptfecha = findViewById(R.id.ptFecha) as EditText
        ptfecha.setText("$day/$month/$year")
    }//end_onDateSelected

    private fun crearMovimiento(){
        var titulo = findViewById(R.id.ptGasto) as EditText
        var fecha = findViewById(R.id.ptFecha) as EditText
        var ingreso = findViewById(R.id.rbIngreso) as RadioButton
        var gasto = findViewById(R.id.rbGasto) as RadioButton
        var importe = findViewById(R.id.ptImporte) as EditText
        var rbgroup = findViewById(R.id.radioGroup) as RadioGroup
        var categoria = findViewById(R.id.spCategoria) as Spinner
        var locate = findViewById(R.id.ptLong) as EditText
        var recibo = findViewById(R.id.imgFoto) as ImageView

        //Comprovamos si los principales campos estan vacios
        if(titulo.text.isNotBlank() && fecha.text.isNotBlank() && rbgroup.checkedRadioButtonId !=-1
            && categoria.selectedItem != null && importe.text.isNotBlank()){

            //si no estan vacios comprobaremos los opcionales
            if(locate.text.isBlank()){
                //si está vacio sera null
            }
            if(recibo.getDrawable()==null){
                //si es esta vacio imgDefoult
            }

            //Una vez comprovados se añadiran a la tabla
        }else{
            //Mostrar Mensaje error en caso de que algun campo obligatoio este vacio
        }
    }

}