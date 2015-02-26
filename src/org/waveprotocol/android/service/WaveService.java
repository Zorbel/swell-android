package org.waveprotocol.android.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.waveprotocol.box.webclient.client.WaveWebSocketClient;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class WaveService extends Service {


  public interface WaveServiceCallback {

    public void onLogin();

    public void onError(String message);

  }




  public class WaveBinder extends Binder {

    public WaveService getService(WaveServiceCallback callback) {
      WaveService.this.mCallback = callback;
      return WaveService.this;
    }

  };

  private WaveServiceCallback mCallback;
  private WaveBinder mBinder = new WaveBinder();

  private String sessionId = null;
  private URL httpUrl;
  private String webSocketUrl;
  private String username;

  private WaveWebSocketClient webSocketClient = null;


  private class LoginTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

      String sessionId = null;
      WaveHttpLogin login = new WaveHttpLogin(params[0], params[1], params[2]);
      sessionId = login.execute();
      return sessionId;
    }

    @Override
    protected void onPostExecute(String result) {

      if (result != null) {
        sessionId = result;
        mCallback.onLogin();
      } else {
        sessionId = null;
        mCallback.onError("Wave Login Error");
      }

    }

  }

  @Override
  public void onCreate() {

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;

  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }



  //
  // Public service interface
  //

  public void login(String host, String username, String password) throws MalformedURLException {

    this.username = username;

    httpUrl = new URL(host);
    webSocketUrl = httpUrl.getProtocol() + "://" + httpUrl.getHost() + ":" + httpUrl.getPort()
        + "/atmosphere";

    // (httpUrl.getProtocol().equals("https") ? "wss://" : "ws://") +
    // httpUrl.getHost() + ":" + httpUrl.getPort() + "/atmosphere";

    new LoginTask().execute(httpUrl.toString(), username, password);

  }


  public boolean isLoggedIn() {
    return sessionId != null;
  }

  public void connect() {

    if (!isLoggedIn()) {
      return; // TODO(pablojan) handle no login in better way
    }

    webSocketClient = new WaveWebSocketClient(webSocketUrl, sessionId);
    webSocketClient.connect();

    // channel = new RemoteViewServiceMultiplexer(websocket,
    // loggedInUser.getAddress());
    // idGenerator = new IdGeneratorExtendedImpl(ClientIdGenerator.create());
    // seed = Session.get().getIdSeed();
    //
    // waveContentManager = WaveContentManager.create(waveStore,
    // waveServerDomain, idGenerator,
    // loggedInUser, seed, channel);

  }


  public void disconnect() {

  }

}
