package com.octo.appaloosasdktest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdktest.R;

public class SimpleActivity extends Activity {

	private static final String STORE_TOKEN = "sqozyyvdxzdxg6jioe26hr92mo3uupk1";
	private static final long STORE_ID = 1833;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	@Override
	protected void onStart() {
		super.onStart();

		Appaloosa.getInstance().autoUpdate(this, STORE_ID, STORE_TOKEN);
	}

	public void buttonClick(View v) {
		Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
	}
}
