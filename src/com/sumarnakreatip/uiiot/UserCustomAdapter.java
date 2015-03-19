package com.sumarnakreatip.uiiot;

import java.util.ArrayList;
//import java.util.List;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UserCustomAdapter extends ArrayAdapter<User> {

	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	String usernametujuan, x, y, z, s;
	CharSequence text;
	int w;
	Context context;
	int layoutResourceId;
	ArrayList<User> a = new ArrayList<User>();
	String respon,et,id,regid;
	
	public UserCustomAdapter(Context context, int layoutResourceId,
			ArrayList<User> a) {
		super(context, layoutResourceId, a);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.a = a;
	}

	public void addUser(String username, String user_id, String npm, String nama, String asal,
			String tujuan, String kapasitas, String waktu_berangkat,
			String jam_berangkat,String keterangan,String reg)
	{
		this.et=username;
		this.regid=reg;
		a.add(new User(user_id, npm, nama, asal, tujuan, kapasitas,
				waktu_berangkat, jam_berangkat, keterangan));
		notifyDataSetChanged();
	}
	
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		UserHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new UserHolder();
			//holder.user_id = (TextView) row.findViewById(R.id.user_id);
			//holder.npm = (TextView) row.findViewById(R.id.npm);
			holder.nama = (TextView) row.findViewById(R.id.nama);
			holder.asal = (TextView) row.findViewById(R.id.asal);
			holder.tujuan = (TextView) row.findViewById(R.id.tujuan);
			//holder.kapasitas = (TextView) row.findViewById(R.id.kapasitas);
			//holder.waktu_berangkat = (TextView) row.findViewById(R.id.w_b);
			//holder.jam_berangkat = (TextView) row.findViewById(R.id.j_b);
			//holder.keterangan = (TextView) row.findViewById(R.id.k);
			row.setTag(holder);
		}
		else
		{
			holder = (UserHolder) row.getTag();
		}
		User user = a.get(position);
		//holder.user_id.setText(user.getuser_id());
		//holder.npm.setText(user.getnpm());
		holder.nama.setText(user.getnama());
		holder.asal.setText(user.getasal());
		holder.tujuan.setText(user.gettujuan());
		//holder.kapasitas.setText(user.getkapasitas());
		//holder.waktu_berangkat.setText(user.getwaktu_berangkat());
		//holder.jam_berangkat.setText(user.getjam_berangkat());
		//holder.keterangan.setText(user.getketerangan());
		
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(context, "Row Click " + a.get(position).user_id, Toast.LENGTH_LONG).show();
				//Log.d("MyDebug", "Row Click " + position);
				id =  a.get(position).user_id;
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
			    alert.setTitle("Detail Tebengan");
			    alert.setMessage("Nama: "+a.get(position).nama
			    		+"\nNPM: "+a.get(position).npm
			    		+"\nAsal: "+a.get(position).asal
			    		+"\nTujuan: "+a.get(position).tujuan
			    		+"\nKuota: "+a.get(position).kapasitas
			    		+"\nBerangkat: "+a.get(position).waktu_berangkat
			    		+"\nJam: "+a.get(position).jam_berangkat
			    		+"\nCatatan: "+a.get(position).keterangan
			    		+"\nApakah Anda ingin Menebeng Tebengan Ini ?");

			    alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
			            //Your action here
						loggin asyncRate = new loggin(); asyncRate.execute();
		        }
			    });

			    alert.setNegativeButton("Tidak",
			        new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int whichButton) {
			            }
			        });

			    alert.show();
			}
		});
		
		
		return row;

	}


	static class UserHolder {
		TextView user_id, npm, nama, asal, tujuan, kapasitas, waktu_berangkat,
				jam_berangkat,keterangan;

		// Button nebeng,konfirmasi,batal;
	}
	private class loggin extends AsyncTask<Void, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(context, "", "Proccessing...", true);
		}

		@Override
		protected String doInBackground(Void... params) {

			String respo = login();

			return respo;
		}

		@Override
		protected void onPostExecute(String rate) {
			// Do whatever you need with the string, you can update your UI from
			// here
			respon = rate;
			if (respon.equalsIgnoreCase("Nebeng Sukses")) {
				//setstatus(1);
				Toast.makeText(context, "Nebeng Sukses", Toast.LENGTH_LONG).show();
				int value=0;
				Intent intent = new Intent(context, Home.class);
				intent.putExtra("username", et);
				intent.putExtra("Map", value);
				intent.putExtra("regid", regid);
				context.startActivity(intent);
                ((Activity) context).finish();
			} else if (respon
					.equalsIgnoreCase("Maaf id ini tidak membuka tebengan")) {
				//setstatus(3);
				Toast.makeText(context, "Maaf id ini tidak membuka tebengan", Toast.LENGTH_LONG).show();
			} else {
				//setstatus(2);
				Toast.makeText(context, "Nebeng Tidak Berhasil. Cek status anda dan kuota tebengan.", Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();

		}
	}

	String login() {
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
			nameValuePairs.add(new BasicNameValuePair("username", et.toString()
					.trim())); // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("id_tebengan",  id.toString().trim())); // $Edittext_value =
													// $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("update", "nebeng"));
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

}