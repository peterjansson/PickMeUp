package com.pickmeup.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pickmeup.client.android.data.DatabaseHandler;
import com.pickmeup.client.android.data.UserData;

public class AndroidClientActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

		// TODO just for testing purposes
//		new DatabaseHandler(this).deleteUserData();
		
		UserData userData = new DatabaseHandler(this).getUserData();
		if (userData != null) {
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, UserDataActivity.class);
			startActivity(intent);
		}
    }
}