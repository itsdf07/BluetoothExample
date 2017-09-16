package com.itsdf07.bluetoot;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Action值                                                说明
 * ACTION_STATE_CHANGED                    蓝牙状态值发生改变
 * ACTION_SCAN_MODE_CHANGED                蓝牙扫描状态(SCAN_MODE)发生改变
 * ACTION_DISCOVERY_STARTED                蓝牙扫描过程开始
 * ACTION_DISCOVERY_FINISHED               蓝牙扫描过程结束
 * ACTION_LOCAL_NAME_CHANGED               蓝牙设备Name发生改变
 * ACTION_REQUEST_DISCOVERABLE             请求用户选择是否使该蓝牙能被扫描
 * <p>
 * PS：如果蓝牙没有开启，用户点击确定后，会首先开启蓝牙，继而设置蓝牙能被扫描。
 * ACTION_REQUEST_ENABLE                   请求用户选择是否打开蓝牙
 * ACTION_FOUND  (该常量字段位于BluetoothDevice类中，稍后讲到)
 * 说明：蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播。
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "dfsu-bluetooth";
    private BluetoothAdapter mBluetoothAdapter;
    /**
     * 蓝牙的状态
     */
    private boolean mBlueToothState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBlueTooth();
    }

    private void initBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void onOpenBlueTooth(View view) {
        mBlueToothState = mBluetoothAdapter.isEnabled();
        if (mBlueToothState) {
            Toast.makeText(this, "蓝牙已经打开", Toast.LENGTH_SHORT).show();
        } else {
            mBluetoothAdapter.enable();
            Toast.makeText(this, "正在打开..", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCloseBlueTooth(View view) {
        mBlueToothState = mBluetoothAdapter.isEnabled();
        if (mBlueToothState) {
            mBluetoothAdapter.disable();
            Toast.makeText(this, "正在关闭.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "蓝牙已经关闭!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 通过广播调用系统的activity来打开蓝牙
     *
     * @param view
     */
    public void onOpenBlueTooth2Broadcast(View view) {
        mBlueToothState = mBluetoothAdapter.isEnabled();
        if (mBlueToothState) {
            Toast.makeText(this, "蓝牙已经打开", Toast.LENGTH_SHORT).show();
        } else {
            //通过广播调用系统的activity来打开蓝牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        }

    }
}
