package com.example.bob.mynote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bob on 2017/8/20.
 */

public class SetFragment extends Fragment {
    private static final String KEY_CONTENT = "com.example.bob.mynote.SetFragment.key_content";
    private MyReceiver myReceiver;

    public static SetFragment newInstancce(String content) {
        SetFragment fragment = new SetFragment();
        Bundle arg = new Bundle();
        arg.putString(KEY_CONTENT, content);
        fragment.setArguments(arg);
        return fragment;
    }
    private String mContent;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getArguments().getString(KEY_CONTENT);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_counter,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
