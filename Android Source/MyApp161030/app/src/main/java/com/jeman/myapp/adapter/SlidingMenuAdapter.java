package com.jeman.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeman.myapp.R;
import com.jeman.myapp.model.ItemSlideMenu;

import java.util.List;

public class SlidingMenuAdapter extends BaseAdapter{

    private Context context;
    private List<ItemSlideMenu> lstItem;

    public SlidingMenuAdapter(Context context, List<ItemSlideMenu> lstItem) {
        this.context = context;
        this.lstItem = lstItem;
    }

    private class ViewHolder {
        ImageView icon;
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.item_sliding_menu, null);
        ImageView img = (ImageView)v.findViewById(R.id.item_img);
        TextView tv = (TextView)v.findViewById(R.id.item_title);

        ItemSlideMenu item = lstItem.get(position);
        img.setImageResource(item.getImgId());
        tv.setText(item.getTitle());

        return v;
    }

    @Override
    public int getCount() { return lstItem.size(); }

    @Override
    public Object getItem(int position) { return lstItem.get(position); }

    @Override
    public long getItemId(int position) { return position; }
}
