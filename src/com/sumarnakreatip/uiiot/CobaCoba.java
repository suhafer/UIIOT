package com.sumarnakreatip.uiiot;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CobaCoba extends Activity {
	
	Handler h = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		
		final UserCustomAdapter ad = new UserCustomAdapter(this, R.layout.tampil, new ArrayList<User>()); 
		final ListView lv = (ListView) findViewById(R.id.list_root);
		lv.setAdapter(ad);

		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(CobaCoba.this, "Click " + position, Toast.LENGTH_LONG).show();
				Log.d("MyDebug", "Click " + position);
			}
		});
				
		
		new Thread() {

			@Override
			public void run() {
				try {
					
					for(int ii = 0; ii < 10; ++ii) {
						Thread.sleep(1000);
						
						final int i = ii;
						h.post(new Runnable() {
							
							@Override
							public void run() {
								ad.add(new User("User" + i, 
						                "npm" + i, 
						                "nama" + i,
						                "asal" + i, 
						                "tujuan" + i, 
						                "kapasitas" + i, 
						                "waktu_berangkat" + i, 
						                "jam_berangkat" + i, 
						                "keterangan" + i));
							}
						});
					}
				} catch (InterruptedException e) {
				}
			}
			
		}.start();
		
		
	}
	
}