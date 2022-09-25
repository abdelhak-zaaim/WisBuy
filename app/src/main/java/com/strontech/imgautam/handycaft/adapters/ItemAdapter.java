package com.strontech.imgautam.handycaft.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.strontech.imgautam.handycaft.R;

/* loaded from: classes5.dex */
public class ItemAdapter extends BaseAdapter {
    int[] bankIcons;
    String[] bankNames;
    Context context;

    public ItemAdapter(Context context, String[] bankNames, int[] bankIcons) {
        this.context = context;
        this.bankNames = bankNames;
        this.bankIcons = bankIcons;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.bankNames.length;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView2 = LayoutInflater.from(this.context).inflate(R.layout.item_adapter, parent, false);
        ((ImageView) convertView2.findViewById(R.id.ImageIcon)).setImageResource(this.bankIcons[position]);
        ((TextView) convertView2.findViewById(R.id.tv)).setText(this.bankNames[position]);
        return convertView2;
    }
}
