package edu.jordivicent.mywonderfulmoneycontrol.Utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

//DatePicker extiende de DialogFragment y implementa el DatePickerDialog
class DatePickerFragment(val listener: (day:Int, month:Int, year:Int) -> Unit): DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    //Devuelve dia/mes/año
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //listener ejecuta la funcion onDateSelected() en activity_Movimiento
        listener(dayOfMonth, month, year)
    }

    //Se Inicializa el datePicker con la fecha actual
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val month: Int = c.get(Calendar.MONTH)
        val year: Int = c.get(Calendar.YEAR)

        return DatePickerDialog(activity as Context, this, year, month, day)
    }

}