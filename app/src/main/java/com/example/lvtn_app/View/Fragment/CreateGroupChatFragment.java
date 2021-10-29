package com.example.lvtn_app.View.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupChatFragment extends DialogFragment{
    //Khai báo
    CircleImageView avatar_group_chat;
    ImageButton ibtn_choose_avatar_group_chat;
    TextInputLayout create_group_chat_name_text_input_layout;
    Button btn_create_group_chat, btn_cancel_create_group_chat;

    SharedPreferences sharedPreferences;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference1, reference2;

    StorageReference storageReference;
    private static final int IMG_REQUEST = 1234;
    private Uri imageUri;
    private StorageTask uploadTask;

    //New group chat infomation
    String group_ID;
    String group_Name;
    String group_Image;
    String group_Creator;
    String group_LastMess;
    String group_LastSender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_create_group_chat, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        avatar_group_chat = view.findViewById(R.id.avatar_group_chat);
        ibtn_choose_avatar_group_chat = view.findViewById(R.id.ibtn_choose_avatar_group_chat);
        create_group_chat_name_text_input_layout = view.findViewById(R.id.create_group_chat_name_text_input_layout);
        btn_create_group_chat = view.findViewById(R.id.btn_create_group_chat);
        btn_cancel_create_group_chat = view.findViewById(R.id.btn_cancel_create_group_chat);

        sharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện chọn hình ảnh từ thiết bị: gọi Intent để chuyển đến thư mục hình ảnh
        ibtn_choose_avatar_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, IMG_REQUEST);
            }
        });

        //Todo: Xử lý sự kiện rời khỏi fragment
        btn_cancel_create_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Group Chat Name
        create_group_chat_name_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (create_group_chat_name_text_input_layout.getEditText().getText().toString().length() == 0){
                        create_group_chat_name_text_input_layout.setError("Please enter GroupChat's name!!!");
                        create_group_chat_name_text_input_layout.setErrorEnabled(true);
                    }else  create_group_chat_name_text_input_layout.setErrorEnabled(false);
                }else {
                    create_group_chat_name_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                create_group_chat_name_text_input_layout.setError("Please enter GroupChat's name!!!");
                                create_group_chat_name_text_input_layout.setErrorEnabled(true);
                            }else  create_group_chat_name_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện tạo một group chat:
        // - Kiểm tra rỗng cho các Text ----- (Done)
        // - Tiến hành kiểm tra đường dẫn và upload hình ảnh lên database ----- (Done)
        // - Lấy ra text và đường dẫn hình ảnh cần sử dụng ----- (Done)
        // - Gọi một instance để tiến hành tạo 1 group chat ----- (Done)
        btn_create_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (create_group_chat_name_text_input_layout.getEditText().getText().length() == 0) {
                    create_group_chat_name_text_input_layout.setError("Please enter GroupChat's name!!!");
                    create_group_chat_name_text_input_layout.setErrorEnabled(true);
                }else {
                    create_group_chat_name_text_input_layout.setErrorEnabled(false);
                    auth = FirebaseAuth.getInstance();
                    firebaseUser = auth.getCurrentUser();
                    storageReference = FirebaseStorage.getInstance().getReference("Uploads");
                    String user_ID = sharedPreferences.getString("user_ID", "token");
                    if (user_ID.equals(firebaseUser.getUid())){
                        group_Name = create_group_chat_name_text_input_layout.getEditText().getText().toString();
                        group_Creator = user_ID;
                        group_LastMess = "This group has been created";
                        group_LastSender = " ";
                        if (uploadTask != null && uploadTask.isInProgress()){
                            Toast.makeText(getContext(), "Upload into database", Toast.LENGTH_SHORT).show();
                        }else {
                            createGroupChat(group_Name, group_Creator, group_LastMess, group_LastSender);
                        }
                    }
                }
            }
        });

        return view;
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Todo: Upload Image and insert information to Database
    private void createGroupChat(String group_Name, String group_Creator, String group_LastMess, String group_LastSender){
        if (imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Creating");
            progressDialog.show();
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        reference1 = FirebaseDatabase.getInstance().getReference("GroupChats");
                        group_ID = reference1.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("group_ID", group_ID);
                        hashMap.put("group_Name", group_Name);
                        hashMap.put("group_Image", mUri);
                        hashMap.put("group_Creator", group_Creator);
                        hashMap.put("group_LastMess", group_LastMess);
                        hashMap.put("group_LastSender", group_LastSender);
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        reference1.child(group_ID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    reference2 = FirebaseDatabase.getInstance().getReference("User_List_By_Group_Chat").child(group_ID);
                                    HashMap<String, Object> hashMap1 = new HashMap<>();
                                    hashMap1.put("user_ID", group_Creator);
                                    hashMap1.put("group_ID", group_ID);
                                    hashMap1.put("position", "Leader");
                                    reference2.child(group_Creator).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                GroupChatFragment.getInstance().groupChat_list.clear();
                                                GroupChatFragment.getInstance().showGroupChatList();
                                                Toast.makeText(activity, "Create success", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                dismiss();
                                            }else {
                                                Toast.makeText(activity, "Create failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        progressDialog.dismiss();
                        dismiss();
                    }else {
                        Toast.makeText(getContext(), "Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMG_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(getContext()).load(imageUri).into(avatar_group_chat);
        }
    }
}