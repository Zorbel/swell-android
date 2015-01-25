package org.waveprotocol.android;

import org.waveprotocol.android.client.WaveAndroidClient;

import com.google.gwt.core.client.Callback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class WaveAndroid extends Activity
{

	private WaveAndroidClient waveClient;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	
    	waveClient = new WaveAndroidClient(); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Context context = getApplicationContext();
        
        // Put the "User" and "Password" for the login on the first two parameters of startSession

    	waveClient.startSession("tim","tim", "http://localhost:9898", new Callback<String, String>() { //

			@Override
			public void onSuccess(String result) {

				Toast.makeText(context, "Connected to Wave server with session Id: "+result , Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(String reason) {

				Toast.makeText(context, "Failed Wave server connection: "+reason , Toast.LENGTH_LONG).show();
			}
		});

    }
}
