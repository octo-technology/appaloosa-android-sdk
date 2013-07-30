package com.octo.appaloosasdk.ui.adapter.itemview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.octo.appaloosasdk.R;
import com.octo.appaloosasdk.ui.adapter.ConfigPropertyAdapter.RowType;

public class ConfigPropertyHeaderItemView implements ConfigPropertyItemView {

	private final String name;

	static class ViewHolder {
		TextView headerLabel;
	}
	
    public ConfigPropertyHeaderItemView(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
		ViewHolder holder;
        if (convertView == null) {
			holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.config_property_header, null);
            holder.headerLabel = (TextView) convertView.findViewById(R.id.config_property_list_header);
			convertView.setTag(holder);
        } else {
			holder = (ViewHolder) convertView.getTag();
        }

        holder.headerLabel.setText(name);

        return convertView;
    }

}