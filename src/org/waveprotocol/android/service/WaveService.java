package org.waveprotocol.android.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.waveprotocol.android.service.WaveWebSocketClient.ConnectionListener;
import org.waveprotocol.box.webclient.client.SimpleWaveStore;
import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.wave.model.document.WaveContext;
import org.waveprotocol.wave.model.extended.id.IdGeneratorExtended;
import org.waveprotocol.wave.model.extended.id.IdGeneratorExtendedImpl;
import org.waveprotocol.wave.model.id.IdGeneratorImpl;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.InvalidParticipantAddress;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.waveref.WaveRef;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class WaveService extends Service {


  public interface WaveServiceCallback {

    public void onLogin();

    public void onConnect();

    public void onReconnect();

    public void onDisconnect();

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


  private String webSocketUrl; // TODO dont' build socket url in this class

  private URL serverUrl;
  private String waveDomain;

  private String sessionId = null;
  private ParticipantId participantId = null;

  private WaveWebSocketClient webSocketClient = null;
  private RemoteViewServiceMultiplexer channel;

  private String seed;
  private IdGeneratorExtended idGenerator;

  private WaveStore waveStore;
  private Map<WaveRef, WaveLoader> loaderStore;


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
        waveDomain = participantId.getDomain();

        mCallback.onLogin();

      } else {

        sessionId = null;
        participantId = null;
        waveDomain = null;

        mCallback.onError("Wave Login Error");

      }

    }

  }


  @Override
  public void onCreate() {

    loaderStore = new HashMap<WaveRef, WaveLoader>();

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;

  }

  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  public IdGeneratorExtended getIdGenerator() {
    return idGenerator;
  }

  //
  // Public service interface
  //

  public void login(String host, String username, String password) throws MalformedURLException {


    serverUrl = new URL(host);
    // TODO move URL build to WaveSocketWasync class
    webSocketUrl = serverUrl.getProtocol() + "://" + serverUrl.getHost() + ":"
        + serverUrl.getPort()
        + "/atmosphere";

    try {
      participantId = ParticipantId.of(username);
    } catch (InvalidParticipantAddress e) {

      mCallback.onError(e.getMessage());
      return;
    }

    new LoginTask().execute(serverUrl.toString(), participantId.toString(), password);

  }


  public boolean isLoggedIn() {
    return sessionId != null && participantId != null;
  }

  public void connect() {

    if (!isLoggedIn()) {
      return; // TODO(pablojan) handle no login in better way
    }

    seed = sessionId; // TODO double-check seed usage
    waveStore = new SimpleWaveStore();

    idGenerator = new IdGeneratorExtendedImpl(new IdGeneratorImpl(participantId.getDomain(),
        new IdGeneratorImpl.Seed() {

          @Override
          public String get() {
            return seed;
          }

        }));

    webSocketClient = new WaveWebSocketClient(webSocketUrl, sessionId);
    webSocketClient.connect(new ConnectionListener() {

      @Override
      public void onDisconnect() {
        mCallback.onDisconnect();
      }

      @Override
      public void onConnect() {
        mCallback.onConnect();
      }

      @Override
      public void onReconnect() {
        mCallback.onReconnect();
      }
    });

    channel = new RemoteViewServiceMultiplexer(webSocketClient, participantId.getAddress());




  }


  public WaveContext openWave(String waveId) {

    WaveRef waveRef = WaveRef.of(WaveId.deserialise(waveId));

    WaveLoader loader = WaveLoader.create(false, waveRef, channel, participantId,
        Collections.<ParticipantId> emptySet(), idGenerator, null);
    WaveContext wave = loader.getWaveContext();

    if (wave != null) {
      waveStore.add(wave);
      loaderStore.put(waveRef, loader);
    }

    return wave;
  }

  public WaveContext createWave(String waveId) {

    WaveRef waveRef = WaveRef.of(WaveId.deserialise(waveId));

    WaveLoader loader = WaveLoader.create(true, waveRef, channel, participantId,
        Collections.<ParticipantId> emptySet(), idGenerator, null);
    WaveContext wave = loader.getWaveContext();

    if (wave != null) {
      waveStore.add(wave);
      loaderStore.put(waveRef, loader);
    }

    return wave;
  }


  public void closeWave(String waveId) {

    WaveRef waveRef = WaveRef.of(WaveId.deserialise(waveId));

    WaveLoader loader = loaderStore.get(waveRef);

    if (loader == null)
      return;

    loader.close();

    waveStore.remove(loader.getWaveContext());
    loaderStore.remove(waveRef);
  }

  public void disconnect() {

    waveStore = null;
    idGenerator = null;

    channel = null;
    webSocketClient = null;
    seed = null;

  }

  public void logout() {
    sessionId = null;
    participantId = null;
    waveDomain = null;
  }

}
