package com.example.bob.mynote;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Bob on 2017/6/1.
 */
public class EditFragment extends Fragment {


    private static final String KEY_CONTENT = "com.example.bob.mynote.EditFragment.key_content";

    public static EditFragment newInstancce(String content,String account){
        EditFragment fragment = new EditFragment();
        Bundle arg = new Bundle();
        arg.putString(KEY_CONTENT, content);
        arg.putString("USER_ID",account);
        fragment.setArguments(arg);
        return fragment;
    }
    private String mAccount;
    private String mContent;
    private EditText mEdit;
    private String mNote;
    private String mTime;
    private DB db;
    private int id;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getArguments().getString(KEY_CONTENT);
        mAccount = getArguments().getString("USER_ID");
        db = new DB(getActivity());
        SharedPreferences sp = getActivity().getSharedPreferences("SP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int check = sp.getInt("ID_KEY",-1);
        if(check == -1){
            editor.putInt("ID_KEY", 0);
            id = 0;
        }
       else{
            id = check;
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container,false);

       // mTextView = (TextView)view.findViewById(R.id.txt_content);
        //mTextView.setText(mContent);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        this.getActivity().findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }


    void add(){
       mTime = getTime();
        mEdit = (EditText)this.getActivity().findViewById(R.id.id_note);
        mNote = mEdit.getText().toString();
        if(mNote != "") {
            mEdit.setText("");
            ContentValues values = new ContentValues();
            Data data = new Data(id, mNote, mTime,mAccount);
            SharedPreferences sp = getActivity().getSharedPreferences("SP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("ID_KEY", id);
            editor.commit();
            MyFragment.mData.add(data);
            db.insert(id, mNote, mTime,mAccount);
            id++;
        }
    }
    public static String getTime(){
        String Time;
        int mMonth,mDay,mHour,mMinute;
        Calendar c = Calendar.getInstance();//
        mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        mHour = c.get(Calendar.HOUR_OF_DAY);//时
        mMinute = c.get(Calendar.MINUTE);//分
        String hour,minute;
        if(mHour<10)hour = "0"+mHour;
        else hour = "" + mHour;
        if(mMinute<10)minute = "0"+mMinute;
        else minute = "" + mMinute;
        Time = mMonth + "月" + mDay + "日"  + " " + hour + ":" + minute;
        return Time;
    }



}
