package com.sumarnakreatip.uiiot;

import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.PolylineOptions;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sumarnakreatip.uiiot.R;

@SuppressLint("Instantiatable")
public class DummyActivity extends FragmentActivity  {
	Button gmaps;
	Intent intent;
	EditText asal,tujuan;
	String type, gotAddresses1, gotAddresses2;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	        setContentView(R.layout.dummy);
			asal= (EditText) this.findViewById(R.id.asal);
	        tujuan= (EditText) this.findViewById(R.id.tujuan);
	    	
	        gmaps=(Button)this.findViewById(R.id.button1);
	        gmaps.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					intent = new Intent(DummyActivity.this, MapActivity.class);				 
					startActivity(intent);
				}}
			);
	        String gotAddresses1 = getIntent().getExtras().getString("mulai");//it requires api 12
	      //the second parameter is optional . If keyName is null then use the `defaultkey` as data.
	        asal.setText(gotAddresses1); gotAddresses2 = getIntent().getExtras().getString("akhir");//it requires api 12
		      //the second parameter is optional . If keyName is null then use the `defaultkey` as data.	           
	        tujuan.setText(gotAddresses2);
	        
	    }
}
