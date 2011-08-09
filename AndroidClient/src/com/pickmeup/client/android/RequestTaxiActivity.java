package com.pickmeup.client.android;

import com.pickmeup.client.android.data.DatabaseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RequestTaxiActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		 TextView textView = (TextView) findViewById(R.id.userInfoText);
		 textView.setText(String.format("Logged in as %s", new DatabaseHandler(this).getUserData().getUserName()));
	}
}
