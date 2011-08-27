package com.pickmeup.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
		
		TextView confirmationMessage = (TextView) findViewById(R.id.confirmationMessage);
		confirmationMessage.setText("Negotation confirmed, taxi on it's way!");
	}
}
