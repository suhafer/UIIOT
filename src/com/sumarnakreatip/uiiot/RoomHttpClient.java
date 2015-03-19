package com.sumarnakreatip.uiiot;

import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RoomHttpClient {
	
	private static final String HOST = "green.ui.ac.id";
	private final AsyncHttpClient client = new AsyncHttpClient();
	
	//kelas untuk isi data
	public static class Product {
			public String user_id;
			public String npm;
			public String nama;
			public String asal;
			public String tujuan;
			public String kapasitas;
			public String waktu_berangkat;
			public String jam_berangkat;
			public String keterangan;
	}
	
	//kelas paket data
	public static class GetAllProducts{
			public String success;
			public String message;
			public Product[] result;
	}
	
	//fungsi untuk pengambilan data
	public interface Function<I,S,O>{
		O call(I input,S sukses);
	}
	
	//method untuk pengambilan data dari server
	public void get_all_products (final Function<List<Product>,String,Void> callback){
		System.out.println("masuk");
		//client.get("http://"+HOST+"/nebeng/room_beri_tebengan.php", new AsyncHttpResponseHandler()
		RequestParams params = new RequestParams("kode", "1");
		client.post("http://"+HOST+"/nebeng/room_beri_tebengan.php", params,new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String response){
				Gson g = new Gson();
				final GetAllProducts r = g.fromJson(response, GetAllProducts.class);
				final List<Product> list = Arrays.asList(r.result);
				final String sukse = (String) r.success; 
				callback.call(list,sukse);
				System.out.println(Arrays.asList(r.result));
			}
		
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				System.out.println("gagal "+arg0);
				arg3.printStackTrace();
			}
		
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				System.out.println("selesai");
			}
		
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				System.out.println("start");
			}
			
		});
	}
}