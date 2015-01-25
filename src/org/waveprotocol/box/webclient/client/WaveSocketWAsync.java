package org.waveprotocol.box.webclient.client;

import java.io.IOException;

import org.atmosphere.wasync.ClientFactory;
import org.atmosphere.wasync.Event;
import org.atmosphere.wasync.Function;
import org.atmosphere.wasync.Request;
import org.atmosphere.wasync.RequestBuilder;
import org.atmosphere.wasync.Socket;
import org.atmosphere.wasync.impl.AtmosphereClient;

import android.util.Log;

public class WaveSocketWAsync implements WaveSocket {

	
	private final AtmosphereClient client;
	@SuppressWarnings("rawtypes")
	private final RequestBuilder requestBuilder;
	private final Socket socket;
	
	public WaveSocketWAsync(final WaveSocket.WaveSocketCallback callback, String urlBase) {
		this.client =  ClientFactory.getDefault().newClient(AtmosphereClient.class);
		
		this.requestBuilder = client.newRequestBuilder()
				.method(Request.METHOD.GET)
				.trackMessageLength(true)
                .uri(urlBase)
                .transport(Request.TRANSPORT.WEBSOCKET);
		
		this.socket = client.create()
				.on(Event.OPEN.name(), new Function<String>() {

					@Override
					public void on(String arg0) {
						callback.onConnect();
					}
					
				}).on(Event.CLOSE.name(), new Function<String>() {

					@Override
					public void on(String arg0) {
						callback.onDisconnect();
					}
					
				}).on(Event.MESSAGE.name(), new Function<String>() {

					@Override
					public void on(String msg) {
						callback.onMessage(msg);
					}
					
				}).on(new Function<Throwable>() {

		            @Override
		            public void on(Throwable t) {
		            	Log.d("WebSocket ERROR", "Error during the socket creation");
		                t.printStackTrace();
		            }

		        });
		
		
		
	}
	
	@Override
	public void connect() {
		try {
			
			socket.open(this.requestBuilder.build());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		socket.close();
	}


	
	@Override
	public void sendMessage(String message) {
		try {
			socket.fire(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
