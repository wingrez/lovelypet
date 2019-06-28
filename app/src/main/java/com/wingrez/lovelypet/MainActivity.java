package com.wingrez.lovelypet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_openFW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_openFW = this.<Button>findViewById(R.id.btn_openFW);
    }

    /**
     * Activity返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, FWService.class));
            }
        }
    }

    /**
     * 按钮点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_openFW: //开启悬浮窗
                startFWService(view);
                break;
            case R.id.btn_closeFW: //关闭悬浮窗
                stopFWService(view);
                break;
        }
    }

    /**
     * 开启悬浮窗服务
     *
     * @param view
     */
    private void startFWService(View view) {
        if (FWService.isFWRunning == true) { //检查悬浮窗开启状态，只允许开启一个悬浮窗
            Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //需要判断系统版本是否大于Android 6.0
            if (!Settings.canDrawOverlays(this)) { //因为版本号大于6.0的系统才可以调用此方法，未授权状态
                Toast.makeText(this, "未授权开启悬浮窗", Toast.LENGTH_SHORT);
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0); //请求授权
            } else { //已授权状态
                Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT);
                startService(new Intent(MainActivity.this, FWService.class));
            }
        } else { //版本号小于6.0，直接开启悬浮窗，无需授权
            Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT);
            startService(new Intent(MainActivity.this, FWService.class));
        }
    }

    /**
     * 关闭悬浮窗服务
     *
     * @param view
     */
    private void stopFWService(View view) {
        stopService(new Intent(MainActivity.this, FWService.class));
    }

}
