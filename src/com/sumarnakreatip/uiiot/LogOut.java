package com.sumarnakreatip.uiiot;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LogOut extends Fragment {

	ImageView ivIcon;
	TextView tvItemName;

	public static final String IMAGE_RESOURCE_ID = "iconResourceID";
	public static final String ITEM_NAME = "itemName";
	
	private ProgressBar mProgress;
    private int mProgressStatus = 0;

	public LogOut() {

	}
	private Handler mHandler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.logout, container,
				false);
		
		mProgress = (ProgressBar) view.findViewById(R.id.progress);
		new Thread(new Runnable() {
            public void run() {
            	while (mProgressStatus < 100) {
                        mProgressStatus = mProgressStatus + doWork();
                        // Update the progress bar
                        mHandler.post(new Runnable() {
                            public void run() {
                                mProgress.setProgress(mProgressStatus);
                            }
                        });
                    }
            }
        }).start();
		return view;
	}
	
	public int doWork(){
    	int a=0,b =0;
    	while (a<=1000000){
    		a++;
    	}
    	b++;
    	return b;
    }
}
