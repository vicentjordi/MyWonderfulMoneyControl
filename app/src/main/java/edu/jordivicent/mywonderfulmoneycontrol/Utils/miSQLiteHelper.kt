package edu.jordivicent.mywonderfulmoneycontrol.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import java.sql.Blob
import java.util.Date

class miSQLiteHelper(context: Context) : SQLiteOpenHelper(

    context,"moneyControl.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val  createCate = "CREATE TABLE IF NOT EXISTS categoria " +
                "(categoria TEXT PRIMARY KEY);"

        val createMovimiento = "CREATE TABLE IF NOT EXISTS movimiento" +
                "(_id INT PRIMARY KEY AUTOINCREMENT, titulo TEXT, fecha DATE, " +
                "tipo TEXT, cate TEXT, importe TEXT, " +
                "latitud TEXT, longitud TEXT, recibo BLOB);"

        db!!.execSQL(createCate)
        db.execSQL(createMovimiento)
    }//onCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion1: Int, newVersion2: Int) {
        val delCate = "DROP TABLE IF EXISTS categoria"
        val delMovi = "DROP TABLE IF EXISTS movimiento"
        db!!.execSQL(delCate)
        db!!.execSQL(delMovi)
        onCreate(db)
    } // on Upgrade

    fun anyadirCategoria(categoria: String){
        val datos = ContentValues()
        datos.put("categoria", categoria)


        val db = this.writableDatabase
        db.insert("categoria", null, datos)

        db.close()
    }//end_anyadirCategoria
    fun anyaditMovi(titulo: String, fecha: String, tipo: String, cate: String,
        importe: String, lati: String, longi: String, recibo: ByteArray ){

        val datos = ContentValues()

        datos.put("titulo", titulo)
        datos.put("fecha", fecha)
        datos.put("tipo", tipo)
        datos.put("cate", cate)
        datos.put("importe", importe)
        datos.put("lati",lati)
        datos.put("longi", longi)
        datos.put("recibo",recibo)


        val db = this.writableDatabase
        db.insert("movimiento", null, datos)

        db.close()
    }

}