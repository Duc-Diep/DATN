package com.ducdiep.bookmarket.utils


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ducdiep.bookmarket.R
import com.ducdiep.bookmarket.extensions.clearTime
import java.util.*

class DateTimePickerDialog : DialogFragment() {
    companion object {
        const val ARGS_TIME = "args_time"
        fun getInstance(): DateTimePickerDialog {
            return getInstance(Calendar.getInstance().clearTime())
        }

        fun getInstance(calendar: Calendar): DateTimePickerDialog {
            val dialog = DateTimePickerDialog()
            val args = Bundle()
            args.putSerializable(ARGS_TIME, calendar)
            dialog.arguments = args
            return dialog
        }
    }

    private var callbacks: ((Calendar) -> Unit)? = null
    private var currentDate: Calendar = Calendar.getInstance().clearTime()

    fun setOnDateChange(action: (Calendar) -> Unit) {
        callbacks = action
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments != null) {
            currentDate = requireArguments().getSerializable(ARGS_TIME) as Calendar
        }
        val year = currentDate[Calendar.YEAR]
        val month = currentDate[Calendar.MONTH]
        val day = currentDate[Calendar.DAY_OF_MONTH]

        requireActivity().resources.configuration.setLocale(Locale("vi"))
        Locale.setDefault(Locale("vi"))

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            R.style.ThemeOverlay_AppCompat_Dialog_Alert,
            null,
            year,
            month,
            day
        )
        datePickerDialog.apply {
            setButton(
                DialogInterface.BUTTON_NEGATIVE,
                context.getString(R.string.cancel)
            ) { _, _ -> datePickerDialog.dismiss() }
            setButton(
                DialogInterface.BUTTON_POSITIVE,
                context.getString(R.string.ok)
            ) { _, _ ->
                val picker = this.datePicker
                currentDate = convertYMDToCalendar(picker.year, picker.month, picker.dayOfMonth)
                callbacks?.invoke(currentDate)
            }
            return datePickerDialog
        }
    }

    private fun convertYMDToCalendar(year: Int, month: Int, day: Int): Calendar {
        val calendar = Calendar.getInstance()
        calendar[year, month, day, 0, 0] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar
    }
}