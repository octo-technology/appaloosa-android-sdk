package com.octo.appaloosasdk.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.octo.appaloosasdk.ui.adapter.itemview.ConfigPropertyItemView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ConfigPropertyAdapter extends ArrayAdapter<ConfigPropertyItemView> {

	private List<ConfigPropertyItemView> items = new ArrayList<ConfigPropertyItemView>();
	private LayoutInflater inflater;
	
	public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }
	
	public ConfigPropertyAdapter(Context context, List<ConfigPropertyItemView> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

	public ConfigPropertyAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }
	
	public List<ConfigPropertyItemView> getItems() { return items; }
	public void setItems(List<ConfigPropertyItemView> items) { this.items = items; }
	public void addItems(List<ConfigPropertyItemView> items) { this.items.addAll(items); }
	
	@Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(inflater, convertView);
    }

}
