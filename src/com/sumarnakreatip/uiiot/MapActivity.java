package com.sumarnakreatip.uiiot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends FragmentActivity implements RoutingListener, LocationListener {
		Button Submit;
		GoogleMap googleMap;
	    MarkerOptions markerOptions;
	    LatLng latlng;
	    Intent intent;
	    Handler handler = new Handler();
	    String user, kapasitas,w_b,k,regid;
	    Marker markerAsal, markerTujuan;
	    //myObject
	    private Marker markerClicked;
	    
	    //koordinat marker untuk setiap fakultas & tempat
	    
	    static final LatLng RIK = new LatLng(-6.370065, 106.829798);
	    static final LatLng FMIPA = new LatLng(-6.369785, 106.826529);
	    static final LatLng FT = new LatLng(-6.362286, 106.823981);
	    static final LatLng FH = new LatLng(-6.364271, 106.831325);
	    static final LatLng FE = new LatLng(-6.360645, 106.825672);
	    static final LatLng FIB = new LatLng(-6.363087, 106.828565);
	    static final LatLng FPSI = new LatLng(-6.363188, 106.830778);
	    static final LatLng FISIP = new LatLng(-6.362480, 106.829426);
	    static final LatLng FKM = new LatLng(-6.371039, 106.828874);
	    static final LatLng FASILKOM = new LatLng(-6.364553, 106.828643);
	    static final LatLng FIK = new LatLng(-6.372745, 106.829336);
	    //static final LatLng FF = new LatLng(-6.368294, 106.826012);
	    static final LatLng VOKASI = new LatLng(-6.369589, 106.820754);
	    static final LatLng PERPUSAT = new LatLng(-6.364990, 106.830928);
	    
	    public String selectedAddresses, awal, selesai; 
	    int count = 0;
	    
	    private HashMap<String, String> assemblyPoint = new HashMap<String, String>();
	    
	    
	    //GEOCODING
	    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>
	    {
	    	Context mContext;
	    		public ReverseGeocodingTask(Context context)
	    		{
	    			super();
	                 mContext = context;
	             }
	             // Finding address using reverse geocoding
	             @Override
	             protected String doInBackground(LatLng... params) 
	             {
	             	//Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
	             	Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
	             	double latitude = params[0].latitude;
	                double longitude = params[0].longitude;
	                List<Address> addresses = null;
	                String addressText="";
	                try 
	                	{
	                	addresses = geocoder.getFromLocation(latitude, longitude,5);
	                	} catch (IOException e) {
	                	// TODO Auto-generated catch block
	                     e.printStackTrace();
	                	}
	                
	                 //if(addresses != null && addresses.size() > 0 )
	                while(addresses!= null && count < addresses.size())
	                 	{
	                     Address address = addresses.get(0);	      
	                     addressText = String.format("%s, %s, %s",
	                     address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                     address.getLocality(),
	                     address.getCountryName());
	                     count++;
	                     addresses = null;
	                 	}
	                 selectedAddresses = addressText;
	                 System.out.println(selectedAddresses);
	                 return addressText;	
	             }	        
	    } 
	    //END OF GEOCODING
	    
		Context context = this;
		double latt;
	    double longg;
	    StringBuilder text = new StringBuilder(); 
//	    double[][] lati =new double[50][100];
//	    double[][] longi =new double[50][100];
	  double[] lati =new double[1000];
	  double[] longi =new double[1000];
	    Double getlat;
	    Double getlong;
	    int i=0;
	    protected GoogleMap map;
	    public LatLng start;
	    public LatLng end;
	    public LatLng GPS;
	    int a=0; //dummy untuk menentukan koordinat yang harus ditempuh


	 // Milliseconds per second
	    private static final int MILLISECONDS_PER_SECOND = 1000;
	    // Update frequency in seconds
	    public static final int UPDATE_INTERVAL_IN_SECONDS = 3;
	    // Update frequency in milliseconds
	    private static final long UPDATE_INTERVAL =
	            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	    // The fastest update frequency, in seconds
	    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	    // A fast frequency ceiling in milliseconds
	    private static final long FASTEST_INTERVAL =
	            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	 // Define an object that holds accuracy and frequency parameters
	    LocationRequest mLocationRequest;
	    LocationClient mLocationClient;
	//ambil data dari txt file
	                 
	    /**
	     * This activity loads a map and then displays the route and pushpins on it.
	     */

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRetainInstance(true);
	        
	        user=getIntent().getExtras().getString("username");
	        kapasitas=getIntent().getExtras().getString("kapasitas");
	        w_b=getIntent().getExtras().getString("waktu_berangkat");
	        k=getIntent().getExtras().getString("keterangan");
	        regid=getIntent().getExtras().getString("regid");
	        
	        
	        
	     // Create the LocationRequest object
	        mLocationRequest = LocationRequest.create();
	        // Use high accuracy
	        mLocationRequest.setPriority(
	                LocationRequest.PRIORITY_HIGH_ACCURACY);
	        // Set the update interval to 5 seconds
	        mLocationRequest.setInterval(UPDATE_INTERVAL);
	        // Set the fastest update interval to 1 second
	        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
	        			
	        setContentView(R.layout.map);
	        SupportMapFragment fm = (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map);
	        map = fm.getMap();
	        CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(-6.366887,106.824476));
	        CameraUpdate zoom=  CameraUpdateFactory.zoomTo(15);
	        map.setMyLocationEnabled(true);
	        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
	        Criteria crit = new Criteria();
	        crit.setAccuracy(Criteria.ACCURACY_FINE);
	        String provider = lm.getBestProvider(crit, true);
	        Location location = lm.getLastKnownLocation(provider);

	        //membuat marker dan getId marker pada Hashmap
	        if (map!=null){
	            Marker rik = map.addMarker(new MarkerOptions().position(RIK)
	                .title("RIK").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(rik.getId(), "RIK");
	            Marker fmipa = map.addMarker(new MarkerOptions()
	                .position(FMIPA)
	                .title("FMIPA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(fmipa.getId(), "FMIPA");
	            Marker ft = map.addMarker(new MarkerOptions().position(FT)
		                .title("FT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(ft.getId(), "FT");
	            
		        Marker fh = map.addMarker(new MarkerOptions()
		                .position(FH)
		                .title("FH").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		        assemblyPoint.put(fh.getId(), "FH");
		        
	            Marker fe = map.addMarker(new MarkerOptions().position(FE)
		                .title("FE").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(fe.getId(), "FE");
		        Marker fib = map.addMarker(new MarkerOptions()
		                .position(FIB)
		                .title("FIB").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		        assemblyPoint.put(fib.getId(), "FIB");
	            Marker fpsi = map.addMarker(new MarkerOptions().position(FPSI)
		                .title("FPSI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(fpsi.getId(), "FPsi");
		        Marker fisip = map.addMarker(new MarkerOptions()
		                .position(FISIP)
		                .title("FISIP").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		        assemblyPoint.put(fisip.getId(), "FISIP");
	            Marker fkm = map.addMarker(new MarkerOptions().position(FKM)
		                .title("FKM").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(fkm.getId(), "FKM");
		        Marker fasilkom = map.addMarker(new MarkerOptions()
		                .position(FASILKOM)
		                .title("FASILKOM").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		        assemblyPoint.put(fasilkom.getId(), "Fasilkom");
	            Marker fik = map.addMarker(new MarkerOptions().position(FIK)
		                .title("FIK").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(fik.getId(), "FIK");
	            /*
		        Marker ff = map.addMarker(new MarkerOptions()
		                .position(FMIPA)
		                .title("FMIPA"));
		                */
	            Marker vokasi = map.addMarker(new MarkerOptions().position(VOKASI)
		                .title("VOKASI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            assemblyPoint.put(vokasi.getId(), "Vokasi");
		        Marker perpusat = map.addMarker(new MarkerOptions()
		                .position(PERPUSAT)
		                .title("PERPUSAT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		        assemblyPoint.put(perpusat.getId(), "Perpusat");
	          }
	        //END
	        
	        
	        map.moveCamera(center);
	        map.animateCamera(zoom);

	        long min=0;
	        float dist=0;
	        lm.requestLocationUpdates(provider, min, dist, this);

	        Submit=(Button)this.findViewById(R.id.button1);
	        Submit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*
					intent = new Intent(MapActivity.this, DummyActivity.class);
					intent.putExtra("mulai", awal);  // pass your values and retrieve them in the other Activity using keyName
					intent.putExtra("akhir", selesai);
					*/
					int value=2;
					intent = new Intent(MapActivity.this, Home.class);
					intent.putExtra("mulai", awal);  // pass your values and retrieve them in the other Activity using keyName
					intent.putExtra("akhir", selesai);
					intent.putExtra("username", user);
					intent.putExtra("kapasitas", kapasitas);
					intent.putExtra("waktu_berangkat", w_b);
					intent.putExtra("keterangan", k);
					intent.putExtra("Map", value);
					intent.putExtra("regid", regid);
					startActivity(intent);
	                finish();
				}}
			);
	        
	    //marker selain fakultas sekali klik saja
	         map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
	            public void onMapClick(final LatLng latlng) {
	                //LayoutInflater li = LayoutInflater.from(context);
	                //final View v = li.inflate(R.layout.startlayout, null);
	                AlertDialog.Builder builder = new AlertDialog.Builder(context);
	               // builder.setView(v);
	                builder.setCancelable(true);   
	                builder.setTitle("Tandai ... Anda");
	                builder.setPositiveButton("Tujuan", new DialogInterface.OnClickListener() {
	                	public void onClick(DialogInterface dialog, int which) {     
	                		// Adding Marker on the touched location with address
	                        new ReverseGeocodingTask(getBaseContext()).execute(latlng); 	                       
	                        Thread t = new Thread( new Runnable(){
	                            public void run() {
	                                for (int count = 0; count < 10; count++){
	                                    handler.postDelayed(new Runnable(){
	                                        public void run() {
	                                        	// String sll = latlng.latitude + " " + latlng.longitude;
	                                            latt=latlng.latitude;
	                                            longg=latlng.longitude;             
//	                                            //start = GPS;
	                                            //start = new LatLng(-6.367058,106.824927);
	                                            end = new LatLng(latt, longg); 
	                                            if (markerTujuan != null){
	                                            	markerTujuan.remove();
	                                            }
	                                            markerTujuan = map.addMarker(new MarkerOptions()
	                                            .title(""+selectedAddresses)                                  
	                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
	                                             .position(latlng)
	                                             );
	                                        }                   
	                                    }, 10);
	                                }
	                            }
	                        });	                      
	                        t.start();                                            
	                        	//routeStart();   
	                            try {
	                         	   
	    							Thread.sleep(3000);
	    						} catch (InterruptedException e) {
	    							// TODO Auto-generated catch block
	    							e.printStackTrace();
	    						}
	                            selesai = selectedAddresses;
	                            if(selesai==null)
	                            	selesai="daerah tidak terdaftar";
	                            else
	                            	System.out.println(selesai);
	                        }
	                });     
	                builder.setNegativeButton("Asal", new DialogInterface.OnClickListener() {
	                	public void onClick(DialogInterface dialog, int which) { 
	                		
	                        // Adding Marker on the touched location with address
	                        new ReverseGeocodingTask(getBaseContext()).execute(latlng); 	                       
	                        Thread t = new Thread( new Runnable(){
	                            public void run() {
	                                for (int count = 0; count < 10; count++){
	                                    handler.postDelayed(new Runnable(){
	                                        public void run() {                                                                                      
	                                            //String sll = latlng.latitude + " " + latlng.longitude;
	                                            latt=latlng.latitude;
	                                            longg=latlng.longitude;             
	                                          // start = GPS;
	                                            //start = new LatLng(-6.367058,106.824927);
	                                           start = new LatLng(latt, longg);
	                                           if (markerAsal != null){
	                                            	markerAsal.remove();
	                                            }
	                                           markerAsal = map.addMarker(new MarkerOptions()
	                                           .title(""+selectedAddresses)
//	                                           .snippet(snippet.getText().toString())
	                                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
	                                           .position(latlng)
	                                           );
	                                        }                   
	                                    }, 10);
	                                }
	                            }
	                        });	                       
	                        t.start(); 
	                        //routeStart();	                        
	                       try {
	                    	   
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                       awal = selectedAddresses;
	                       if(selesai==null)
                           	selesai="daerah tidak terdaftar";
                           else 
                        	   System.out.println(awal);
	                } });
	               
					                AlertDialog alert = builder.create();
									alert.show();  
	         
	            }});	
	    
	         //markerclicklistener
	         map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() 
	         {
	         	@Override
	         	public boolean onMarkerClick(Marker arg0) 
	         	{ // TODO Auto-generated method stub
	         		//myObject = arg0;
	         		final String id = assemblyPoint.get(arg0.getId());
	         		
	         		AlertDialog.Builder fakultasmarker = new AlertDialog.Builder(context);
	         		fakultasmarker.setTitle("Tandai "+ id + " sebagai ... Anda");
	         		fakultasmarker.setPositiveButton("Tujuan", null);
	         		fakultasmarker.setPositiveButton("Tujuan", new DialogInterface.OnClickListener() 
	         		{
	         			public void onClick(DialogInterface dialog, int which) 
	         			{     // Adding Marker on the touched location with address
	         				selesai = id;
	         			}
	         		});	                        

	         		fakultasmarker.setNegativeButton("Asal", new DialogInterface.OnClickListener() 
	         		{
	         			public void onClick(DialogInterface dialog, int which) 
	         			{   
	         				awal = id;
	         			}
	         		});	
	         		
	         		AlertDialog alertDialog1 = fakultasmarker.create();
	         		alertDialog1.show();
	         		return false; 
	         	}
	         	});				
	    }
	    
	    private void setRetainInstance(boolean b) {
			// TODO Auto-generated method stub
			
		}

		@Override
	    public void onRoutingFailure() {
	      // The Routing request failed
	    }

	    @Override
	    public void onRoutingStart() {
	      // The Routing Request starts
	    }

	    @Override
	    public void onRoutingSuccess(PolylineOptions mPolyOptions) {
	      
	      PolylineOptions polyoptions = new PolylineOptions();
	      polyoptions.color(Color.BLUE);
	      polyoptions.width(10);
	      polyoptions.addAll(mPolyOptions.getPoints());
	      map.addPolyline(polyoptions);
	      
	    }
	    @Override
		public void onLocationChanged(Location location) {       	
			double latitude = location.getLatitude();
			// Getting longitude of the current location
			double longitude = location.getLongitude();
			// Creating a LatLng object for the current location
			GPS = new LatLng(latitude, longitude);
	        //declare textview untuk menampilkan posisi
			TextView nowloc = (TextView) findViewById(R.id.location);
			nowloc.setText("Posisi saat ini: "+latitude+", "+longitude);
	       // TextView dest = (TextView) findViewById(R.id.next_dest);
	        if(latitude==lati[a] && longitude==longi[a])
	        {
	        	a++;
	        }
	    	
		}



		public void routeStart(){
	        Routing routing = new Routing(Routing.TravelMode.DRIVING);
	        routing.registerListener(this);
	        routing.execute(start, end);		
		}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
	    // do nothing.
	}
	
}
		                            