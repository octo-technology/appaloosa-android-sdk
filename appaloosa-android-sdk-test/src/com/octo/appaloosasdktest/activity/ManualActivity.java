package com.octo.appaloosasdktest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.appaloosasdk.Appaloosa;
import com.octo.appaloosasdk.async.listeners.ApplicationUpToDateListener;
import com.octo.appaloosasdk.async.listeners.ApplicationUpdateListener;
import com.octo.appaloosasdk.exception.AppaloosaException;
import com.octo.appaloosasdktest.R;

public class ManualActivity extends Activity {

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
		Appaloosa.getInstance().checkForUpdate(this, STORE_ID, STORE_TOKEN, new ApplicationUpToDateListener() {

			@Override
			public void onRequestSuccess(boolean isUpToDate, final long appId) {
				if (isUpToDate == false) {
					// need to update (do what you want : show a dialog or not, ..)
					Appaloosa.getInstance().downloadAndInstallApplication(ManualActivity.this, STORE_ID, STORE_TOKEN, appId, new ApplicationUpdateListener() {

						@Override
						public void onRequestProgressUpdate(RequestProgress progress) {
							// show a progress if necessary
							Log.d("APPALOOSA", progress.getProgress() + "");
						}

						@Override
						public void onRequestSuccess(boolean arg0) {
							// install OK
						}

						@Override
						public void onRequestFailure(AppaloosaException arg0) {
							// install failed
						}
					});
				}
			}

			@Override
			public void onRequestFailure(AppaloosaException e) {
				// ...
			}
		});
	}

	public void buttonClick(View v) {
		Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
	}

}
