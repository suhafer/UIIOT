package com.sumarnakreatip.uiiot;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Home extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;
	
	public String user,asal,tujuan,kapasitas,w_b,k,regid;
	int maps=0;
	boolean map=false,segarkan=false;
	
	List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		// Initializing
		user=getIntent().getExtras().getString("username");
		regid=getIntent().getExtras().getString("regid");
		maps=getIntent().getExtras().getInt("Map");
		segarkan=getIntent().getExtras().getBoolean("segar");
		
		
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Add Drawer Item to dataList
		dataList.add(new DrawerItem("Room", R.drawable.ic_action_cloud));
		dataList.add(new DrawerItem("Profil", R.drawable.ic_action_group));
		dataList.add(new DrawerItem("Beri Tebengan", R.drawable.ic_action_good));
		dataList.add(new DrawerItem("Tentang", R.drawable.ic_action_about));
		dataList.add(new DrawerItem("Log Out", R.drawable.ic_action_import_export));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (maps==0) {
			SelectItem(0);
		}
		else if (maps == 1) {
			SelectItem(1);
		}
		else if (maps == 2) {
			if(!segarkan){
				map=true;
				asal=getIntent().getExtras().getString("mulai");
				tujuan=getIntent().getExtras().getString("akhir");
				kapasitas=getIntent().getExtras().getString("kapasitas");
				w_b=getIntent().getExtras().getString("waktu_berangkat");
				k=getIntent().getExtras().getString("keterangan");
			}else{
				segarkan=false;
			}
			
			SelectItem(2);
		}
		else if (savedInstanceState == null)
		{
			SelectItem(0);
		}

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu_option, menu);
		return true;
	}
	
	/*
	public void updateItem(int respon,String response){
		Fragment fragment = null;
		Bundle args = new Bundle();
		
		fragment = new BeriTebengan(Home.this,user,respon,response);
		args.putString(BeriTebengan.ITEM_NAME, dataList.get(2).getItemName());
		args.putInt(BeriTebengan.IMAGE_RESOURCE_ID, dataList.get(2).getImgResID());

		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();

		mDrawerList.setItemChecked(2, true);
		setTitle(dataList.get(2).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);
	}*/

	public void SelectItem(int possition) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (possition) {
		case 0:
			fragment = new Room(Home.this,user,regid);
			args.putString(Room.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(Room.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 1:
			fragment = new Profil(Home.this,user,regid);
			args.putString(Profil.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(Profil.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			break;
		case 2:
			fragment = new BeriTebengan(Home.this,user,asal,tujuan,kapasitas,w_b,k,map,regid);
			args.putString(BeriTebengan.ITEM_NAME, dataList.get(possition)
					.getItemName());
			args.putInt(BeriTebengan.IMAGE_RESOURCE_ID, dataList.get(possition)
					.getImgResID());
			map=false;
			break;
		case 3:
			fragment = new About();
			break;
			
		case 4:
			fragment = new LogOut();
			SaveSharedPreference.clearUserName(Home.this);
			Intent intent = new Intent(this, LoginPage.class);
        	intent.putExtra("regid",  regid);
	        startActivity(intent);
	        finish();
			break;
		default:
			break;
		}

		fragment.setArguments(args);
		FragmentManager frgManager = getFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();

		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.muatulang:
				//SelectItem(mDrawerList.getCheckedItemPosition());
				if (mDrawerList.getCheckedItemPosition()==0){
					int value=0;
					Intent intent = new Intent(this, Home.class);
					intent.putExtra("username", user);
					intent.putExtra("Map", value);
					intent.putExtra("regid", regid);
			        startActivity(intent);
			        finish();
				}
				else if(mDrawerList.getCheckedItemPosition()==1){
					int value=1;
					Intent intent = new Intent(this, Home.class);
					intent.putExtra("username", user);
					intent.putExtra("Map", value);
					intent.putExtra("regid", regid);
			        startActivity(intent);
			        finish();
				}
				else if(mDrawerList.getCheckedItemPosition()==2){
					int value=2;
					Intent intent = new Intent(this, Home.class);
					intent.putExtra("username", user);
					intent.putExtra("regid", regid);
					intent.putExtra("Map", value);
					intent.putExtra("segar", true);
					intent.putExtra("mulai", "");
					intent.putExtra("akhir", "");
					intent.putExtra("kapasitas", "");
					intent.putExtra("waktu_berangkat", "");
					intent.putExtra("keterangan", "");
			        startActivity(intent);
			        finish();
				}
				else{
					
				}
	            return true;
	        default:
	        	//return false;
	        	return super.onOptionsItemSelected(item);
	    }
	}

        private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			SelectItem(position);

		}
	}
        
        public void onDestroy(){
    		super.onDestroy();
    	}
        /*
        @Override
    	public void onBackPressed() {
    	}*/
}