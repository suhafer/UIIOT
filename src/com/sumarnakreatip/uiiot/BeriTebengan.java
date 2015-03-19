package com.sumarnakreatip.uiiot;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


@SuppressLint("Instantiatable")
public final class BeriTebengan extends Fragment {

	ImageView ivIcon;
	TextView tvItemName;
	
	Context layout;
	
	Button submit, gmaps;
	EditText asal,tujuan,kota,keterangan;
	RadioGroup jl;
	String type,et,k, gotAddresses1, gotAddresses2,kuota,waktu,w_b,regid;
	StringBuilder wb;
	private boolean setbefore=false;
	
	private String respon;
	private TextView tv;
	
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	
	//private TimePicker timePicker1;
	private Button btnChangeTime;
 
	private int hour;
	private int minute;
 
	static final int TIME_DIALOG_ID = 999;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";
	

	public BeriTebengan(Context ctx, String user, String a, String b, String c, String d, String e, boolean f,String reg) {
		this.layout=ctx;
		this.et=user;
		this.gotAddresses1=a;
		this.gotAddresses2=b;
		this.kuota=c;
		this.waktu=d;
		this.k=e;
		this.setbefore=f;
		this.regid=reg;
	}
	
	
	//@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.beritebengan_layout, container,
				false);
		
		asal= (EditText)view.findViewById(R.id.asal);
        tujuan= (EditText)view.findViewById(R.id.tujuan);
        jl= (RadioGroup)view.findViewById(R.id.kuota);
        tv= (TextView)view.findViewById(R.id.w_b);
		btnChangeTime = (Button) view.findViewById(R.id.button2);
        keterangan= (EditText)view.findViewById(R.id.j_k);
        
        //tv = (TextView)view.findViewById(R.id.tv);
        //tv.setText(et);
        setCurrentTimeOnView(setbefore);
        if (setbefore){
        	asal.setText(gotAddresses1);
        	tujuan.setText(gotAddresses2);
        	keterangan.setText(k);
        	setbefore=false;
        	
        	if (kuota.equalsIgnoreCase("1")) {
				jl.check(R.id.no_1);
			} else if (kuota.equalsIgnoreCase("2")) {
				jl.check(R.id.no_2);
			} else if (kuota.equalsIgnoreCase("3")) {
				jl.check(R.id.no_3);
			} else if (kuota.equalsIgnoreCase("4")) {
				jl.check(R.id.no_4);
			} else if (kuota.equalsIgnoreCase("5")) {
				jl.check(R.id.no_5);
			} else if (kuota.equalsIgnoreCase("6")) {
				jl.check(R.id.no_6);
			} else {
				
			}
        }
        
        submit=(Button)view.findViewById(R.id.button1);
        
        gmaps=(Button)view.findViewById(R.id.button4);
        
        
        btnChangeTime.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
        		onCreateDialog(TIME_DIALOG_ID).show();
                 
            }
        });
        gmaps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				int checkedRadioButtonId = jl.getCheckedRadioButtonId();
				if (checkedRadioButtonId == R.id.no_1) {
					type="1";
				} else if (checkedRadioButtonId == R.id.no_2) {
					type="2";
				} else if (checkedRadioButtonId == R.id.no_3) {
					type="3";
				} else if (checkedRadioButtonId == R.id.no_4) {
					type="4";
				} else if (checkedRadioButtonId == R.id.no_5) {
					type="5";
				} else if (checkedRadioButtonId == R.id.no_6) {
					type="6";
				} else {
					type="";
				}
        		if (keterangan.getText().toString().trim().equalsIgnoreCase(""))
        			{k=" ";}
        		else{k=keterangan.getText().toString().trim();}
        		
			Intent gmaps = new Intent(getActivity(), MapActivity.class);
            gmaps.putExtra("username",et.toString().trim());
            gmaps.putExtra("kapasitas",type.toString().trim());
            gmaps.putExtra("waktu_berangkat",w_b.toString().trim());
            gmaps.putExtra("keterangan",k.toString().trim());
			gmaps.putExtra("regid", regid);
            startActivity(gmaps);
            getActivity().finish();
			}
		});
        

//        
//        if(asal!=null){
//		Bundle gotBasket1 = getActivity().getIntent().getExtras();
//		gotAddresses1 = gotBasket1.getString("key1");
//		asal.setText(gotAddresses1);
//        }
        
        submit.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
        		int checkedRadioButtonId = jl.getCheckedRadioButtonId();
				if (checkedRadioButtonId == R.id.no_1) {
					type="1";
				} else if (checkedRadioButtonId == R.id.no_2) {
					type="2";
				} else if (checkedRadioButtonId == R.id.no_3) {
					type="3";
				} else if (checkedRadioButtonId == R.id.no_4) {
					type="4";
				} else if (checkedRadioButtonId == R.id.no_5) {
					type="5";
				} else if (checkedRadioButtonId == R.id.no_6) {
					type="6";
				} else {
					type="";
				}
        		if (keterangan.getText().toString().trim().equalsIgnoreCase(""))
        			{k=" ";}
        		else{k=keterangan.getText().toString().trim();}
        			
        		if (asal.getText().toString().trim().equalsIgnoreCase("") &&
        				tujuan.getText().toString().trim().equalsIgnoreCase("")&&
        				type.equalsIgnoreCase("")){
        			Toast.makeText(layout, "Tolong Isi Form dengan Benar", Toast.LENGTH_LONG).show();
        		}
        		else{
            		loggin asyncRate = new loggin();
                    asyncRate.execute();
        		}
                 
            }
        });
		
		return view;
	}
	
	
	
	
	String login(){
        try{            
              
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://green.ui.ac.id/nebeng/beri_tebengan.php");
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be <span id="IL_AD8" class="IL_AD">similar</span>, 
            nameValuePairs.add(new BasicNameValuePair("username",et.toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("asal",asal.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("tujuan",tujuan.getText().toString().trim())); 
            nameValuePairs.add(new BasicNameValuePair("kapasitas",type.toString().trim())); 
            nameValuePairs.add(new BasicNameValuePair("waktu_berangkat",w_b.toString().trim())); 
            nameValuePairs.add(new BasicNameValuePair("keterangan",k.toString().trim()));
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
	
	
	public class loggin extends AsyncTask<Void, String, String> {

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
            dialog.dismiss();
            if(respon.equalsIgnoreCase("Sukses")){
            	Toast.makeText(layout, "Sukses Dimasukkan", Toast.LENGTH_LONG).show();
            	segarkan();
              }else if(respon.equalsIgnoreCase("Catch")){
              	Toast.makeText(layout, "Gagal Dimasukkan", Toast.LENGTH_LONG).show();
              }else{
              	Toast.makeText(layout, rate, Toast.LENGTH_LONG).show();
              }
        	
        }
	}
	void segarkan(){
		int value=0;
    	Intent intent = new Intent(getActivity(), Home.class);
        intent.putExtra("username", et.toString().trim());
		intent.putExtra("Map", value);
        startActivity(intent);
        getActivity().finish();
	}
	// display current time
		public void setCurrentTimeOnView(boolean a) {
	 
			//tvDisplayTime = (TextView) findViewById(R.id.tvTime);
			//timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
			if(a){
				tv.setText(waktu);
				w_b=waktu;
				}
			else
			{
				final Calendar c = Calendar.getInstance();
				hour = c.get(Calendar.HOUR_OF_DAY);
				minute = c.get(Calendar.MINUTE);
		 
				// set current time into textview
				tv.setText(
		                    new StringBuilder().append(pad(hour))
		                                       .append(":").append(pad(minute)));
				wb=new StringBuilder().append(pad(hour)).append(".").append(pad(minute));
				w_b=wb.toString();
				/* set current time into timepicker
				timePicker1.setCurrentHour(hour);
				timePicker1.setCurrentMinute(minute);*/
			}
		}
	
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case TIME_DIALOG_ID:
				// set time picker as current time
				return new TimePickerDialog(layout,timePickerListener, hour, minute,false);
	 
			}
			return null;
		}
	 
		private TimePickerDialog.OnTimeSetListener timePickerListener = 
	            new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int selectedHour,
					int selectedMinute) {
				hour = selectedHour;
				minute = selectedMinute;
	 
				// set current time into textview
				tv.setText(new StringBuilder().append(pad(hour))
						.append(":").append(pad(minute)));
				wb=new StringBuilder().append(pad(hour)).append(".").append(pad(minute));
				w_b=wb.toString();
				/* set current time into timepicker
				timePicker1.setCurrentHour(hour);
				timePicker1.setCurrentMinute(minute);*/
	 
			}
		};
	 
		private static String pad(int c) {
			if (c >= 10)
			   return String.valueOf(c);
			else
			   return "0" + String.valueOf(c);
		}
   
}
