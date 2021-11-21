package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.IssueTypeAdapter;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.Process;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {
    Spinner spinner_issue_process;
    TextView tv_name_bar_chart, tv_name_bar_chart_2, tv_name_pie_chart,
            textView_number_1, textView_number_2, tv_working_efficiency;
    ArrayList<Issue> issue_list, toDo_list, inProgress_list, done_list, task_list, bug_list, story_list;
    ArrayList<Process> processes;
    ArrayList<ProcessType> processType_list;
    ProcessTypeAdapter processTypeAdapter;
    BarChart barChart_issues, barChart_issue_by_type;
    PieChart pieChart_working_efficiency;

    DateFormat dateFormat = new DateFormat();

    SharedPreferences sharedPreferences;
    String project_ID;

    AppCompatActivity activity;

    static StatisticFragment instance;

    public static StatisticFragment getInstance() {
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        instance = this;

        tv_name_bar_chart = view.findViewById(R.id.tv_name_bar_chart);
        tv_name_bar_chart_2 = view.findViewById(R.id.tv_name_bar_chart_2);
        tv_name_pie_chart = view.findViewById(R.id.tv_name_pie_chart);
        textView_number_1 = view.findViewById(R.id.textView_number_1);
        textView_number_2 = view.findViewById(R.id.textView_number_2);
        tv_working_efficiency = view.findViewById(R.id.tv_working_efficiency);
        spinner_issue_process = view.findViewById(R.id.spinner_issue_process);
        barChart_issues = view.findViewById(R.id.barChart_issues);
        barChart_issue_by_type = view.findViewById(R.id.barChart_issue_by_type);
        pieChart_working_efficiency = view.findViewById(R.id.pieChart_working_efficiency);

        pieChart_working_efficiency.setHighlightPerTapEnabled(true);

        barChart_issues.setEnabled(false);
        barChart_issues.setDrawValueAboveBar(true);
        barChart_issues.setPinchZoom(false);

        XAxis xAxis1 = barChart_issues.getXAxis();
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setTextSize(12f);

        YAxis yAxis_left1 = barChart_issues.getAxisLeft();
        YAxis yAxis_right1 = barChart_issues.getAxisRight();
        yAxis_left1.setTextSize(12f);
        yAxis_right1.setTextSize(12f);

        barChart_issue_by_type.setEnabled(false);
        barChart_issue_by_type.setDrawValueAboveBar(true);
        barChart_issue_by_type.setPinchZoom(false);

        XAxis xAxis2 = barChart_issue_by_type.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextSize(12f);

        YAxis yAxis_left2 = barChart_issue_by_type.getAxisLeft();
        YAxis yAxis_right2 = barChart_issue_by_type.getAxisRight();
        yAxis_left2.setTextSize(12f);
        yAxis_right2.setTextSize(12f);

        //For spinner use
        processType_list = new ArrayList<>();
        //For firebase use to get issue by process type
        issue_list = new ArrayList<>();
        toDo_list = new ArrayList<>();
        inProgress_list = new ArrayList<>();
        done_list = new ArrayList<>();
        //For save all issue by process type
        processes = new ArrayList<>();
        //For firebase use to get issue by issue type
        task_list = new ArrayList<>();
        bug_list = new ArrayList<>();
        story_list = new ArrayList<>();

        sharedPreferences = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        project_ID = sharedPreferences.getString("project_ID", "token");

        processType_list = new ArrayList<>();
        processType_list.add(new ProcessType(R.drawable.all_issues, getString(R.string.all_issues)));
        processType_list.add(new ProcessType(R.drawable.todo, getString(R.string.todo)));
        processType_list.add(new ProcessType(R.drawable.inprogress, getString(R.string.inprogress)));
        processType_list.add(new ProcessType(R.drawable.done, getString(R.string.done)));

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_issue_process.setAdapter(processTypeAdapter);

        //ToDo_Task_List
        toDo_list.add(new Issue("1", "Demo ứng dụng", " ", "Todo", "", "Task", "27/09/2021", "Medium", "Chí Thiện", "1w", "Chí Thiện", project_ID, ""));
        toDo_list.add(new Issue("2", "Viết tài liệu thiết kế"," ", "Todo", "", "Story", "15/09/2021", "High", "Thiện Võ", "1w", "Chí Thiện", project_ID, ""));

        //InProgress_Task_List
        inProgress_list.add(new Issue("1", "Thiết kế giao diện ứng dụng"," ", "InProgress", "", "Task", "12/09/2021", "Medium", "Rakitic Võ", "1w", "Chí Thiện", project_ID, ""));

        //Done_Task_List
        done_list.add(new Issue("1", "Viết mô tả ứng dụng"," ", "Done", "", "Story", "26/08/2021", "Medium", "Chí Thiện", "1w", "Chí Thiện", project_ID, ""));
        done_list.add(new Issue("2", "Viết lại mô tả ứng dụng"," ", "Done", "", "Bug", "01/09/2021", "Low", "Võ Rakitic", "1w", "Chí Thiện", project_ID, ""));
        done_list.add(new Issue("3", "Thiết kế mô hình dữ liệu"," ", "Done", "",  "Story", "09/09/2021", "High", "Thiện Võ", "1w", "Chí Thiện", project_ID, ""));

        //ToDo_List
        processes.add(new Process(1, sharedPreferences.getString("project_name_txt", "Project_name"),"ToDo", toDo_list));

        //InProgress_List
        processes.add(new Process(2, sharedPreferences.getString("project_name_txt", "Project_name"),"InProgress", inProgress_list));

        //Done_List
        processes.add(new Process(3, sharedPreferences.getString("project_name_txt", "Project_name"),"Done", done_list));

        activity = (AppCompatActivity) view.getContext();
        getIssueList();

        spinner_issue_process.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        Toast.makeText(getContext(), "" + processType_list.get(0).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "All issues: " + issue_list.size(), Toast.LENGTH_SHORT).show();
                        showBarChartByIssueType(issue_list);
                        tv_name_bar_chart.setText(R.string.all_issues);
                        break;
                    case 1:
//                        Toast.makeText(getContext(), "" + processType_list.get(1).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "Todo List: " + toDo_list.size(), Toast.LENGTH_SHORT).show();
                        showBarChartByIssueType(toDo_list);
                        tv_name_bar_chart.setText(R.string.issues_in_todo);
                        break;
                    case 2:
//                        Toast.makeText(getContext(), "" + processType_list.get(2).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "InProgress List: " + inProgress_list.size(), Toast.LENGTH_SHORT).show();
                        showBarChartByIssueType(inProgress_list);
                        tv_name_bar_chart.setText(R.string.issues_in_inprogress);
                        break;
                    case 3:
//                        Toast.makeText(getContext(), "" + processType_list.get(3).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "Done List: " + done_list.size(), Toast.LENGTH_SHORT).show();
                        showBarChartByIssueType(done_list);
                        tv_name_bar_chart.setText(R.string.issues_in_done);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return  view;
    }

    public void getIssueList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                issue_list.clear();
                toDo_list.clear();
                inProgress_list.clear();
                done_list.clear();
                task_list = new ArrayList<>();
                bug_list = new ArrayList<>();
                story_list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Issue issue = dataSnapshot.getValue(Issue.class);
                    if (issue.getIssue_project_ID().equals(project_ID)){
                        issue_list.add(issue);
                    }
                }
                if (issue_list.size() > 0){
                    barChart_issues.setVisibility(View.VISIBLE);
                    barChart_issue_by_type.setVisibility(View.VISIBLE);
                    pieChart_working_efficiency.setVisibility(View.VISIBLE);
                    textView_number_1.setVisibility(View.VISIBLE);
                    textView_number_2.setVisibility(View.VISIBLE);
                    tv_name_bar_chart.setVisibility(View.VISIBLE);
                    tv_name_bar_chart_2.setVisibility(View.VISIBLE);
                    tv_name_pie_chart.setVisibility(View.VISIBLE);
                    tv_working_efficiency.setVisibility(View.VISIBLE);
                    addIssueIntoProcess(issue_list);
                }else {
                    issue_list.clear();
                    toDo_list.clear();
                    inProgress_list.clear();
                    done_list.clear();
                    barChart_issues.setVisibility(View.GONE);
                    barChart_issue_by_type.setVisibility(View.GONE);
                    pieChart_working_efficiency.setVisibility(View.GONE);
                    textView_number_1.setVisibility(View.GONE);
                    textView_number_2.setVisibility(View.GONE);
                    tv_name_bar_chart.setVisibility(View.GONE);
                    tv_name_bar_chart_2.setVisibility(View.GONE);
                    tv_name_pie_chart.setVisibility(View.GONE);
                    tv_working_efficiency.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addIssueIntoProcess(ArrayList<Issue> issues){
        issue_list = issues;
        toDo_list.clear();
        inProgress_list.clear();
        done_list.clear();
        for (Issue issue : issues){
            switch (issue.getIssue_ProcessType().toString()){
                case "ToDo":
                    toDo_list.add(issue);
                    processes.get(0).setList(toDo_list);
//                Toast.makeText(getContext(), "Add success Todo - " + processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "InProgress":
                    inProgress_list.add(issue);
                    processes.get(1).setList(inProgress_list);
//                Toast.makeText(getContext(), "Add success InProgress - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "Done":
                    done_list.add(issue);
                    processes.get(2).setList(done_list);
//                Toast.makeText(getContext(), "Add success Done - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
            }
            if (toDo_list.size() > 0){
                for (int i = 0; i < toDo_list.size(); i++) {
                    try {
                        Date date = dateFormat.sdf.parse(toDo_list.get(i).getIssue_StartDate());
                        String s = toDo_list.get(i).getIssue_ID();
                        if (date.getTime() < Calendar.getInstance().getTime().getTime()){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID).child(s);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("issue_ProcessType", "InProgress");
                            databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        getIssueList();
                                    }
                                }
                            });
                            toDo_list.remove(i);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        showBarChartByIssueType(issues);
        showBarChartByIssueProcessType(issues);
        if (done_list.size() > 0){
            tv_name_pie_chart.setVisibility(View.VISIBLE);
            pieChart_working_efficiency.setVisibility(View.VISIBLE);
            showPieChartByIssueDoneList(done_list);
            pieChart_working_efficiency.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    PieEntry pieEntry = (PieEntry) e;
//                    Toast.makeText(activity, "" + pieEntry.getLabel(), Toast.LENGTH_SHORT).show();
                    IssueDoneProjectFragment dialog = new IssueDoneProjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", pieEntry.getLabel().toString());
                    bundle.putString("projectID", project_ID);
                    dialog.setArguments(bundle);
                    dialog.show(activity.getSupportFragmentManager(), "IssueDoneProjectFragment");
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }else {
            tv_name_pie_chart.setVisibility(View.GONE);
            pieChart_working_efficiency.setVisibility(View.GONE);
        }
    }

    public void showBarChartByIssueType(ArrayList<Issue> issues){
        task_list = new ArrayList<>();
        bug_list = new ArrayList<>();
        story_list = new ArrayList<>();
        for (Issue issue : issues){
            switch (issue.getIssue_Type().toString()){
                case "Task":
                    task_list.add(issue);
//                Toast.makeText(getContext(), "Add success Todo - " + processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "Bug":
                    bug_list.add(issue);
//                Toast.makeText(getContext(), "Add success InProgress - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "Story":
                    story_list.add(issue);
//                Toast.makeText(getContext(), "Add success Done - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        String temp1 = "Total Issue by process: " + (toDo_list.size()+inProgress_list.size()+ done_list.size());
//            String temp2 = "Total Issue by type: " + (toDo_list.size()+inProgress_list.size()+ done_list.size());

        ArrayList<BarEntry> issues1 = new ArrayList<>();
        ArrayList<BarEntry> issues2 = new ArrayList<>();
        ArrayList<BarEntry> issues3 = new ArrayList<>();
        issues1.add(new BarEntry(1, task_list.size()));
        issues2.add(new BarEntry(3, bug_list.size()));
        issues3.add(new BarEntry(5, story_list.size()));

        BarDataSet barDataSet1 = new BarDataSet(issues1, getString(R.string.task));
        barDataSet1.setHighlightEnabled(false);
        barDataSet1.setColors(Color.argb(255, 139,195,74));
        barDataSet1.setValueTextColor(Color.BLACK);
        barDataSet1.setValueTextSize(14f);
        BarDataSet barDataSet2 = new BarDataSet(issues2, getString(R.string.bug));
        barDataSet2.setHighlightEnabled(false);
        barDataSet2.setColors(Color.argb(255, 243,209,107));
        barDataSet2.setValueTextColor(Color.BLACK);
        barDataSet2.setValueTextSize(14f);
        BarDataSet barDataSet3 = new BarDataSet(issues3, getString(R.string.story));
        barDataSet3.setHighlightEnabled(false);
        barDataSet3.setColors(Color.argb(255, 105,42,117));
        barDataSet3.setValueTextColor(Color.BLACK);
        barDataSet3.setValueTextSize(14f);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        BarData barData = new BarData(dataSets);
        barChart_issues.setFitBars(true);
        barChart_issues.setData(barData);
        barChart_issues.getDescription().setText(" ");
        barChart_issues.animateY(2000);
        Legend l = barChart_issues.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(18f);
        l.setTextSize(14f);
        l.setXEntrySpace(35f);
    }

    public void showBarChartByIssueProcessType(ArrayList<Issue> issues){
        tv_name_bar_chart_2.setText(R.string.allissuesbyprocess);
        toDo_list.clear();
        inProgress_list.clear();
        done_list.clear();
        for (Issue issue : issues) {
            switch (issue.getIssue_ProcessType().toString()) {
                case "ToDo":
                    toDo_list.add(issue);
//                Toast.makeText(getContext(), "Add success Todo - " + processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "InProgress":
                    inProgress_list.add(issue);
//                Toast.makeText(getContext(), "Add success InProgress - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "Done":
                    done_list.add(issue);
//                Toast.makeText(getContext(), "Add success Done - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        ArrayList<BarEntry> issues1 = new ArrayList<>();
        ArrayList<BarEntry> issues2 = new ArrayList<>();
        ArrayList<BarEntry> issues3 = new ArrayList<>();
        issues1.add(new BarEntry(1, toDo_list.size()));
        issues2.add(new BarEntry(3, inProgress_list.size()));
        issues3.add(new BarEntry(5, done_list.size()));

        BarDataSet barDataSet1 = new BarDataSet(issues1, getString(R.string.todo));
        barDataSet1.setHighlightEnabled(false);
        barDataSet1.setColors(Color.argb(255, 139,195,74));
        barDataSet1.setValueTextColor(Color.BLACK);
        barDataSet1.setValueTextSize(14f);
        BarDataSet barDataSet2 = new BarDataSet(issues2,  getString(R.string.inprogress));
        barDataSet2.setHighlightEnabled(false);
        barDataSet2.setColors(Color.argb(255, 243,209,107));
        barDataSet2.setValueTextColor(Color.BLACK);
        barDataSet2.setValueTextSize(14f);
        BarDataSet barDataSet3 = new BarDataSet(issues3,  getString(R.string.done));
        barDataSet3.setHighlightEnabled(false);
        barDataSet3.setColors(Color.argb(255, 105,42,117));
        barDataSet3.setValueTextColor(Color.BLACK);
        barDataSet3.setValueTextSize(14f);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        BarData barData = new BarData(dataSets);
        barChart_issue_by_type.setFitBars(true);
        barChart_issue_by_type.setData(barData);
        barChart_issue_by_type.getDescription().setText(" ");
        barChart_issue_by_type.animateY(2000);
        Legend l = barChart_issue_by_type.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(18f);
        l.setTextSize(14f);
        l.setXEntrySpace(35f);
    }

    public void showPieChartByIssueDoneList(ArrayList<Issue> done_list){
        tv_name_pie_chart.setText(getString(R.string.issues_complete_perfromance));
        ArrayList<Issue> perfect_issue, delay_issue;
        perfect_issue = new ArrayList<>();
        delay_issue = new ArrayList<>();
        for (Issue issue : done_list){
            try {
                Date date1 = dateFormat.sdf.parse(issue.getIssue_EstimateFinishDate().toString());
                Date date2 = dateFormat.sdf.parse(issue.getIssue_FinishDate().toString());
                if (date2.getTime() <= date1.getTime()){
                    perfect_issue.add(issue);
                }else delay_issue.add(issue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        Toast.makeText(activity, "" + perfect_issue.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(activity, "" + delay_issue.size(), Toast.LENGTH_SHORT).show();
        ArrayList<PieEntry> issues = new ArrayList<>();
        issues.add(new PieEntry(perfect_issue.size(), getString(R.string.perfectissues)));
        issues.add(new PieEntry(delay_issue.size(), getString(R.string.delayissues)));

        PieDataSet pieDataSet = new PieDataSet(issues, " ");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(14f);

        PieData pieData = new PieData(pieDataSet);
        pieChart_working_efficiency.setData(pieData);
        pieChart_working_efficiency.getDescription().setText("");
        int x = perfect_issue.size();
        int y = delay_issue.size();
        float t = ((float)x / (float)(x+y)) * 100;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        pieChart_working_efficiency.setCenterText( decimalFormat.format(t) + "%");
        pieChart_working_efficiency.setCenterTextColor(Color.BLACK);
        pieChart_working_efficiency.setCenterTextSize(24f);
        pieChart_working_efficiency.setEntryLabelColor(Color.BLACK);
        pieChart_working_efficiency.animateY(2000);
        Legend l = pieChart_working_efficiency.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(18f);
        l.setTextSize(14f);
        l.setXEntrySpace(30f);
    }
}