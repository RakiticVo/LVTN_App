package com.example.lvtn_app.View.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.NotificationAdapter;
import com.example.lvtn_app.Adapter.ProjectsAdapter;
import com.example.lvtn_app.Model.Joining_Group_Chat;
import com.example.lvtn_app.Model.Joining_Project;
import com.example.lvtn_app.Model.Joining_Request;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Project_Issue_Request;
import com.example.lvtn_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    //Khai báo
    RecyclerView recyclerViewNotification;
    NotificationAdapter notificationAdapter;
    public ArrayList<Joining_Request> joining_requests, storage;

    FirebaseUser firebaseUser;
    AppCompatActivity activity;

    static NotificationFragment instance;

    public static NotificationFragment getInstance() {
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        instance = this;

        recyclerViewNotification = view.findViewById(R.id.rcv_list_notification);

        joining_requests = new ArrayList<>();
        storage = new ArrayList<>();

        activity = (AppCompatActivity) getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        joining_requests.add(new Joining_Request(" ", " "," "," "," "," ", " "));

        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(getContext(), joining_requests);
        recyclerViewNotification.setAdapter(notificationAdapter);

        storage.clear();
        joining_requests.clear();
        notificationAdapter.notifyDataSetChanged();
//        Toast.makeText(activity, "" + recyclerViewNotification.getChildCount(), Toast.LENGTH_SHORT).show();
        getJoiningProjectID();

        return view;
    }

    public void getJoiningProjectID(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp.add(dataSnapshot.getKey());
                }
                if (temp.size() > 0){
                    getJoiningProjectRequest(temp);
                }else {
                    getJoiningGroupChatID();
                }
//                Toast.makeText(activity, "" + temp, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getJoiningProjectRequest(ArrayList<String> projectID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications")
                .child("Joining_Project");
        for (String s : projectID){
            reference.child(s).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Joining_Project joiningProject = snapshot.getValue(Joining_Project.class);
                    if (joiningProject != null){
                        saveList(new Joining_Request(
                                joiningProject.getType(),
                                joiningProject.getLeader_ID(),
                                joiningProject.getProject_ID(),
                                joiningProject.getReceiver_ID(),
                                joiningProject.getPosition(),
                                joiningProject.getStatus(),
                                joiningProject.getResult()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        getJoiningGroupChatID();
    }

    public void getJoiningGroupChatID(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp.add(dataSnapshot.getKey());
                }
                if (temp.size() > 0) {
                    getJoiningGroupChatRequest(temp);
                }else {
                    getIssueProjectID();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getJoiningGroupChatRequest(ArrayList<String> groupChatID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        for (String s : groupChatID){
            reference.child(s).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Joining_Group_Chat joiningGroupChat = snapshot.getValue(Joining_Group_Chat.class);
                    if (joiningGroupChat != null){
                        saveList(new Joining_Request(
                                joiningGroupChat.getType(),
                                joiningGroupChat.getLeader_ID(),
                                joiningGroupChat.getGroup_ID(),
                                joiningGroupChat.getReceiver_ID(),
                                joiningGroupChat.getPosition(),
                                joiningGroupChat.getStatus(),
                                joiningGroupChat.getResult()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        getIssueProjectID();
    }

    public void getIssueProjectID(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp.add(dataSnapshot.getKey());
                }
                if (temp.size() > 0){
                    getProjectIssueRequest(temp);
                }else {
                    showNotification(storage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProjectIssueRequest(ArrayList<String> projectID){
        ArrayList<Project_Issue_Request> temp = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        for (String s : projectID){
            temp.clear();
            reference.child(s).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    temp.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Project_Issue_Request projectIssueRequest = dataSnapshot.getValue(Project_Issue_Request.class);
                        if (projectIssueRequest != null){
                            temp.add(projectIssueRequest);
                        }
                        if (temp.size() == snapshot.getChildrenCount()){
                            for (Project_Issue_Request issue : temp) {
                                saveList(new Joining_Request(
                                        issue.getType(),
                                        issue.getLeader_ID(),
                                        issue.getProject_ID(),
                                        issue.getReceiver_ID(),
                                        issue.getIssue_ID(),
                                        issue.getStatus(),
                                        issue.getResult()));
                            }
                            break;
                        }
                    }
//                    Toast.makeText(activity, "" + storage.size(), Toast.LENGTH_SHORT).show();
                    showNotification(storage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void saveList(Joining_Request requests){
        storage.add(requests);
    }

    public void showNotification(ArrayList<Joining_Request> requests){
        joining_requests.clear();
//        Toast.makeText(activity, "" + requests.size(), Toast.LENGTH_SHORT).show();
        if (requests.size() > 0){
            for (Joining_Request request : requests){
                joining_requests.add(request);
                notificationAdapter.notifyDataSetChanged();
            }
        }
    }
}