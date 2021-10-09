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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.AssigneeAdapter;
import com.example.lvtn_app.Adapter.IssueTypeAdapter;
import com.example.lvtn_app.Adapter.PriorityAdapter;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.Model.Priority;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueDetailFragment extends DialogFragment {
    //Khai báo
    Spinner spinner_process_issue_detail, spinner_issue_type_detail, spinner_priority_issue_detail, spinner_assignee_issue_detail;
    ImageView img_issue_type_detail;
    TextView tv_name_issue_detail, tv_issue_finish_date;
    ImageButton ibtn_back_issue_detail, ibtn_confirm_done_issue, calendar_start_date_issue_detail;
    TextInputLayout description_issue_detail_text_input_layout, start_date_issue_detail_text_input_layout, estimate_time_issue_detail_text_input_layout;
    Button btn_update_issue_detail, btn_cancel_issue_detail;

    //Array List for Spinner
    ArrayList<IssueType> issueType_list;
    ArrayList<ProcessType> processType_list;
    ArrayList<Priority> priority_list;
    ArrayList<Member> member_list;

    //Adapter for Spinner
    IssueTypeAdapter issueTypeAdapter;
    ProcessTypeAdapter processTypeAdapter;
    PriorityAdapter priorityAdapter;
    AssigneeAdapter assigneeAdapter;

    //Date formate
    final Calendar myCalendar = Calendar.getInstance();
    DateFormat dateFormat = new DateFormat();
    Date date1 = new Date();

    //SharedPreferences
    SharedPreferences sharedPreferences;

    //New issue information
    String issuename = "";
    String process_type = "";
    String decription = "";
    String issue_type = "";
    String startdate = "";
    String priority = "";
    String assignee = "";
    String estimatetime = "";
    String creator = "";
    String finishdate = "";
    int project_id = -1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IssueDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssueDetailFragment newInstance(String param1, String param2) {
        IssueDetailFragment fragment = new IssueDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_issue_detail, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        spinner_process_issue_detail = view.findViewById(R.id.spinner_process_issue_detail);
        spinner_issue_type_detail = view.findViewById(R.id.spinner_issue_type_detail);
        spinner_priority_issue_detail = view.findViewById(R.id.spinner_priority_issue_detail);
        spinner_assignee_issue_detail = view.findViewById(R.id.spinner_assignee_issue_detail);

        img_issue_type_detail = view.findViewById(R.id.img_issue_type_detail);
        tv_name_issue_detail = view.findViewById(R.id.tv_name_issue_detail);
        tv_issue_finish_date = view.findViewById(R.id.tv_issue_finish_date);
        ibtn_back_issue_detail = view.findViewById(R.id.ibtn_back_issue_detail);
        ibtn_confirm_done_issue = view.findViewById(R.id.ibtn_confirm_done_issue);
        calendar_start_date_issue_detail = view.findViewById(R.id.calendar_start_date_issue_detail);

        description_issue_detail_text_input_layout = view.findViewById(R.id.description_issue_detail_text_input_layout);
        start_date_issue_detail_text_input_layout = view.findViewById(R.id.start_date_issue_detail_text_input_layout);
        estimate_time_issue_detail_text_input_layout = view.findViewById(R.id.estimate_time_issue_detail_text_input_layout);

        btn_update_issue_detail = view.findViewById(R.id.btn_update_issue_detail);
        btn_cancel_issue_detail = view.findViewById(R.id.btn_cancel_issue_detail);

        issueType_list = new ArrayList<>();
        issueType_list.add(new IssueType(R.drawable.task, "Task"));
        issueType_list.add(new IssueType(R.drawable.bug, "Bug"));
        issueType_list.add(new IssueType(R.drawable.user_story, "Story"));

        processType_list = new ArrayList<>();
        processType_list.add(new ProcessType(R.drawable.todo, "ToDo"));
        processType_list.add(new ProcessType(R.drawable.inprogress, "InProgress"));
        processType_list.add(new ProcessType(R.drawable.done, "Done"));

        priority_list = new ArrayList<>();
        priority_list.add(new Priority(R.drawable.high, "High"));
        priority_list.add(new Priority(R.drawable.medium, "Medium"));
        priority_list.add(new Priority(R.drawable.low, "Low"));

        member_list = new ArrayList<>();
        member_list.add(new Member(1, "Chí Thiện", ""));
        member_list.add(new Member(1, "Thiện Võ", "https://progameguides.com/wp-content/uploads/2021/07/Genshin-Impact-Character-Raiden-Shogun-1.jpg"));
        member_list.add(new Member(1, "Rakitic Võ", ""));

        issueTypeAdapter = new IssueTypeAdapter(getContext(), issueType_list);
        spinner_issue_type_detail.setAdapter(issueTypeAdapter);

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_process_issue_detail.setAdapter(processTypeAdapter);

        priorityAdapter = new PriorityAdapter(getContext(), priority_list);
        spinner_priority_issue_detail.setAdapter(priorityAdapter);

        assigneeAdapter = new AssigneeAdapter(getContext(), member_list);
        spinner_assignee_issue_detail.setAdapter(assigneeAdapter);

        start_date_issue_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(Calendar.getInstance().getTime()));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                start_date_issue_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(myCalendar.getTime()));
                checkRightStartDate(start_date_issue_detail_text_input_layout.getEditText().getText().toString());
            }
        };

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi Fragment
        ibtn_back_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện rời khỏi Fragment
        btn_cancel_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện cập nhật một Issue
        // - Kiểm tra rỗng cho các text ----- (Incomplete)
        // - Lấy các text cần dùng ----- (Done)
        // - Gọi Api Service để cập nhật thông tin Issue trên database ----- (Incomplete)
        // - Gọi một Instance để cập nhật một Issue ----- (Incomplete)
        btn_update_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: Check permission


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                // Todo: remember check date must be >= today
                                creator = sharedPreferences.getString("userName_txt", "User Name");
                                issuename = tv_name_issue_detail.getText().toString();
                                decription = description_issue_detail_text_input_layout.getEditText().getText().toString();
                                startdate = start_date_issue_detail_text_input_layout.getEditText().getText().toString();
                                estimatetime = estimate_time_issue_detail_text_input_layout.getEditText().getText().toString();
                                finishdate = tv_issue_finish_date.getText().toString();

                                Toast.makeText(getContext(), "" + creator + "\n"
                                        + issuename + "\n"
                                        + process_type + "\n"
                                        + decription + "\n"
                                        + issue_type + "\n"
                                        + startdate + "\n"
                                        + priority + "\n"
                                        + assignee + "\n"
                                        + estimatetime + "\n"
                                        + finishdate, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Update success!!!", Toast.LENGTH_SHORT).show();
                                dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(getContext(), "Update error!!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to save change?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        //Todo: Xử lý sự kiện hoàn thành một Issue
        // - Gọi Api Service để xác nhận hoàn thành một Issue trên database ----- (Incomplete)
        // - Gọi một Instance để xác nhận hoàn thành một Issue ----- (Incomplete)
        ibtn_confirm_done_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update list with process is done and return dashboard
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Toast.makeText(getContext(), "This task is Done!!!", Toast.LENGTH_SHORT).show();
                                dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Would you like to confirm this issue is complete?").setPositiveButton("Confirm", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày từ Calendar View
        calendar_start_date_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Issue Detail Estimate Time
        estimate_time_issue_detail_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    checkRightEstimateTime(estimate_time_issue_detail_text_input_layout.getEditText().getText().toString());
                }
            }
        });
        estimate_time_issue_detail_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    estimate_time_issue_detail_text_input_layout.setError("Wrong time!!!");
                    estimate_time_issue_detail_text_input_layout.setErrorEnabled(true);
                } else {
                    estimate_time_issue_detail_text_input_layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkRightEstimateTime(s.toString());
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Issue Detail Start Date
        start_date_issue_detail_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    startdate = start_date_issue_detail_text_input_layout.getEditText().getText().toString();
//                    Toast.makeText(getContext(), "" + start_date, Toast.LENGTH_SHORT).show();
                    checkRightStartDate(startdate);
                }else {
                    start_date_issue_detail_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                start_date_issue_detail_text_input_layout.setError("Please choose day!!!");
                                start_date_issue_detail_text_input_layout.setErrorEnabled(true);
                            }else{
                                start_date_issue_detail_text_input_layout.setErrorEnabled(false);
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

        //Todo: Xử lý sự kiện lấy Issue Detail Issue Type và hiện hình ảnh theo Issue Type
        spinner_issue_type_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + issueType_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                img_issue_type_detail.setImageResource(issueType_list.get(position).getImage());
                issue_type = issueType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Process Type
        spinner_process_issue_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + processType_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                process_type = processType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Priority
        spinner_priority_issue_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + priority_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                priority = priority_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Assignee
        spinner_assignee_issue_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + user_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                assignee = member_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void checkRightStartDate(String date){
        if (!dateFormat.isValidDate(date)){
            start_date_issue_detail_text_input_layout.setError("Wrong format. Ex: dd/MM/yyy");
            start_date_issue_detail_text_input_layout.setErrorEnabled(true);
        }else{
            try {
                date1 = dateFormat.sdf.parse(date);
                if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                    start_date_issue_detail_text_input_layout.setError("Wrong start day!!!");
                    start_date_issue_detail_text_input_layout.setErrorEnabled(true);
                }else start_date_issue_detail_text_input_layout.setErrorEnabled(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkRightEstimateTime(String date){
        String last = date.substring(date.length() - 1);
        if (date.length() >= 0 && date.length() < 2 || (!last.equals("d") && !last.equals("m") && !last.equals("w"))) {
            estimate_time_issue_detail_text_input_layout.setError("Wrong format!!! Ex: 1d, 2w, 3m");
            estimate_time_issue_detail_text_input_layout.setErrorEnabled(true);
        } else {
            estimate_time_issue_detail_text_input_layout.setErrorEnabled(false);
        }
    }
}