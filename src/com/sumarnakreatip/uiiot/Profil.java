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

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sumarnakreatip.uiiot.ProfilHttpClient.Function;
import com.sumarnakreatip.uiiot.ProfilHttpClient.Product;


@SuppressLint("InflateParams")
public class Profil extends Fragment {

	ImageView ivIcon;
	TextView tvItemName;
	
	ImageButton b,c;
	EditText asal,tujuan,kota,w_b;
	RadioGroup jl;
	String type,et,update;
	
	Context layout;
	
	private String respon,regid;
	//private TextView tv;
	
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	
	final ProfilHttpClient n;
	final StatusHttpClient m;
	View view;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";
	

	public Profil(Context ctx, String user, String reg) {
		this.layout=ctx;
		this.et=user;
		this.n = new ProfilHttpClient(et);
		this.m = new StatusHttpClient(et);
		this.regid=reg;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.layout_profil, container,
				false);
		//profil asyncRateprofil = new profil();
        //asyncRateprofil.execute();
		//final LinearLayout rows = (LinearLayout) view.findViewById(R.id.rows);
		tvItemName = (TextView) view.findViewById(R.id.idreg);
		//tvItemName.setText(regid);
		
        n.get_all_products(new Function<List<Product>, Void>() {
			
			@Override
			public Void call(List<Product> input) {
				LinearLayout rows = (LinearLayout) view.findViewById(R.id.rows);
				for (Product p:input){
					final View row = LayoutInflater.from(layout).inflate(R.layout.tampilprofil, null);
					((TextView)row.findViewById(R.id.npm)).setText(p.npm);
					((TextView)row.findViewById(R.id.nama)).setText(p.nama);
					((TextView)row.findViewById(R.id.role)).setText(p.role);
					/*
					((TextView)row.findViewById(R.id.jenis_kelamin)).setText(p.jenis_kelamin);
					((TextView)row.findViewById(R.id.alamat)).setText(p.alamat);
					((TextView)row.findViewById(R.id.email)).setText(p.email);
					((TextView)row.findViewById(R.id.no_hp)).setText(p.no_hp);
					((TextView)row.findViewById(R.id.no_telp)).setText(p.no_telp);*/
					rows.addView(row);
				}
				return null;
			}
		});
		m.get_all_products(new StatusHttpClient.Function<List<StatusHttpClient.Product>, Void>() {
			
			@Override
			public Void call(List<StatusHttpClient.Product> input) {
				LinearLayout rows = (LinearLayout) view.findViewById(R.id.rowd);
				for (StatusHttpClient.Product p:input){
					//final View row = LayoutInflater.from(layout).inflate(R.layout.tampilprofil, null);
					final View row = LayoutInflater.from(layout).inflate(R.layout.tampilstatus, null);
					((TextView)row.findViewById(R.id.nama)).setText(p.nama);
					((TextView)row.findViewById(R.id.no_hp)).setText(p.no_hp);
					((TextView)row.findViewById(R.id.email)).setText(p.email);
					((TextView)row.findViewById(R.id.status)).setText(p.status);
					rows.addView(row);
				}
				return null;
			}
		});
        
		b=(ImageButton)view.findViewById(R.id.batal);
		c=(ImageButton)view.findViewById(R.id.drop);
		
		b.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
        		update="batal";
        		loggin asyncRate = new loggin();
                asyncRate.execute();
                 
            }
        });
		c.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
        		update="reset";
        		loggin asyncRate = new loggin();
                asyncRate.execute();
                 
            }
        });
		return view;
	}
	
	/*
	private class profil extends AsyncTask<Void, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(layout, "", "Proccessing...", true);
		}

		@Override
		protected String doInBackground(Void... params) {

			
			
			
			respon="sukses";
			return respon;
		}

		@Override
		protected void onPostExecute(String rate) {
			dialog.dismiss();

		}
	}
	*/
	
	private class loggin extends AsyncTask<Void, String, String> {

		@Override
			protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(layout, "","Proccessing...", true);
		}
		@Override
        protected String doInBackground(Void... params) {

			String respo=login();

            return respo;
        }

        @Override
        protected void onPostExecute(String rate) {                     
            // Do whatever you need with the string, you can update your UI from here
        	respon=rate;
            if(respon.equalsIgnoreCase("Postingan nebeng berhasil dihapus")){
            	//setstatus(1);
            	Toast.makeText(layout, "Postingan nebeng berhasil dihapus", Toast.LENGTH_LONG).show();
            	segarkan();
              }else if(respon.equalsIgnoreCase("Tebengan berhasil dibatalkan")){
              	//setstatus(3);
              	Toast.makeText(layout, "Tebengan berhasil dibatalkan", Toast.LENGTH_LONG).show();
              	segarkan();
              }else{
              	//setstatus(2);
              	Toast.makeText(layout, "Proses tidak berhasil. Cek status anda.", Toast.LENGTH_LONG).show();
              }
            dialog.dismiss();
        	
        }
	}
	
	void segarkan(){
		int value=1;
    	Intent intent = new Intent(getActivity(), Home.class);
        intent.putExtra("username", et.toString().trim());
		intent.putExtra("Map", value);
		intent.putExtra("regid", regid);
        startActivity(intent);
        getActivity().finish();
	}
	
	String login(){
        try{            
              
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://green.ui.ac.id/nebeng/batal.php");
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be <span id="IL_AD8" class="IL_AD">similar</span>, 
            nameValuePairs.add(new BasicNameValuePair("username",et.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("update",update.toString().trim())); 
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            // edited by James from coderzheaven.. <span id="IL_AD6" class="IL_AD">from here</span>....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            return response;
        }catch(Exception e){
        	String response="Catch";
            System.out.println("Exception : " + e.getMessage());
        	return response;
        }
    }
	
}
