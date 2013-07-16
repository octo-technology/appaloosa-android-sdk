package com.octo.appaloosasdktest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdktest.R;

public class DevPanelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void buttonClick(View v) {
		Toast.makeText(this, "Display dev panel", Toast.LENGTH_SHORT).show();
		Appaloosa.getInstance().displayDevPanel(this);
	}

}
