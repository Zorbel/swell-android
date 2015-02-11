package org.waveprotocol.android;

import org.waveprotocol.android.client.WaveAndroidClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gwt.core.client.Callback;

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

      Thread loginThread = new Thread(new LoginRunnable(user, pass, server));
      loginThread.start();

    }

    else {
      AlertDialog alertDialog = new AlertDialog.Builder(this).create();
      alertDialog.setMessage("You must complete all fields");
      alertDialog.show();
    }
  }

  private class LoginRunnable implements Runnable {

    final String user, password, server;

    public LoginRunnable(String user, String password, String server) {
      this.user = user;
      this.password = password;
      this.server = server;
    }

    @Override
    public void run() {

      final WaveAndroidClient waveClient = new WaveAndroidClient();

      waveClient.startSession(user, password, server, new Callback<String, String>() {

        @Override
        public void onSuccess(String result) {

          WaveAndroid.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

              Intent i = new Intent(WaveAndroid.this, WaveAndroidSession.class);
              i.putExtra("user", user);
              i.putExtra("pass", password);
              i.putExtra("server", server);
              startActivity(i);

            }
          });

        }

        @Override
        public void onFailure(final String reason) {

          WaveAndroid.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

              Toast
.makeText(WaveAndroid.this.getApplicationContext(),
                  "Failed Wave server connection: " + reason, Toast.LENGTH_LONG)
                  .show();

            }
          });

        }

      });

    }
  }


}
