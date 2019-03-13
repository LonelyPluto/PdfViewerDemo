package com.lonelypluto.pdfviewerdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lonelypluto.pdfviewerdemo.activity.BasePDFActivity;
import com.lonelypluto.pdfviewerdemo.activity.MuPDFActivity;
import com.lonelypluto.pdfviewerdemo.activity.MoreSetActivity;
import com.lonelypluto.pdfviewerdemo.activity.SignActivity;
import com.lonelypluto.pdfviewerdemo.adapter.MainAdapter;
import com.lonelypluto.pdfviewerdemo.entry.MainBean;
import com.lonelypluto.pdfviewerdemo.widget.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101;

    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private List<MainBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        initData();
        initView();
        setListener();
    }

    /**
     * 初始化数据
     */
    private void initData(){
        list = new ArrayList<>();
        List<String> list_title = Arrays.asList(getResources().getStringArray(R.array.main_title));
        List<String> list_describe = Arrays.asList(getResources().getStringArray(R.array.main_describe));
        for (int i = 0; i < list_title.size(); i++) {
            MainBean bean = new MainBean();
            bean.setTitle(list_title.get(i));
            bean.setDescribe(list_describe.get(i));
            list.add(bean);
        }
    }

    /**
     * 初始化布局
     */
    private void initView(){
        adapter = new MainAdapter(this, list);
        recyclerView = (RecyclerView)findViewById(R.id.main_rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//HORIZONTAL 水平
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置点击事件
     */
    private void setListener(){
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                switch (vh.getLayoutPosition()) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, BasePDFActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, MuPDFActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, MoreSetActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, SignActivity.class));
                        break;
                }

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                java.util.Map<String, Integer> perms = new HashMap<>();
                // Initial
//                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION and WRITE_EXTERNAL_STORAGE
//                Boolean location = perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                Boolean storage = perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    // All Permissions Granted
                    Toast.makeText(this, "允许存储权限", Toast.LENGTH_SHORT).show();

                } else { // !location && !storage case
                    // Permission Denied
                    Toast.makeText(this, "存储权限被拒绝", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    private void checkPermissions() {
        List<String> permissions = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {// else: We already have permissions, so handle as normal

        }
    }
}
