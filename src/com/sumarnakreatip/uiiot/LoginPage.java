package com.sumarnakreatip.uiiot;

import java.util.ArrayList;
import java.util.List;

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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class LoginPage extends Activity {
    
	//variabel layout
	Button b,c;
    EditText et,pass;
    
    //variabel koneksi
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    String regid;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        regid=getIntent().getExtras().getString("regid");
        b = (Button)findViewById(R.id.button1);  
        et = (EditText)findViewById(R.id.username);
        pass= (EditText)findViewById(R.id.password);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(LoginPage.this, "", 
                        "Processing...", true);
                 new Thread(new Runnable() {
                        public void run() {
                        		login();                          
                        }
                      }).start();               
            }
        });

    }
    
    //method tuk cek ke sso
    void login(){
        try{                      
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://green.ui.ac.id/nebeng/check.php");
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be <span id="IL_AD8" class="IL_AD">similar</span>, 
            nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim())); 
            nameValuePairs.add(new BasicNameValuePair("regid",regid)); 
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            //dialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    
                    dialog.dismiss();
                }
            });
            
            //cek apakah mahasiswa aktif atau tidak
            if(response.equalsIgnoreCase("User Found")){
            	runOnUiThread(new Runnable() {
                     public void run() {
            			Toast.makeText(LoginPage.this,"Login Sukses", Toast.LENGTH_LONG).show();
                     }
                 });
            	SaveSharedPreference.setUserName(LoginPage.this,et.getText().toString().trim());
                Intent datalogin = new Intent(LoginPage.this,Home.class);
                datalogin.putExtra("username",et.getText().toString().trim());
            	datalogin.putExtra("regid",  regid);
                startActivity(datalogin);
                finish();
            }
            else if(response.equalsIgnoreCase("User New")){
            	runOnUiThread(new Runnable() {
                     public void run() {
            			Toast.makeText(LoginPage.this,"Login Sukses", Toast.LENGTH_LONG).show();
                     }
                });
            	SaveSharedPreference.setUserName(LoginPage.this,et.getText().toString().trim());
                Intent datalogin = new Intent(LoginPage.this,Kontak.class);
                datalogin.putExtra("username",et.getText().toString().trim());
            	datalogin.putExtra("regid",  regid);
                startActivity(datalogin);
                finish();
            }
            else {
            	runOnUiThread(new Runnable() {
                    public void run() {
           			Toast.makeText(LoginPage.this,"Login Tidak berhasil, Cek username dan password anda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
             
        }catch(Exception e){
            dialog.dismiss();
            Toast.makeText(LoginPage.this,"Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    
    public void onDestroy(){
		super.onDestroy();
	}
}