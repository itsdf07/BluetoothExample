package com.itsdf07.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

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
    private BluetoothHeadset mBluetoothHeadset;
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
        registerBtReceiver();
        requestPermissions();
    }

    /**
     * 权限申请
     */
    private void requestPermissions() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            /**
             * 6.0之后监听广播：BluetoothDevice.ACTION_FOUND所需申请的权限
             * Manifest需添加权限：
             * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
             * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
             */
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

    }

    /**
     * 注册蓝牙检测广播
     */
    private void registerBtReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //注册监听蓝牙sco状态
        intentFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);

        //蓝牙开关状态的广播
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //蓝牙连接状态的广播
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);

        //蓝牙扫描的广播：开始
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //蓝牙扫描的广播：结束
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 用BroadcastReceiver来取得搜索结果
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        //蓝牙配对的广播
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);


        //蓝牙扫描状态(SCAN_MODE)发生改变
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

        registerReceiver(mBluetoothReceiver, intentFilter);
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

    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.d(TAG, "onServiceConnected：profile = " + profile);
            mBluetoothHeadset = (BluetoothHeadset) proxy;
            Log.d(TAG, "onServiceConnected：bluetoothHeadset = " + mBluetoothHeadset);
            List<BluetoothDevice> devices = mBluetoothHeadset.getConnectedDevices();
            Log.d(TAG, "onServiceConnected：设备连接数 = " + devices.size());
            for (int i = 0; i < devices.size(); i++) {
                if (devices.get(i) == null) {
                    Log.d(TAG, "onServiceConnected：设备  " + i + " 为 空");
                    continue;
                }
                String macAddress = devices.get(i).getAddress().toString();
                Log.d(TAG, "onServiceConnected：设备  " + i + " 地址为：" + macAddress);
            }
        }

        public void onServiceDisconnected(int profile) {
            Log.d(TAG, "onServiceConnected：profile = " + profile + ",mBluetoothHeadset = " + mBluetoothHeadset);
            mBluetoothAdapter.closeProfileProxy(profile, mBluetoothHeadset);
        }
    };

    /**
     * 蓝牙相关广播接收器<br>
     * 1、注册监听蓝牙sco通道状态 : AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED<br>
     * 状态key：AudioManager.EXTRA_SCO_AUDIO_STATE，状态value如下：<br>
     * {@code AudioManager.SCO_AUDIO_STATE_CONNECTED }: 连接成功<br>
     * {@code AudioManager.SCO_AUDIO_STATE_CONNECTING }: 连接中<br>
     * {@code AudioManager.SCO_AUDIO_STATE_DISCONNECTED }: 连接失败<br>
     * {@code AudioManager.SCO_AUDIO_STATE_ERROR }: 连接异常<br>
     * 2、蓝牙连接状态的广播 : BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED<br>
     * 状态key：BluetoothHeadset.EXTRA_STATE，状态value如下：<br>
     * {@code BluetoothHeadset.STATE_DISCONNECTED }: 未连接<br>
     * {@code BluetoothHeadset.STATE_CONNECTING }: 连接中<br>
     * {@code BluetoothHeadset.STATE_CONNECTED }: 连接成功<br>
     * {@code BluetoothHeadset.STATE_DISCONNECTING }:<br>
     * 3、蓝牙开关状态的广播 : BluetoothAdapter.ACTION_STATE_CHANGED
     */
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive：action = " + action);
            if (AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED.equals(action)) //蓝牙sco状态
            {
                int scoState = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, AudioManager.SCO_AUDIO_STATE_ERROR);
                Log.d(TAG, "onReceive：当前蓝牙SCO状态 = " + scoState + "," + TranslateUtils.getBluetoothScoAudioStateTip(scoState));
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))//蓝牙开关状态的广播
            {
                int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                Log.d(TAG, "onReceive：当前蓝牙开关状态 = " + bluetoothState + "," + TranslateUtils.getBluetoothStateTip(bluetoothState));
            } else if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action))//蓝牙连接状态的广播
            {
                int headsetState = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_CONNECTED);
                Log.d(TAG, "onReceive：当前蓝牙连接状态 = " + headsetState);
                switch (headsetState) {
                    case BluetoothHeadset.STATE_CONNECTED://连接成功
                        mBluetoothAdapter.getProfileProxy(getApplicationContext(), mProfileListener, BluetoothProfile.HEADSET);
                        break;
                    case BluetoothHeadset.STATE_DISCONNECTED://未连接
                        break;
                    case BluetoothHeadset.STATE_CONNECTING://连接中
                        break;
                    case BluetoothHeadset.STATE_DISCONNECTING:
                        break;
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))//蓝牙扫描的广播：开始
            {

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))//蓝牙扫描的广播：结束
            {

            } else if (BluetoothDevice.ACTION_FOUND.equals(action))//用BroadcastReceiver来取得搜索结果
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String addr = device.getAddress();  //还有其他相关的信息
                Log.d(TAG, "onReceive：deviceName = " + name + ",deviceAddress = " + addr);

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))//蓝牙配对的广播
            {

            } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action))//蓝牙扫描状态(SCAN_MODE)发生改变
            {

            }

        }
    };

}
