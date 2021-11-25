package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Project_Issue_List;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ProjectInfoFragment extends DialogFragment {
    //Declare
    TextInputLayout edt_description_text_input_layout, edt_finish_date_text_input_layout;
    TextView tv_name_project_info, tv_leader_project_info, tv_start_date_project_info, tv_finish_date;
    ImageButton ibtn_finish_date_project_info, ibtn_delete_project_info, ibtn_back_project_info;
    Button btn_update_project_info, btn_cancel_project_info;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //Project info
    String project_ID;
    String project_Name;
    String project_Description;
    String project_FinishDate;
    String project_DateCreate;
    String project_Leader;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference1, reference2, reference3;
    AppCompatActivity activity;
    ProgressDialog progressDialog;

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

        edt_description_text_input_layout = view.findViewById(R.id.edt_description_project_info_text_input_layout);
        tv_leader_project_info = view.findViewById(R.id.tv_leader_project_info);
        edt_finish_date_text_input_layout = view.findViewById(R.id.edt_finish_date_text_input_layout);
        tv_name_project_info = view.findViewById(R.id.tv_name_project_info);
        tv_start_date_project_info = view.findViewById(R.id.tv_start_date_project_info);
        tv_finish_date = view.findViewById(R.id.tv_finish_date);
        ibtn_finish_date_project_info = view.findViewById(R.id.ibtn_finish_date_project_info);
        ibtn_delete_project_info = view.findViewById(R.id.ibtn_delete_project_info);
        ibtn_back_project_info = view.findViewById(R.id.ibtn_back_project_info);
        btn_update_project_info = view.findViewById(R.id.btn_update_project_info);
        btn_cancel_project_info = view.findViewById(R.id.btn_cancel_project_info);

        sharedPreferences = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        activity = (AppCompatActivity) getContext();
        progressDialog = new ProgressDialog(getContext());
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        project_ID = sharedPreferences.getString("project_ID", "token");
        getProjectInfo(project_ID);

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

        //Event Handling
        ibtn_back_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_cancel_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_description_text_input_layout.getEditText().setText(" ");
                edt_finish_date_text_input_layout.getEditText().setText(dateFormat.formatDate(Calendar.getInstance().getTime()));
            }
        });

        ibtn_delete_project_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo delete from DB
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
//                                Toast.makeText(getContext(), "Todo: Delete Project", Toast.LENGTH_SHORT).show();
                                deleteProjectAll(project_ID);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
//                                Toast.makeText(getContext(), "Cancel delete!!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(activity.getString(R.string.delete_project_question))
                        .setPositiveButton(activity.getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(activity.getString(R.string.no), dialogClickListener).show();
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
                if (edt_finish_date_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error", Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    project_Description = edt_description_text_input_layout.getEditText().getText().toString();
                                    if (project_Description.length() == 0){
                                        project_Description = " ";
                                    }
                                    project_FinishDate = edt_finish_date_text_input_layout.getEditText().getText().toString();
                                    reference2 =  FirebaseDatabase.getInstance().getReference("Projects").child(project_ID);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("project_Description", project_Description);
                                    hashMap.put("project_FinishDate", project_FinishDate);
                                    reference2.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(activity, activity.getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                                                getProjectInfo(project_ID);
                                            }
                                        }
                                    });
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(activity, activity.getString(R.string.cancel_update), Toast.LENGTH_SHORT).show();
                                    getProjectInfo(project_ID);
                                    break;
                            }
                            edt_description_text_input_layout.getEditText().clearFocus();
                            edt_finish_date_text_input_layout.getEditText().clearFocus();
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(activity.getString(R.string.save_changes_question))
                            .setPositiveButton(activity.getString(R.string.yes), dialogClickListener)
                            .setNegativeButton(activity.getString(R.string.no), dialogClickListener).show();
                }
            }
        });

        return view;
    }
    //Delete Project
    public void deleteProjectAll(String project_ID){
        progressDialog.setMessage(activity.getString(R.string.deleting));
        progressDialog.show();
        deleteIssueListByUser(project_ID);
    }

    public void deleteIssueListByUser(String project_ID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issue_List_By_User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User_Issue_List list = dataSnapshot.getValue(User_Issue_List.class);
                    if (list.getProject_ID().equals(project_ID)){
//                        Toast.makeText(activity, "" + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Issue_List_By_User").child(dataSnapshot.getKey());
                        databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
//                                    Toast.makeText(activity, "Delete issue list by user success", Toast.LENGTH_SHORT).show();
                                    deleteIssueListByProject(project_ID);
                                }else {
//                                    Toast.makeText(activity, "Delete issue list by user success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(activity, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        deleteIssueListByProject(project_ID);
    }

    public void deleteIssueListByProject(String project_ID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issue_List_By_Project").child(project_ID);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(activity, "Delete issue list by project success", Toast.LENGTH_SHORT).show();
                    deleteIssues(project_ID);
                }else {
                    Toast.makeText(activity, "Delete issue list by project failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteIssues(String project_ID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(activity, "Delete issues success", Toast.LENGTH_SHORT).show();
                    deleteUserListByProject(project_ID);
                }else {
//                    Toast.makeText(activity, "Delete issues by project failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteUserListByProject(String project_ID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(project_ID);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(activity, "Delete user list by project success", Toast.LENGTH_SHORT).show();
                    deleteProject(project_ID);
                } else {
//                    Toast.makeText(activity, "Delete user list by project failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteProject(String project_ID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Projects").child(project_ID);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(activity, "Delete project success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    dismiss();
                    getFragmentManager().beginTransaction().replace(R.id.frame_main, new ProjectsFragment()).commit();
                }else {
//                    Toast.makeText(activity, "Delete project failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Get Project Infomation
    public void getProjectInfo(String project_ID){
        reference1 = FirebaseDatabase.getInstance().getReference("Projects").child(project_ID);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                if (project != null){
                    project_Name = project.getProject_Name();
                    project_Description = project.getProject_Description();
                    project_DateCreate = project.getProject_DateCreate();
                    project_FinishDate = project.getProject_FinishDate();

                    tv_name_project_info.setText(project_Name);
                    edt_description_text_input_layout.getEditText().setText(project_Description);
                    tv_start_date_project_info.setText(project_DateCreate);
                    edt_finish_date_text_input_layout.getEditText().setText(project_FinishDate);

                    reference3 = FirebaseDatabase.getInstance().getReference("Users").child(project.getProject_Leader());
                    reference3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            project_Leader = user.getUser_Name();
                            tv_leader_project_info.setText(project_Leader);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if (!project.getProject_Leader().equals(firebaseUser.getUid())){
                        ibtn_delete_project_info.setVisibility(View.GONE);
                        edt_description_text_input_layout.getEditText().setEnabled(false);
                        edt_description_text_input_layout.getEditText().setBackgroundResource(R.drawable.custom_edt);
                        edt_finish_date_text_input_layout.getEditText().setEnabled(false);
                        edt_finish_date_text_input_layout.getEditText().setBackgroundResource(R.drawable.custom_edt);
                        ibtn_finish_date_project_info.setVisibility(View.GONE);
                        btn_update_project_info.setVisibility(View.GONE);
                        btn_cancel_project_info.setVisibility(View.GONE);
                    }else {
//                    Toast.makeText(activity, "Leader is here", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}