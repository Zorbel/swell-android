package org.waveprotocol.android;

import org.waveprotocol.android.client.WaveAndroidClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gwt.core.client.Callback;

public class WaveAndroid extends Activity implements OnClickListener {

	private WaveAndroidClient waveClient;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		waveClient = new WaveAndroidClient();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button butttonLogin = (Button) findViewById(R.id.buttonLogin);
		butttonLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		EditText eUser = (EditText) findViewById(R.id.editTextUser);
		EditText ePassword = (EditText) findViewById(R.id.editTextPassword);
		EditText eServer = (EditText) findViewById(R.id.editTextServer);

		String user = eUser.getText().toString();
		String pass = ePassword.getText().toString();
		String server = eServer.getText().toString();
		
		final Context context = getApplicationContext();

		if (!user.isEmpty() && !pass.isEmpty() && !server.isEmpty()) {
			
			waveClient.startSession(user, pass, server,
					new Callback<String, String>() {

						@Override
						public void onSuccess(String result) {

							Toast.makeText(context, "Connected to Wave server with session Id: " + result, Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(String reason) {

							Toast.makeText(context,
									"Failed Wave server connection: " + reason, Toast.LENGTH_LONG).show();
						}
					});
		}
		
		else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setMessage("You must complete all fields");
			alertDialog.show();
		}
	}
}
