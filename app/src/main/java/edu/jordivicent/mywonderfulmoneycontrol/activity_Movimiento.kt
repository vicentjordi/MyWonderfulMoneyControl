package edu.jordivicent.mywonderfulmoneycontrol

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper

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

        //Asignar Botones
        var crear = findViewById(R.id.btnCrear) as Button
        var locate = findViewById(R.id.btLocate) as Button
        var foto = findViewById(R.id.btnFoto) as Button

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        locate.setOnClickListener{locateMe()}
        foto.setOnClickListener{
            //Crear Intent camara
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //Ejecutar camaraLauncher
            camaraLauncher.launch(intent)
        }

    }

    fun locateMe(){
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

}