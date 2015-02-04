package org.waveprotocol.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WaveAndroid extends Activity implements OnClickListener {

	private EditText eUser;
	private EditText ePassword;
	private EditText eServer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button butttonLogin = (Button) findViewById(R.id.buttonLogin);
		butttonLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		eUser = (EditText) findViewById(R.id.editTextUser);
		ePassword = (EditText) findViewById(R.id.editTextPassword);
		eServer = (EditText) findViewById(R.id.editTextServer);

		String user = eUser.getText().toString();
		String pass = ePassword.getText().toString();
		String server = eServer.getText().toString();

		if (!user.isEmpty() && !pass.isEmpty() && !server.isEmpty()) {
			Intent i = new Intent(this, WaveAndroidSession.class);
			i.putExtra("user", user);
			i.putExtra("pass", pass);
			i.putExtra("server", server);
			startActivity(i);
		}

		else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setMessage("You must complete all fields");
			alertDialog.show();
		}
	}
}
