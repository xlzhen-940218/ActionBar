package com.xlzhen.actionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar=(ActionBar)findViewById(R.id.actionBar);

        actionBar.setLinearLayout(Color.parseColor("#3F51B5"), new int[]{R.mipmap.ic_apps_white_48dp , R.mipmap.ic_more_vert_white_48dp}, new ActionBar.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int resImg) {
                switch (resImg) {
                    case R.mipmap.ic_more_vert_white_48dp://查看更多
                        Toast.makeText(MainActivity.this, "您点击了查看更多键", Toast.LENGTH_SHORT).show();
                        break;
                    case R.mipmap.ic_apps_white_48dp://打开其他APP
                        Toast.makeText(MainActivity.this, "您点击了打开其他APP键", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        actionBar.setNoticeView(Color.parseColor("#303F9F"));
        actionBar.setLeftButton(R.mipmap.ic_menu_white_36dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "您点击了菜单键", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
