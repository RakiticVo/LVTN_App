package com.example.lvtn_app.View.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupChatFragment extends DialogFragment{
    //Khai báo
    CircleImageView avatar_group_chat;
    ImageButton ibtn_choose_avatar_group_chat;
    TextInputLayout create_group_chat_name_text_input_layout;
    Button btn_create_group_chat, btn_cancel_create_group_chat;
    SharedPreferences sharedPreferences;
    public Bitmap temp;
    public Uri chosenImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Khai báo
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
        ibtn_choose_avatar_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        btn_create_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                if (create_group_chat_name_text_input_layout.getEditText().getText().length() == 0) {
                    create_group_chat_name_text_input_layout.setError("Enter GroupChat's name");
                    create_group_chat_name_text_input_layout.setErrorEnabled(true);
                }else {
                    String username = sharedPreferences.getString("username_txt", "User Name");
                    String groupchatname = create_group_chat_name_text_input_layout.getEditText().getText().toString();
                    GroupChatFragment.getInstance().createGroupChat(groupchatname, chosenImageUri, username);
                    dismiss();
                }
            }
        });

        btn_cancel_create_group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), chosenImageUri);
                avatar_group_chat.setImageBitmap(mBitmap);
                temp = mBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}