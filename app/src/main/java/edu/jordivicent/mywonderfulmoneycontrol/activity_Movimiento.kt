package edu.jordivicent.mywonderfulmoneycontrol

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.jordivicent.mywonderfulmoneycontrol.Utils.miSQLiteHelper

class activity_Movimiento : AppCompatActivity() {
    lateinit var moneyControlDB: miSQLiteHelper
    private lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)

        moneyControlDB = miSQLiteHelper(this)

        var crear = findViewById(R.id.btnCrear) as Button
        var locate = findViewById(R.id.btLocate) as Button

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        locate.setOnClickListener{locateMe()}

    }

    fun locateMe(){
        var longitud = findViewById(R.id.ptLong) as EditText
        var latiud = findViewById(R.id.ptLat) as EditText

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
            if (location != null) {
                latiud.setText(location.latitude.toString())
                longitud.setText((location.longitude.toString()))
            }
        }//end_fusedLocation
    }
}