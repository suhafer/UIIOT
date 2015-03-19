package com.sumarnakreatip.uiiot;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("Instantiatable")
public class ProfilHttpClient {
	String et;
public ProfilHttpClient (String s){	
	et=s.toString();
}
//private static final String HOST = "10.0.2.2";
private static final String HOST = "green.ui.ac.id";
private final AsyncHttpClient client = new AsyncHttpClient();
public static class Product {
		public String npm;
		public String nama;
		public String role;
		/*public String jenis_kelamin;
		public String alamat;
		public String email;
		public String no_hp;
		public String no_telp;*/
}
public static class GetAllProducts{
		public int success;
		public String message;
		public Product[] result;
}

public interface Function<I,O>{
	O call(I input);
}

public void get_all_products (final Function<List<Product>,Void> callback){
System.out.println("masuk");
RequestParams params = new RequestParams("username", et);
client.post("http://"+HOST+"/nebeng/profil.php",params,new AsyncHttpResponseHandler()
{
	@Override
	public void onSuccess(String response){
		Gson g = new Gson();
		final GetAllProducts r = g.fromJson(response, GetAllProducts.class);
		final List<Product> list = Arrays.asList(r.result);
		callback.call(list);
		System.out.println(Arrays.asList(r.result));
	}
	
	
});

}

}