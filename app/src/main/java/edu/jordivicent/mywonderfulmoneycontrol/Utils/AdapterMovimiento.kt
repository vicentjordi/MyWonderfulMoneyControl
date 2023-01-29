package edu.jordivicent.mywonderfulmoneycontrol.Utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.jordivicent.mywonderfulmoneycontrol.R
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ListaMovimientosBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
/*
class AdapterMovimiento : RecyclerView.Adapter<AdapterMovimiento.ViewHolder>(){
    lateinit var context: Context
    lateinit var cursor: Cursor


    fun RecyclerViewMovimiento(context: Context, cursor: Cursor){
        this.context = context
        this.cursor = cursor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder((inflater.inflate(R.layout.activity_main, parent, false)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)

        holder.etTitulo.text = cursor.getString(1)
        holder.etFecha.text = cursor.getString(2)
        holder.etTipo.text = cursor.getString(3)
        holder.etCate.text = cursor.getString(4)
        holder.etImporte.text = cursor.getString(5)
        holder.etLat.text = cursor.getString(6)
        holder.etLong.text = cursor.getString(7)


        // holder.imgFoto.drawable = cursor.getBlob(8)
    }

    override fun getItemCount(): Int {
        if(cursor==null){
            return 0
        }else{
            return cursor.count
        }

    }

    inner class  ViewHolder: RecyclerView.ViewHolder {
        val etTitulo: TextView
        val etFecha: TextView
        val etTipo: TextView
        val etCate: TextView
        val etImporte: TextView
        val etLong: TextView
        val etLat: TextView
        val imgFoto: ImageView

      constructor(view: View) : super (view){
            val bindingItem = ListaMovimientosBinding.bind(view)
            etTitulo = bindingItem.etTItulo
            etFecha = bindingItem.etFecha
            etTipo = bindingItem.etTipo
            etCate = bindingItem.etCate
            etImporte = bindingItem.etImporte
            etLat = bindingItem.etLat
            etLong =bindingItem.etLong
            imgFoto = bindingItem.imgFoto
        }


    }


}
*/
