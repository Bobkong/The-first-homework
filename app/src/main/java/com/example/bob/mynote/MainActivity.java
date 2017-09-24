package com.example.bob.mynote;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private String mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        mAccount = getIntent().getStringExtra("USER_ID");
        new BottomNavigationBarBuilder(mViewPager, getSupportFragmentManager())
                .addItem(findViewById(R.id.id_edit), EditFragment.newInstancce("aaaa",mAccount))
                .addItem(findViewById(R.id.id_my), MyFragment.newInstancce("aaaa",mAccount))
                .addItem(findViewById(R.id.id_set), SetFragment.newInstancce("aaaa"))
                .build();
    }

    private void bindView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

}
