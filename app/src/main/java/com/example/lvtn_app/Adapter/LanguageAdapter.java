package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.R;

import java.util.ArrayList;

public class LanguageAdapter extends BaseAdapter {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<String> languages;

    public LanguageAdapter(Context context, ArrayList<String> languages) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.languages = languages;
    }


    @Override
    public int getCount() {
        if (languages.size() > 0){
            return languages.size();
        }else return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Khai báo
        convertView = mInflater.inflate(R.layout.item_language, null);

        ImageView imageview_language = convertView.findViewById(R.id.imageview_language);
        TextView textview_language = convertView.findViewById(R.id.textview_language);

        String language = languages.get(position).toString();
        textview_language.setText(language);
        switch (language){
            case "en":
                imageview_language.setImageResource(R.drawable.united_states);
                break;
            case "vi":
                imageview_language.setImageResource(R.drawable.vietnam);
                break;
        }

        return convertView;
    }
}
