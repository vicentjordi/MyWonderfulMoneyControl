package edu.jordivicent.mywonderfulmoneycontrol.Adapters

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.database.getBlobOrNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.maps.MapView
import edu.jordivicent.mywonderfulmoneycontrol.R
import edu.jordivicent.mywonderfulmoneycontrol.databinding.ListaMovimientosBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.FieldPosition

class AdapterMovimiento : RecyclerView.Adapter<AdapterMovimiento.ViewHolder>(){
    lateinit var context: Context
    lateinit var cursor: Cursor

    fun MyRecyclerViewAdapter(context: Context, cursor: Cursor) {
        this.context = context
        this.cursor = cursor
    }

    //Infla la vista de los items.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RECYCLERVIEW", "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.lista_movimientos, parent, false))
    }

    override fun getItemCount(): Int {
        return  cursor.count
    }

    override fun onBindViewHolder(holder: AdapterMovimiento.ViewHolder, position: Int){
        cursor.moveToPosition(position)
        Log.d("RECYCLERVIEW", "onBindViewHolder")
        //Se asignan los valores a los elementos de la UI
        holder.titulo.text = cursor.getString(1)
        holder.fecha.text = cursor.getString(2)
        holder.movimiento.text = cursor.getString(3)
        holder.tipoCategoria.text = cursor.getString(4)
        holder.precio.text = cursor.getString(5)

        val byteArray = cursor.getBlob(6)
        println(byteArray.toString())
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        holder.img.setImageBitmap(bmp)

    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        //Se crean las referencias de la UI
        val img: ImageView
        val titulo: TextView
        val tipo: TextView
        val movimiento: TextView
        val categoria: TextView
        val tipoCategoria: TextView
        val fecha: TextView
        val lblprecio: TextView
        val precio: TextView
        val mapa: MapView

        constructor(view: View) : super(view) {
            //Se enlazan los elementos de la UI mediante ViewBinding.
            val bindingItemsRV = ListaMovimientosBinding.bind(view)
            this.img = bindingItemsRV.imgFoto
            this.titulo = bindingItemsRV.etTitulo
            this.tipo = bindingItemsRV.lblTipo
            this.movimiento = bindingItemsRV.etTipo
            this.categoria = bindingItemsRV.lblCategoria
            this.tipoCategoria = bindingItemsRV.etCategoria
            this.fecha = bindingItemsRV.etFecha
            this.lblprecio = bindingItemsRV.lblPrecio
            this.precio = bindingItemsRV.etImporte
            this.mapa = bindingItemsRV.mapLocation
        }
    }

}

