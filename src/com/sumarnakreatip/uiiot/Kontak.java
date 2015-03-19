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
 
public class Kontak extends Activity {
    
	//variabel layout
	Button b,c;
    EditText nomor,email;
    String regid,user;
    
    //variabel koneksi
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kontak);
        
        //inisialisasi variabel
		user=getIntent().getExtras().getString("username");
		regid=getIntent().getExtras().getString("regid");
        b = (Button)findViewById(R.id.button1);  
        nomor = (EditText)findViewById(R.id.nomor);
        email= (EditText)findViewById(R.id.email);
        
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Kontak.this, "", 
                        "Processing...", true);
                 new Thread(new Runnable() {
                        public void run() {
                        		masukkan();                          
                        }
                      }).start();               
            }
        });

    }
    
    //method tuk masukkan data ke profil
    void masukkan(){
        try{                      
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://green.ui.ac.id/nebeng/update_hp_email.php");
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be <span id="IL_AD8" class="IL_AD">similar</span>, 
            nameValuePairs.add(new BasicNameValuePair("username",user));
            nameValuePairs.add(new BasicNameValuePair("nomor",nomor.getText().toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("email",email.getText().toString().trim())); 
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
            
            //cek apakah data sudah berhasil dimasukkan atau belum
            if(response.equalsIgnoreCase("Sukses")){
		    	runOnUiThread(new Runnable() {
		             public void run() {
		    			Toast.makeText(Kontak.this,"Data sukses dimasukkan", Toast.LENGTH_LONG).show();
		             }
		        });
                Intent datalogin = new Intent(Kontak.this,Home.class);
                datalogin.putExtra("username",user);
            	datalogin.putExtra("regid",  regid);
                startActivity(datalogin);
                finish();
            }
            else {
            	runOnUiThread(new Runnable() {
                    public void run() {
           			Toast.makeText(Kontak.this,"Data tidak berhasil dimasukkan, coba ulang", Toast.LENGTH_SHORT).show();
                    }
                });
            }
             
        }catch(Exception e){
            dialog.dismiss();
            Toast.makeText(Kontak.this,"Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    
    public void onDestroy(){
		super.onDestroy();
	}
}