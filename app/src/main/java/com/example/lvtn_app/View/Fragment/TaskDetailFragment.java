package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskDetailFragment extends DialogFragment {
    //Khai báo
    TextView tv_name_task_detail;
    ImageView img_task_type_detail;
    ImageButton ibtn_back_task_detail, ibtn_go_to_project_issue_detail, calendar_start_task_issue_detail;
    TextInputLayout description_task_detail_text_input_layout, start_date_task_detail_text_input_layout;
    Button btn_update_task_detail, btn_cancel_task_detail;

    Calendar myCalendar = Calendar.getInstance();
    DateFormat dateFormat = new DateFormat();
    Date date1 = new Date();

    AppCompatActivity activity;
    SharedPreferences sharedPreferences;

    //New task information
    String taskType = "";
    String taskName = "";
    String taskProjectID = "";
    String taskDescription = "";
    String task_start_date = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskDetailFragment newInstance(String param1, String param2) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        tv_name_task_detail = view.findViewById(R.id.tv_name_task_detail);
        img_task_type_detail = view.findViewById(R.id.img_task_type_detail);
        ibtn_back_task_detail = view.findViewById(R.id.ibtn_back_task_detail);
        ibtn_go_to_project_issue_detail = view.findViewById(R.id.ibtn_go_to_project_issue_detail);
        calendar_start_task_issue_detail = view.findViewById(R.id.calendar_start_task_issue_detail);
        btn_update_task_detail = view.findViewById(R.id.btn_update_task_detail);
        btn_cancel_task_detail = view.findViewById(R.id.btn_cancel_task_detail);

        activity = (AppCompatActivity) getContext();

        description_task_detail_text_input_layout = view.findViewById(R.id.description_task_detail_text_input_layout);
        start_date_task_detail_text_input_layout = view.findViewById(R.id.start_date_task_detail_text_input_layout);

        start_date_task_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(Calendar.getInstance().getTime()));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                start_date_task_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(myCalendar.getTime()));
                checkRightStartDate(start_date_task_detail_text_input_layout.getEditText().getText().toString());
            }
        };

        sharedPreferences = requireActivity().getSharedPreferences("Task", Context.MODE_PRIVATE);
        taskType = sharedPreferences.getString("task_type", "abc");
        taskName  = sharedPreferences.getString("task_name", "abc");
        taskProjectID  = sharedPreferences.getString("task_projectID", "abc");
        taskDescription  = sharedPreferences.getString("task_description", "abc");
        task_start_date  = sharedPreferences.getString("task_start_date", "abc");
//        Toast.makeText(getContext(), "" + taskType, Toast.LENGTH_SHORT).show();

        switch (taskType.toLowerCase()){
            case "task":
                img_task_type_detail.setImageResource(R.drawable.task);
                break;
            case "bug":
                img_task_type_detail.setImageResource(R.drawable.bug);
                break;
            case "story":
                img_task_type_detail.setImageResource(R.drawable.user_story);
                break;
        }
        tv_name_task_detail.setText(taskName);
        description_task_detail_text_input_layout.getEditText().setText(taskDescription);
        start_date_task_detail_text_input_layout.getEditText().setText(task_start_date);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi Fragment
        ibtn_back_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện rời khỏi Fragment
        btn_cancel_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày từ Calendar View
        calendar_start_task_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Issue Detail Start Date
        start_date_task_detail_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    task_start_date = start_date_task_detail_text_input_layout.getEditText().getText().toString();
//                    Toast.makeText(getContext(), "" + start_date, Toast.LENGTH_SHORT).show();
                    checkRightStartDate(task_start_date);
                }else {
                    start_date_task_detail_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                start_date_task_detail_text_input_layout.setError("Please choose day!!!");
                                start_date_task_detail_text_input_layout.setErrorEnabled(true);
                            }else{
                                start_date_task_detail_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            checkRightStartDate(s.toString());
                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện cập nhật một Task(là một type của Issue)
        // - Kiểm tra rỗng cho các text ----- (Incomplete)
        // - Lấy các text cần dùng ----- (Done)
        // - Gọi Api Service để cập nhật thông tin Task(Issue) trên database ----- (Incomplete)
        // - Gọi một Instance để cập nhật một Task ----- (Incomplete)
        btn_update_task_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: Check permission
//                Toast.makeText(getContext(), check + "\n" + temp, Toast.LENGTH_SHORT).show();
                checkRightStartDate(start_date_task_detail_text_input_layout.getEditText().getText().toString());

                if (start_date_task_detail_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error" , Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    // Todo: remember check date must be >= today
                                    taskName = tv_name_task_detail.getText().toString();
                                    taskDescription = description_task_detail_text_input_layout.getEditText().getText().toString();
                                    task_start_date = start_date_task_detail_text_input_layout.getEditText().getText().toString();

                                    Toast.makeText(activity, activity.getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(activity, activity.getString(R.string.cancel_update), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(activity.getString(R.string.save_changes_question))
                            .setPositiveButton(activity.getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(activity.getString(R.string.no), dialogClickListener).show();
                }
            }
        });

        ibtn_go_to_project_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update list with process is done and return dashboard
                if (!taskProjectID.equals("abc")){
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("project_ID", taskProjectID);
                    editor.commit();
                    dismiss();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProjectDetailFragment()).commit();
                    MainActivity.getInstance().meowBottomNavigation.show(1, true);
                }
            }
        });

        return view;
    }

    public void checkRightStartDate(String date){
        if (!dateFormat.isValidDate(date)){
            start_date_task_detail_text_input_layout.setError(activity.getString(R.string.wrongFormat2));
            start_date_task_detail_text_input_layout.setErrorEnabled(true);
        }else{
            try {
                date1 = dateFormat.sdf.parse(date);
                if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                    start_date_task_detail_text_input_layout.setError(activity.getString(R.string.wrong_start_date));
                    start_date_task_detail_text_input_layout.setErrorEnabled(true);
                }else start_date_task_detail_text_input_layout.setErrorEnabled(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}