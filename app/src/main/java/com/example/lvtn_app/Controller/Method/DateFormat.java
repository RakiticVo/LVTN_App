package com.example.lvtn_app.Controller.Method;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormat {
    public String myFormat = "dd/MM/yyyy"; //In which you need put here
    public SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

    public String formatDate(Date date){
        return sdf.format(date);
    }

    public boolean checkDate(Date date){
        sdf.setLenient(false);
        boolean flag = false;
        if (!sdf.format(date).equals(sdf.format(Calendar.getInstance().getTime()))){
            if (date.getTime() < Calendar.getInstance().getTime().getTime()){
                flag = false;
            }else flag = true;
        }else flag = true;
        return flag;
    }

    public Date checkFormatDate(String start_date){
        String pattern = "(0?[1-9]|[12][0-9]|3[01])\\/(0?[1-9]|1[0-2])\\/([0-9]{4})";
        sdf.setLenient(false);
        if (start_date.matches(pattern)) {
            try {
                Date date = sdf.parse(start_date);
                return date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
