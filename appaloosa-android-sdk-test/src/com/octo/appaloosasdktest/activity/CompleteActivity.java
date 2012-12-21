package com.octo.appaloosasdktest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.async.listeners.ApplicationUpdateListener;
import com.octo.appaloosasdk.exception.AppaloosaException;
import com.octo.appaloosasdktest.R;

public class CompleteActivity extends Activity {

	private static final String STORE_TOKEN = "sqozyyvdxzdxg6jioe26hr92mo3uupk1";
	private static final long STORE_ID = 1833;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setProgressBarIndeterminate(true);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setProgressBarIndeterminateVisibility(true);
		Appaloosa.getInstance().autoUpdate(this, STORE_ID, STORE_TOKEN, true, 0, 0, new ApplicationUpdateListener() {

			@Override
			public void onRequestProgressUpdate(RequestProgress progress) {
				Log.d("APPALOOSA", progress.getProgress() + "");
			}

			@Override
			public void onRequestSuccess(boolean updated) {
				setProgressBarIndeterminateVisibility(false);
			}

			@Override
			public void onRequestFailure(AppaloosaException e) {
				setProgressBarIndeterminateVisibility(false);
			}
		});
	}

	public void buttonClick(View v) {
		Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
	}

}
