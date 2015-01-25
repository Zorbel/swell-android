package org.waveprotocol.android.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.waveprotocol.box.webclient.client.ClientIdGenerator;
import org.waveprotocol.box.webclient.client.RemoteViewServiceMultiplexer;
import org.waveprotocol.box.webclient.client.Session;
import org.waveprotocol.box.webclient.client.SimpleWaveStore;
import org.waveprotocol.box.webclient.client.WaveWebSocketClient;
import org.waveprotocol.box.webclient.search.SearchBuilder;
import org.waveprotocol.box.webclient.search.SearchService;
import org.waveprotocol.box.webclient.search.WaveStore;
import org.waveprotocol.wave.client.extended.WaveContentManager;
import org.waveprotocol.wave.client.extended.WaveContentWrapper;
import org.waveprotocol.wave.concurrencycontrol.common.UnsavedDataListener;
import org.waveprotocol.wave.model.extended.WaveType;
import org.waveprotocol.wave.model.extended.id.IdGeneratorExtended;
import org.waveprotocol.wave.model.extended.id.IdGeneratorExtendedImpl;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.schema.SchemaProvider;
import org.waveprotocol.wave.model.schema.conversation.ConversationSchemas;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.waveref.WaveRef;

import android.util.Log;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;


public class WaveAndroidClient {

  private static final Logger log = Logger.getLogger(WaveAndroidClient.class.getName());


  public class UnsavedDataListenerProxy implements UnsavedDataListener {

    private UnsavedDataListener actualListener = null;

    public void setActualUnsavedDataListener(UnsavedDataListener actualListener) {
      this.actualListener = actualListener;
    }

    @Override
    public void onUpdate(UnsavedDataInfo unsavedDataInfo) {
      if (actualListener != null)
        this.actualListener.onUpdate(unsavedDataInfo);

    }

    @Override
    public void onClose(boolean everythingCommitted) {
      if (actualListener != null)
        this.actualListener.onClose(everythingCommitted);
    }

  }


  private final static String SESSION_COOKIE_NAME = "WSESSIONID";

  /* Components depending on the user session */
  private String seed;
  private ParticipantId loggedInUser = null;
  private RemoteViewServiceMultiplexer channel;
  private IdGeneratorExtended idGenerator;

  /* Components shared across sessions */
  private final SchemaProvider schemaProvider;

  private final WaveStore waveStore;


  private String waveServerDomain;
  private String waveServerURLSchema;
  private String waveServerURL;
  private WaveWebSocketClient websocket;
  private SearchBuilder searchBuilder;

  private WaveContentManager waveContentManager = null;
  private Map<String, WaveContentWrapper> activeWaveMap = null;




  /**
   * Default constructor
   */
  public WaveAndroidClient() {

    this.activeWaveMap = new HashMap<String, WaveContentWrapper>();
    this.schemaProvider = new ConversationSchemas();
    this.waveStore = new SimpleWaveStore();

  }

  /**
   * Performs a login against Wave's /auth servlet. This method doesn't start a
   * web socket session. Wave server will set a session cookie.
   *
   * CORS and XHR is taken into account:
   * http://stackoverflow.com/questions/10977058
   * /xmlhttprequest-and-set-cookie-cookie
   *
   * @param user name of the user without domain part
   * @param callback
   */
  private void login(final String user, final String password,
      final Callback<String, String> callback) {

	  final ParticipantId participantId = ParticipantId.ofUnsafe(user + "@" + this.waveServerDomain);

	    
	    String url = "http://wave.p2pvalue.eu/auth/signin?r=none"; //Stablishing the url of the wave server. 
	    //"r=none" -> parameter that indicates the server not to send a blank html page as a response to the request
	    
	    
	    // Using the Apache HTTP library included in the Android Default API (org.apache.http)
	    HttpClient clienthttp = new DefaultHttpClient(); //Http client in charge of handling the http comunication
	    
	    HttpPost post = new HttpPost(url); //The post that will be sent to the wave server
	      
	    
	    try {
	    	
	    	 // Allow cookie headers, and so Wave session can be set
//	      builder.setIncludeCredentials(true);
	    		
	    	post.addHeader("Content-Type", "application/x-www-form-urlencoded");
	    	
	    	//Include the required login information in the post header
	    	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	    	nameValuePair.add(new BasicNameValuePair("address", user + "@prototype.p2pvalue.eu")); 
	    	nameValuePair.add(new BasicNameValuePair("password", password));
	    	nameValuePair.add(new BasicNameValuePair("signIn", "Sign+in"));
	    	post.setEntity(new UrlEncodedFormEntity(nameValuePair));
	    	   	
	    	// Execute HTTP Post Request
	        ResponseHandler<String> responseHandler=new BasicResponseHandler();
	        
	        String response = clienthttp.execute(post, new BasicResponseHandler() {
	        	@Override
	        	public String handleResponse(HttpResponse response)
	        			throws HttpResponseException, IOException {
	        		
	        		// TODO fix Wave auth server to do not make redirects
//	              // xmlHTTTPResquest object doesn't handle 302 properly
	        		
	        		//See the actual response in the log (in html format) 
	                //Log.d("HTTP RESPONSE", response); 
	        		
	        		if (response.getStatusLine().getStatusCode() == 200) { //Code 200 means that the connection was successful
	        			
	        			loggedInUser = participantId;
	        			
//	                  // This fakes the former Wave Client JS session object.
//	                  String sessionId = Cookies.getCookie(SESSION_COOKIE_NAME);
	      //
////	                  createWebClientSession(WaveAndroidClient.this.waveServerDomain, participantId.getAddress(),
////	                      sessionId);
	      //
//	                  callback.onSuccess(sessionId);
	        			
	        			Log.d("HTTP SUCCESS", "Wave login succesfull for: " + user);
	        			Log.d("HTTP SUCCESS", "Wave user info: " + response.getStatusLine().getReasonPhrase());
	       			
	        			callback.onSuccess(null);
	        			 
	        		} else { //Code != 200 means that the connection was wrong
	        			
	        			loggedInUser = null;
	        			
	        			Log.d("HTTP ERROR", "Error calling wave login servlet: " + response.getStatusLine().getReasonPhrase());
	        			
	        			callback.onFailure("Error calling wave login servlet: " + response.getStatusLine().getReasonPhrase());
	        		}
	        		
	        		return super.handleResponse(response);
	        	}
	        }); 

	    } catch (ClientProtocolException e) {
	    	
	    	Log.w("HTTP ERROR", "Error calling wave login servlet: protocol");
	    	callback.onFailure(e.getMessage());
	    	
		} catch (IOException e) {
			
			Log.w("HTTP ERROR", "Error calling wave login servlet: IO");
	    	callback.onFailure(e.getMessage());
		}

	  }

// This JSNI method is not usable in Android

  /**
   * Set up Wave's webclient old session object (Session).
   *
   * @param localDomain
   * @param userAddress
   * @param sessionId
   */

//  private native void createWebClientSession(String localDomain, String userAddress,
//      String sessionId) /*-{
//                        $wnd.__session = new Object();
//                        $wnd.__session['domain'] = localDomain;
//                        $wnd.__session['address'] = userAddress;
//                        $wnd.__session['id'] = sessionId;
//                        }-*/;

  /**
   * Initialize the infrastructure of communications with the wave server for
   * the logged in user. It seems we need a new Web socket client instance for
   * each user session. Not sure how to close a websocket.
   */
  private void startComms() {

    assert (loggedInUser != null);

    log.log(Level.INFO, "Starting wave session...");

    String webSocketURL = waveServerURLSchema.equals("http://") ? "ws://" : "wss://";
    webSocketURL += waveServerURL + "/";

    websocket = new WaveWebSocketClient(true, webSocketURL);
    websocket.connect();

    channel = new RemoteViewServiceMultiplexer(websocket, loggedInUser.getAddress());
    idGenerator = new IdGeneratorExtendedImpl(ClientIdGenerator.create());
    seed = Session.get().getIdSeed();

    waveContentManager =
        WaveContentManager.create(waveStore, waveServerDomain, idGenerator,
        loggedInUser, seed,
        channel);

  }

  public void stopComms() {
    waveContentManager = null;
    channel = null;
    seed = null;
  }

  /**
   * Retrieves a list of wave digests for the user logged in the wave server.
   *
   * @param userName
   * @param startIndex
   * @param numResults
   * @param callback
   */
  public void getWaves(int startIndex, int numResults,
      SearchService.Callback callback) {

    searchBuilder.newSearch().setQuery("").setIndex(startIndex).setNumResults(numResults)
        .search(callback);

  }



  /*******************************************************************************************/




  /**
   * Login the user in and start communications with Wave provider
   *
   * @param url to the Wave server, https://local.net:9898
   * @param user
   * @param password
   * @param callback
   * @return
   */
  public boolean startSession(String user, String password, final String url,
      final Callback<String, String> callback) {

    // TODO validate url, if it fails return false

    waveServerURLSchema = url.startsWith("http://") ? "http://" : "https://";
    // TODO extract domain from URL
    waveServerDomain = "local.net";
    waveServerURL = url.replace(waveServerURLSchema, "");

    login(user, password, new Callback<String, String>() {

      @Override
      public void onFailure(String reason) {
        callback.onFailure(reason);
      }

      @Override
      public void onSuccess(String result) {

        searchBuilder =
            CustomJsoSearchBuilderImpl.create(waveServerURLSchema + waveServerURLSchema);
        startComms();
        callback.onSuccess(result);

      }
    });

    return true;

  }




  /**
   * Logout user and close communications with Wave provider
   *
   * @return
   */
  public boolean stopSession() {

    // Destroy all waves
    for (Entry<String, WaveContentWrapper> entry : activeWaveMap.entrySet())
      entry.getValue().destroy();

    activeWaveMap.clear();

    // Disconnect from Wave's websocket
    stopComms();

    // Clear user session
    Cookies.removeCookie(SESSION_COOKIE_NAME);
    loggedInUser = null;
    searchBuilder = null;


    return true;

  }



  public String createWave(String type, final Callback<WaveContentWrapper, String> callback) {

    WaveType waveType = WaveType.valueOf(type);
    if (WaveType.UNKNOWN == waveType) return null;

    final WaveId waveId = idGenerator.newWaveId(waveType);
    final WaveContentWrapper waveWrapper =
        waveContentManager.getWaveContentWrapper(WaveRef.of(waveId), true);

    activeWaveMap.put(waveId.toString(), waveWrapper);


    waveWrapper.load(new Command() {
      @Override
      public void execute() {

        callback.onSuccess(waveWrapper);

      }
    });

    return waveId.toString();


  }


  /**
   * Open an existing wave.
   *
   * @param wave WaveId
   * @param callback
   * @return null if wave is not a valid WaveId. The WaveId otherwise.
   */
  public String openWave(final String wave, final Callback<WaveContentWrapper, String> callback) {

    // if (activeWaveMap.containsKey(wave)) return wave;

    WaveId waveId = null;
    try {

      waveId = WaveId.deserialise(wave);

    } catch (Exception e) {
      callback.onFailure(e.getMessage());
      return null;
    }

    final WaveContentWrapper waveWrapper =
        waveContentManager.getWaveContentWrapper(WaveRef.of(waveId), false);

    activeWaveMap.put(wave, waveWrapper);

    waveWrapper.load(new Command() {
      @Override
      public void execute() {

        callback.onSuccess(waveWrapper);

      }
    });

    return wave;

  }




  public boolean closeWave(String wave) {

    WaveContentWrapper waveWrapper = activeWaveMap.get(wave);

    if (waveWrapper == null) return false;

    waveWrapper.destroy();
    activeWaveMap.remove(wave);

    return true;

  }




}
