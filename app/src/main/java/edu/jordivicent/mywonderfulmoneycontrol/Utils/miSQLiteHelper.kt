package edu.jordivicent.mywonderfulmoneycontrol.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log
import java.sql.Blob
import java.util.Date

class miSQLiteHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){
        val TAG = "SQLite"
        companion object {
        //Declaramos la versión y el nombre de la bd.
            val DATABASE_VERSION = 1
            val DATABASE_NAME = "moneyControl.db"
            // Se Declara la tabla categoria y sus columnas
            val TABLA_CATEGORIA = "categoria"
            val COLUMNA_ID = "_id"
            val COLUMNA_CATEGORIA = "categoria"
            // Se Declara la tabla movimiento y sus columnas
            val TABLA_MOVIMIENTO = "movimiento"
            val COLUMNA_TITULO = "titulo"
            val COLUMNA_FECHA = "fecha"
            val COLUMNA_TIPO = "tipo"
            val COLUMNA_IMPORTE = "importe"
            val COLUMNA_LATLONG = "latlong"
            val COLUMNA_RECIBO = "recibo"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        try{
            //Se crea la tabla de categoria
            val crearTablaCategoria = "CREATE TABLE $TABLA_CATEGORIA " +
                    "($COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_CATEGORIA TEXT)"

            //Se crea la tabla de movimientos
            val crearTablaMovimiento = "CREATE TABLE $TABLA_MOVIMIENTO " +
                    "($COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMNA_TITULO TEXT, $COLUMNA_FECHA DATE, $COLUMNA_TIPO TEXT, " +
                    "$COLUMNA_CATEGORIA TEXT, $COLUMNA_IMPORTE TEXT, " +
                    "$COLUMNA_LATLONG TEXT, $COLUMNA_RECIBO BLOB)"

            db!!.execSQL(crearTablaCategoria)
            db!!.execSQL(crearTablaMovimiento)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCREATE)", e.message.toString())
        }
    }//OnCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Se invoca onUpgrade cuando la base de datos necesia ser actualizada
        try{
            val dropTablaCategoria = "DROP TABLE IF EXISTS $TABLA_CATEGORIA"
            val dropTablaMovimiento = "DROP TABLE IF EXISTS $TABLA_MOVIMIENTO"
            db!!.execSQL(dropTablaCategoria)
            db!!.execSQL(dropTablaMovimiento)
            onCreate(db)
        }catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
        }
    }//OnUpgrade

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.e("$TAG (onUpgrade)", "Base de datos abierta")
    }

    fun addCategoria(categoria: String){
        //Se crea un ArrayMap<>() haciendo uso de ContentValues
        val data = ContentValues()
        data.put("categoria", categoria)
        //Se abre la BD en modo escritura
        val db = this.writableDatabase
        db.insert("categoria", null, data)
        //Se cierra la BD
        db.close()
    }//addCategoria

    fun addMovimiento(titulo: String, fecha: String, tipo: String, categoria: String,
                      importe: String, latlong: String, recibo: ByteArray){
        //Se crea un ArrayMap<>() haciendo uso de ContentValues
        val data = ContentValues()
        data.put("titulo", titulo)
        data.put("fecha", fecha)
        data.put("tipo", tipo)
        data.put("categoria", categoria)
        data.put("importe", importe)
        data.put("latlong",latlong)
        data.put("recibo",recibo)
        //Se abre la BD en modo escritura
        val db = this.writableDatabase
        db.insert("movimiento", null, data)
        db.close()
    }
}


