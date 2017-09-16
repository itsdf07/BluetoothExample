package com.itsdf07.bluetooth;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by itsdf07 on 2017/9/16.
 */

class DeviceAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<DeviceBean> mDeviceData = new ArrayList();

    public DeviceAdapter(Context context, ArrayList<DeviceBean> data) {
        mContext = context;
        setData(data);
    }

    public void setData(ArrayList<DeviceBean> data) {
        if (mDeviceData == null) {
            mDeviceData = new ArrayList<>();
        } else {
            mDeviceData.clear();
        }
        mDeviceData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDeviceData == null ? 0 : mDeviceData.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceData == null ? null : mDeviceData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_deviceinfo, parent, false);
        }
        DeviceBean bean = mDeviceData.get(position);
        TextView deviceName = CommonViewHolder.get(convertView, R.id.tv_deviceName);
        TextView deviceMac = CommonViewHolder.get(convertView, R.id.tv_deviceMac);
        deviceName.setText(TextUtils.isEmpty(bean.getDeviceName()) ? "null" : bean.getDeviceName());
        deviceMac.setText(bean.getDeviceMac());
        return convertView;
    }
}
