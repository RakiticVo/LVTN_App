package com.example.lvtn_app.View.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
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

    //New group chat infomation
    String creator = "";
    String group_chat_name = "";
    String avatar_group = "";

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

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện chọn hình ảnh từ thiết bị: gọi Intent để chuyển đến thư mục hình ảnh
        ibtn_choose_avatar_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
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
                    creator = sharedPreferences.getString("userName_txt", "User Name");
                    group_chat_name = create_group_chat_name_text_input_layout.getEditText().getText().toString();

                    File file = new File(avatar_group.toString());
                    String file_path = file.getAbsolutePath();
//                    Toast.makeText(getContext(), "" + file_path, Toast.LENGTH_SHORT).show();
                    if (file_path.length() > 1){
                        String[] array_file_name = file_path.split("\\.");
                        if (!array_file_name[1].equals("png") || !array_file_name[1].equals("jpg")){
                            array_file_name[1] = "png";
                        }
                        file_path = array_file_name[0] + System.currentTimeMillis() + "." + array_file_name[1];
//                        Toast.makeText(getContext(), "" + file_path, Toast.LENGTH_SHORT).show();
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("upload_file", file_path, requestFile);
                        ApiService uploadImage = ApiUtils.connectRetrofit();
                        uploadImage.isUploadUserImageSuccess(body).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response != null){
                                    String message = response.body();
                                    avatar_group = ApiUtils.baseUrl + "image/" + message;
                                    GroupChatFragment.getInstance().createGroupChat(group_chat_name, avatar_group, creator);
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
//                                Toast.makeText(getDialog().getWindow().getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("BBB", "onFailure: " + t.getMessage() );
                            }
                        });
                    }else {
                        GroupChatFragment.getInstance().createGroupChat(group_chat_name, null, creator);
                    }
                    dismiss();
                }
            }
        });

        return view;
    }

    //Todo: Phương thức xử lý hình ảnh và hiển thị
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri chosenImageUri = data.getData();
            avatar_group = getRealPathFromURI(chosenImageUri);
            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), chosenImageUri);
                avatar_group_chat.setImageBitmap(mBitmap);
//                Toast.makeText(getContext(), "" + avatar_path, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Todo: Phương thức upload hình ảnh lên database
    public void updateUserInformation(int user_id, String gender, String phone, String dob, String address,String avatar){
        ApiService updateUserInfo = ApiUtils.connectRetrofit();
        updateUserInfo.isUpdateUserInformationSuccess(user_id, gender, phone, dob, address, avatar).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getContext(), "Create " + response.body().toLowerCase(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "" + call + "\n" + t, Toast.LENGTH_LONG).show();
                Log.e("TAG", "onFailure: " + call + "\n" + t);
            }
        });
    }

    //Todo: Phương thức lấy địa chỉ thật của hình ảnh trong thiết bị
    public String getRealPathFromURI(Uri uri) {
        String path = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            path = cursor.getString(columnIndex);
        }
        cursor.close();
        return path;
    }
}