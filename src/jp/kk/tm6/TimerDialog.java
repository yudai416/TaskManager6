package jp.kk.tm6;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class TimerDialog extends TimePickerDialog{
    public TimerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
	}

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
    }
}