package com.example.bob.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Bob on 2017/8/26.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText mAccount;
    private EditText mPassWord;
    private DB db;
    private Button mRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DB(this);
        mAccount = (EditText)findViewById(R.id.id_account);
        mPassWord = (EditText)findViewById(R.id.id_password);
        mRegister = (Button)findViewById(R.id.id_register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registe();
            }
        });
    }

    private void registe() {
        String account = mAccount.getText().toString();
        String password = mPassWord.getText().toString();
        long isExisted = db.ifExisted(account);
        if(isExisted == 0){
            db.insertUser(account,password);
            LoginActivity.mUserList.add(new User(account,password));
            Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        }
        else{
            Toast.makeText(this,"账号已存在",Toast.LENGTH_SHORT).show();
        }
    }
}
