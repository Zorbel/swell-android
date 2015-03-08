package org.waveprotocol.android;

import java.net.MalformedURLException;

import org.waveprotocol.android.service.WaveService;
import org.waveprotocol.android.service.WaveService.WaveServiceCallback;
import org.waveprotocol.wave.model.document.WaveContext;
import org.waveprotocol.wave.model.wave.ObservableWavelet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WaveAndroid extends Activity implements ServiceConnection,
    WaveServiceCallback {

  private EditText eUser;
  private EditText ePassword;
  private EditText eServer;

  private Intent mWaveServiceIntent;
  private WaveService mWaveService;

  Button buttonLogin;
  Button buttonSend;

  ObservableWavelet mWavelet;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    buttonLogin = (Button) findViewById(R.id.buttonLogin);
    buttonLogin.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        eUser = (EditText) findViewById(R.id.editTextUser);
        ePassword = (EditText) findViewById(R.id.editTextPassword);
        eServer = (EditText) findViewById(R.id.editTextServer);

        String user = eUser.getText().toString();
        String pass = ePassword.getText().toString();
        String server = eServer.getText().toString();

        if (!user.isEmpty() && !pass.isEmpty() && !server.isEmpty()) {

          try {
            mWaveService.login(server, user, pass);
          } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }

        else {
          AlertDialog alertDialog = new AlertDialog.Builder(WaveAndroid.this).create();
          alertDialog.setMessage("You must complete all fields");
          alertDialog.show();
        }

      }
    });

    buttonSend = (Button) findViewById(R.id.buttonSend);
    buttonSend.setEnabled(false);

    buttonSend.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {

        mWavelet.getBlip("myBlip").getContent().insertText(0, "LA-LA-LA ");

      }

    });

    // Bind to Wave Service
    Intent mWaveServiceIntent = new Intent(this, WaveService.class);
    bindService(mWaveServiceIntent, this, Context.BIND_AUTO_CREATE);

  }




  @Override
  protected void onDestroy() {
    stopService(mWaveServiceIntent);
  }



  // Wave Service Binding Callbacks

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    mWaveService = ((WaveService.WaveBinder) service).getService(this);
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    mWaveService = null;
  }

  // Wave Service Operational Callbacks

  @Override
  public void onLogin() {
    Toast.makeText(this, "Login success. Connecting to Wave Server", Toast.LENGTH_LONG).show();
    buttonLogin.setEnabled(false);
    mWaveService.connect();
  }

  @Override
  public void onError(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();

  }

  @Override
  public void onConnect() {
    Toast.makeText(this, "Connected to Wave Server", Toast.LENGTH_LONG).show();

    WaveContext wave = mWaveService.createWave(mWaveService.getIdGenerator().newWaveId()
        .serialise());

    mWavelet = wave.getWave().getRoot();
    mWavelet.createBlip("myBlip").getContent().insertText(0, "LO-LO-LO ");

    buttonSend.setEnabled(true);
  }

  @Override
  public void onReconnect() {
    Toast.makeText(this, "Reconnected to Wave Server", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDisconnect() {
    Toast.makeText(this, "Disconnected to Wave Server", Toast.LENGTH_SHORT).show();
  }

}
