package com.finsera.util

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.finsera.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var mListener: DialogDateListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_date_picker, null)

        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker.init(year, month, day, null)

        val builder= AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.choose_date))
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                mListener?.onDialogDateSet(tag, datePicker.year, datePicker.month, datePicker.dayOfMonth)
            }
            .setNegativeButton("Cancel",null)
        val dialog = builder.create()
        return dialog
    }

    fun setDialogDateListener(listener: DialogDateListener) {
        mListener = listener
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        mListener?.onDialogDateSet(tag, year, month, day)
    }

    interface DialogDateListener {
        fun onDialogDateSet(tag: String?,  year: Int, month: Int, day: Int)
    }
}