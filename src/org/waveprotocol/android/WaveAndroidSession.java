package org.waveprotocol.android;

import org.waveprotocol.android.client.WaveAndroidClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gwt.core.client.Callback;

public class WaveAndroidSession extends Activity {

	private String user;
	private String password;
	private String server;
	private WaveAndroidClient waveClient;

	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.session);
		this.getUserData();
		
		this.context = getApplicationContext();

		waveClient = new WaveAndroidClient();
		waveClient.startSession(user, password, server,
				new Callback<String, String>() {

					@Override
					public void onSuccess(String result) {

						Toast.makeText(context, "Connected to Wave server with session Id: "
										+ result, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onFailure(String reason) {
						Toast.makeText(context, "Failed Wave server connection: " + reason,
								Toast.LENGTH_LONG).show();
					}
				});
	}

	private void getUserData() {
		Bundle extras = getIntent().getExtras();
		this.user = extras.getString("user");
		this.password = extras.getString("pass");
		this.server = extras.getString("server");
	}

}
