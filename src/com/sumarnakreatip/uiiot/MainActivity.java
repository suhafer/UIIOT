package com.sumarnakreatip.uiiot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {

	//Variabel koneksi
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	String respon;
	
	private ProgressBar mProgress;
	private int mProgressStatus = 0;
	
	//variabel GCM
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
	TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;	
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "771209098584";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCMDemo";

    //control handler   
    private Handler mHandler = new Handler();

	@Override
	protected void onCreate (Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.activity_main);
	    
	    //xml progress bar
	    mProgress = (ProgressBar) findViewById(R.id.progress);
	    context = getApplicationContext();
	    
	    //cek register id tuk gcm
	    if (checkPlayServices()) {
	        gcm = GoogleCloudMessaging.getInstance(this);
	        regid = getRegistrationId(context);
	        
	        if (regid.equalsIgnoreCase("")) {
	            //registerInBackground();
	            loggin asyncRate = new loggin();
	            asyncRate.execute();
	            Log.e("==========================","=========================");
	            Log.e("regid",regid);
	            Log.e("==========================","=========================");
	        }
	        else{
	        	Log.e("==========================","=========================");
	            Log.e("regid",regid);
	            Log.e("==========================","=========================");
	            
	        }
	        
	        // Start lengthy operation in a background thread
	        //pengecekan login
	        new Thread(new Runnable() {
	            public void run() {
                    // penentuan jalur, langsung masuk home atau ke halaman login
                	if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
                	{
                		while (mProgressStatus < 100) {
                            mProgressStatus = mProgressStatus + doWork();
                            // Update the progress bar
                            mHandler.post(new Runnable() {
                                public void run() {
                                    mProgress.setProgress(mProgressStatus);
                                }
                            });
                        }
                        login();
                	}
                	else{
                		while (mProgressStatus < 100) {
                            mProgressStatus = mProgressStatus + doWork();
                            // Update the progress bar
                            mHandler.post(new Runnable() {
                                public void run() {
                                    mProgress.setProgress(mProgressStatus);
                                }
                            });
                        }
                		home();
                	}     
	            }
	        }).start();               
	    }
	    else {
	    	Log.i(TAG, "No valid Google Play Services APK found.");
            Toast.makeText(context, "No valid Google Play Services APK found.", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
    
    public void onDestroy(){
		super.onDestroy();
	}
    
    //method untuk menunggu
    public int doWork(){
    	int a=0,b =0;
    	while (a<=20000000){
    		a++;
    	}
    	b++;
    	return b;
    }
    
    //ke halaman login
    public void login(){
    	Intent intent = new Intent(this, LoginPage.class);
    	intent.putExtra("regid",  regid.toString().trim());
        startActivity(intent);
        finish();
    }
    
    //method ke halaman home
    public void home(){
    	Intent intent = new Intent(this, Home.class);
    	intent.putExtra("username",SaveSharedPreference.getUserName(MainActivity.this).trim());
    	intent.putExtra("regid",  regid.toString().trim());
        startActivity(intent);
        finish();
    }
    
    //method pengecekan service gcm
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            	//Toast.makeText(this,"This device is not supported.", Toast.LENGTH_LONG).show();
            	Log.i(TAG, "This device is not supported.");
            	Toast.makeText(context, "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
    
    /**
     *
     * method untuk cek dan dapatkan regid tuk gcm
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            //Toast.makeText(context, "Registration not found.", Toast.LENGTH_LONG).show();
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            //Toast.makeText(context, "App version changed.", Toast.LENGTH_LONG).show();
            return "";
        }
        return registrationId;
    }
    
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    
    
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    //method untuk mengirim regid ke server
    private String sendRegistrationIdToBackend() {
    	try {
			httpclient = new DefaultHttpClient();
			// sure the <span id="IL_AD9" class="IL_AD">url</span> is correct.
			httppost = new HttpPost(
					"http://green.ui.ac.id/nebeng/tigatombol.php");
			// add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android
			// side variable name and php side variable name should be <span
			// id="IL_AD8" class="IL_AD">similar</span>,
			nameValuePairs.add(new BasicNameValuePair("username", SaveSharedPreference.getUserName(MainActivity.this).toString()
					.trim())); // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("regid",  regid.toString().trim())); // $Edittext_value =
													// $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			// class="IL_AD">from here</span>....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost,
					responseHandler);
			System.out.println("Response : " + response);
			return response;
		} catch (Exception e) {
			String response = "Catch";
			System.out.println("Exception : " + e.getMessage());
			return response;
		}
    }
    
    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        //Toast.makeText(context, "Saving regId on app version " + appVersion, Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    
    //koneksi tuk cek gcm
    private class loggin extends AsyncTask<Void, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//dialog = ProgressDialog.show(context, "", "Proccessing...", true);
		}

		@Override
		protected String doInBackground(Void... params) {
			String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;
                storeRegistrationId(context, regid);                   
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
		}

		@Override
		protected void onPostExecute(String rate) {
			
		}
	}       
}
