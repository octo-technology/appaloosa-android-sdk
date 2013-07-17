package com.octo.appaloosasdk.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.octo.appaloosasdk.R;
import com.octo.appaloosasdk.model.ConfigProperty;
import com.octo.appaloosasdk.ui.adapter.ConfigPropertyAdapter;
import com.octo.appaloosasdk.ui.adapter.itemview.ConfigPropertyItemView;
import com.octo.appaloosasdk.ui.adapter.itemview.ConfigPropertyDataItemView;
import com.octo.appaloosasdk.ui.adapter.itemview.ConfigPropertyHeaderItemView;
import com.octo.appaloosasdk.utils.SystemUtils;

public class AppaloosaDevPanelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appaloosa_dev_panel);
		ListView phonePropertiesListView = (ListView) findViewById(R.id.phone_properties_listview);

		ConfigPropertyAdapter adapter = new ConfigPropertyAdapter(this, generateConfigPropertyList());

		phonePropertiesListView.setAdapter(adapter);
	}

	private List<ConfigPropertyItemView> generateConfigPropertyList() {
		
		List<ConfigPropertyItemView> configItems = new ArrayList<ConfigPropertyItemView>();

		PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(),
	        		PackageManager.GET_PERMISSIONS
	        		| PackageManager.GET_SIGNATURES
	        		| PackageManager.GET_CONFIGURATIONS
	        		| PackageManager.GET_META_DATA);

		} catch (NameNotFoundException e) {
	        Log.w("AppaloosaDevPanelActivity", "echec de la recuperation des meta data");
	        e.printStackTrace();
	    }
		
		configItems.add(new ConfigPropertyHeaderItemView("System"));
		this.fillPropertyList(configItems, SystemUtils.getGeneralConfigPropertyList(this), false);
		
		configItems.add(new ConfigPropertyHeaderItemView("Screen"));
		this.fillPropertyList(configItems, SystemUtils.getDisplayMetricsDataList(this), false);

		configItems.add(new ConfigPropertyHeaderItemView("General package info"));
		this.fillPropertyList(configItems, SystemUtils.getPackageInfoDataList(packageInfo), false);
		
		configItems.add(new ConfigPropertyHeaderItemView("Requested permissions"));
		this.fillPropertyList(configItems, SystemUtils.getRequestedPermissionsDataList(packageInfo), true);
		
		configItems.add(new ConfigPropertyHeaderItemView("Permissions"));
		this.fillPropertyList(configItems, SystemUtils.getPermissionsDataList(packageInfo), true);
		
		configItems.add(new ConfigPropertyHeaderItemView("Signatures"));
		this.fillPropertyList(configItems, SystemUtils.getSignaturesDataList(packageInfo), true);
		
		configItems.add(new ConfigPropertyHeaderItemView("Services"));
		this.fillPropertyList(configItems, SystemUtils.getServicesDataList(packageInfo), true);
		
		return configItems;
	}
	
	private void fillPropertyList(List<ConfigPropertyItemView> configItems, List<ConfigProperty> items, boolean hideLabel) {
		if (items.size() == 0) {
			configItems.add(new ConfigPropertyDataItemView(null));
			return ;
		}
		
		for (ConfigProperty item : items) {
			if (hideLabel) {
				item.setLabel(item.getValue());
				item.setValue("");
			}
			configItems.add(new ConfigPropertyDataItemView(item));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.appaloosa_dev_panel, menu);
		return true;
	}

}
