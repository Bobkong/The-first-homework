package com.example.bob.mynote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2017/8/26.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mAccount;
    private EditText mPassWord;
    private Button mLogin;
    private TextView mRegister;
    private DB db;
    public static List<User> mUserList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DB(this);
        mAccount = (EditText)findViewById(R.id.id_account);
        mPassWord = (EditText)findViewById(R.id.id_password);
        mLogin = (Button)findViewById(R.id.id_login);
        mRegister = (TextView)findViewById(R.id.id_register);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        Cursor cr = db.getAllUsers();
        if(cr!=null&&cr.moveToFirst()) {
            do {
                mUserList.add(new User(cr.getString(cr.getColumnIndex("account")),cr.getString(cr.getColumnIndex("password"))));
            } while (cr.moveToNext());
        }
    }

    private void register() {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    private void check() {
        String account = mAccount.getText().toString();
        Log.d("account",account);
        String password = mPassWord.getText().toString();
        Log.d("password",password);
        boolean right = false;
        for(int i = 0;i < mUserList.size();i++){
            if(account.equals(mUserList.get(i).getAccount())&&password.equals(mUserList.get(i).getPassword())){
                right = true;
                startActivity(new Intent(this,MainActivity.class).putExtra("USER_ID",account));
            }
        }
        if(!right){
            Toast.makeText(this,"账号与密码不匹配",Toast.LENGTH_SHORT).show();
        }
    }
}
