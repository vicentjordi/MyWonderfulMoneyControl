package edu.jordivicent.mywonderfulmoneycontrol.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class miSQLiteHelper(context: Context) : SQLiteOpenHelper(

    context,"moneyControl.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val  createCate = "CREATE TABLE IF NOT EXISTS categoria " +
                "(categoria TEXT PRIMARY KEY)"

        db!!.execSQL(createCate)
    }//onCreate

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion1: Int, newVersion2: Int) {
        val delCate = "DROP TABLE IF EXISTS categoria"
        db!!.execSQL(delCate)
        onCreate(db)
    } // on Upgrade

    fun anyadirCategoria(categoria: String){
        val datos = ContentValues()
        datos.put("categoria", categoria)
        datos

        val db = this.writableDatabase
        db.insert("categoria", null, datos)

        db.close()
    }//end_anyadirCategoria

}