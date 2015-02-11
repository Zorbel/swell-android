package org.waveprotocol.android;

import org.waveprotocol.android.client.WaveAndroidClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

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
  }

  private void getUserData() {
    Bundle extras = getIntent().getExtras();
    this.user = extras.getString("user");
    this.password = extras.getString("pass");
    this.server = extras.getString("server");
  }

}
