package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ProjectInfoFragment extends DialogFragment {
    //Declare
    TextInputLayout edt_decription_text_input_layout, edt_estimate_time_text_input_layout, edt_finish_date_text_input_layout;
    TextView tv_name_project_info, tv_start_date_project_info, tv_finish_date;
    ImageButton ibtn_finish_date_project_info, ibtn_delete_project_info, ibtn_back_project_info;
    Button btn_update_project_info;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();
    Date date1 = new Date();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_project_info, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        edt_decription_text_input_layout = view.findViewById(R.id.edt_decription_text_input_layout);
        edt_estimate_time_text_input_layout = view.findViewById(R.id.edt_estimate_time_text_input_layout);
        edt_finish_date_text_input_layout = view.findViewById(R.id.edt_finish_date_text_input_layout);
        tv_name_project_info = view.findViewById(R.id.tv_name_project_info);
        tv_start_date_project_info = view.findViewById(R.id.tv_start_date_project_info);
        tv_finish_date = view.findViewById(R.id.tv_finish_date);
        ibtn_finish_date_project_info = view.findViewById(R.id.ibtn_finish_date_project_info);
        ibtn_delete_project_info = view.findViewById(R.id.ibtn_delete_project_info);
        ibtn_back_project_info = view.findViewById(R.id.ibtn_back_project_info);
        btn_update_project_info = view.findViewById(R.id.btn_update_project_info);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edt_finish_date_text_input_layout.getEditText().setText(dateFormat.formatDate(myCalendar.getTime()));
                if (dateFormat.isValidDate(edt_finish_date_text_input_layout.getEditText().getText().toString())){
                    try {
                        date1 = dateFormat.sdf.parse(edt_finish_date_text_input_layout.getEditText().getText().toString());
                        if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                            edt_finish_date_text_input_layout.setError("Wrong day!!!");
                            edt_finish_date_text_input_layout.setErrorEnabled(true);
                        }else edt_finish_date_text_input_layout.setErrorEnabled(false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //Set data
        tv_name_project_info.setText(sharedPreferences.getString("project_name_txt", "Project Name"));

        String description = sharedPreferences.getString("project_decription_txt", "");
        if (!description.equals("")){
            edt_decription_text_input_layout.getEditText().setText(description);
        }else {
            edt_decription_text_input_layout.getEditText().setText("");
        }

        String start_date = sharedPreferences.getString("project_start_date_txt", "");
        tv_start_date_project_info.setText(start_date);

        String estimate_time = sharedPreferences.getString("project_estimate_time_txt", "");
        if (!estimate_time.equals("")){
            edt_estimate_time_text_input_layout.getEditText().setText(estimate_time);
        }else {
            edt_estimate_time_text_input_layout.getEditText().setText("");
        }

        String finish_date = sharedPreferences.getString("project_finish_date_txt", "");
        if (!finish_date.equals("")){
            edt_finish_date_text_input_layout.getEditText().setText(finish_date);
        }else {
            tv_finish_date.setText(R.string.estimate_finish_date_2);
            edt_finish_date_text_input_layout.getEditText().setText("");
        }

        //Event Handling
        ibtn_back_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ibtn_delete_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo delete from DB
                Toast.makeText(getContext(), "Todo: Delete Project", Toast.LENGTH_SHORT).show();
                dismiss();
                getFragmentManager().popBackStack();
            }
        });

        ibtn_finish_date_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edt_estimate_time_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String last = edt_estimate_time_text_input_layout.getEditText().getText().toString();
                    if (last.equals("")){
                        edt_estimate_time_text_input_layout.setError("Please enter estimate time!!!");
                        edt_estimate_time_text_input_layout.setErrorEnabled(true);
                    }else {
                        last = last.substring(last.length() - 1);
                        if (!last.equals("d") && !last.equals("m") && !last.equals("w") && !last.equals("y")) {
                            edt_estimate_time_text_input_layout.setError("Wrong format!!! Ex: 1d, 2w, 3m, 4y");
                            edt_estimate_time_text_input_layout.setErrorEnabled(true);
                        } else {
                            edt_estimate_time_text_input_layout.setErrorEnabled(false);
                        }
                    }
                }else{
                    edt_estimate_time_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String last = edt_estimate_time_text_input_layout.getEditText().getText().toString();
                            last = last.substring(last.length() - 1);
                            if (s.length() >= 0 && s.length() < 2 || (!last.equals("d") && !last.equals("m") && !last.equals("w") && !last.equals("y"))) {
                                edt_estimate_time_text_input_layout.setError("Ex: 1d, 2w, 3m, 4y");
                                edt_estimate_time_text_input_layout.setErrorEnabled(true);
                            } else {
                                edt_estimate_time_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        edt_finish_date_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (dateFormat.isValidDate(edt_finish_date_text_input_layout.getEditText().getText().toString())){
                        try {
                            date1 = dateFormat.sdf.parse(edt_finish_date_text_input_layout.getEditText().getText().toString());
                            if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                                edt_finish_date_text_input_layout.setError("Wrong day!!!");
                                edt_finish_date_text_input_layout.setErrorEnabled(true);
                            }else edt_finish_date_text_input_layout.setErrorEnabled(false);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btn_update_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_estimate_time_text_input_layout.isErrorEnabled() || edt_finish_date_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error", Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    String project_name = sharedPreferences.getString("project_name_txt", "Project Name");
                                    String decription = edt_decription_text_input_layout.getEditText().getText().toString();
                                    String estimatetime = edt_estimate_time_text_input_layout.getEditText().getText().toString();
                                    String finishdate = edt_finish_date_text_input_layout.getEditText().getText().toString();

                                    Toast.makeText(getContext(), "" + project_name + "\n"
                                            + decription + "\n"
                                            + estimatetime + "\n"
                                            + finishdate, Toast.LENGTH_SHORT).show();

                                    editor.putString("project_decription_txt", decription);
                                    editor.putString("project_estimate_time_txt", estimatetime);
                                    editor.putString("project_finish_date_txt", finishdate);
                                    editor.commit();

                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Create error!!!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to save changes?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        return view;
    }
}