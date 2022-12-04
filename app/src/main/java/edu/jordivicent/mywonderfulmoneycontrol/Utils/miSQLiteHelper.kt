package edu.jordivicent.mywonderfulmoneycontrol.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class miSQLiteHelper(context: Context) : SQLiteOpenHelper(

    context,"moneyControl.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val  ordenCreacion = "CREATE TABLE categoria " +
                "(_id STRING PRIMARY KEY AUTOINCREMENT," +
                "gasto TEXT NOT NULL, fecha DATE NOT NULL, " +
                "tipo TEXT NOT NULL, categoria TEXT NOT NULL, " +
                "importe TEXT NOT NULL, longitude TEXT, latitude TEXT, img TEXT)"

        db!!.execSQL(ordenCreacion)
    }//onCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion1: Int, newVersion2: Int) {
        val ordenBorrado = "DROP TABLE IF EXISTS categoria"
        db!!.execSQL(ordenBorrado)
        onCreate(db)
    } // on Upgrade

    fun anyadirMovimiento(categoria: String){
        val datos = ContentValues()
        datos.put("categoria", categoria)
        datos

        val db = this.writableDatabase
        db.insert("categoria", null, datos)

        db.close()
    }//end_anyadirMovimiento

}