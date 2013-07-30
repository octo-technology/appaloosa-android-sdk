package com.octo.appaloosasdk.ui.adapter.itemview;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.octo.appaloosasdk.R;
import com.octo.appaloosasdk.model.ConfigProperty;
import com.octo.appaloosasdk.ui.adapter.ConfigPropertyAdapter.RowType;

public class ConfigPropertyDataItemView implements ConfigPropertyItemView {
	
	private final ConfigProperty configProperty;

	static class ViewHolder {
		TextView dataLabel;
		TextView dataValue;
	}
	
    public ConfigPropertyDataItemView(ConfigProperty configProperty) {
        this.configProperty = configProperty;
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.config_property_item, null);
			holder.dataLabel = (TextView) convertView.findViewById(R.id.item_property_name);
			holder.dataValue = (TextView) convertView.findViewById(R.id.item_property_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (configProperty == null) {
			holder.dataLabel.setText("");
			holder.dataValue.setText("None");
			holder.dataValue.setTypeface(null, Typeface.ITALIC);
		} else {
			holder.dataLabel.setText(configProperty.getLabel());
			holder.dataValue.setText(configProperty.getValue());
			holder.dataValue.setTypeface(null, Typeface.NORMAL);
		}
		
		return convertView;
	}

}