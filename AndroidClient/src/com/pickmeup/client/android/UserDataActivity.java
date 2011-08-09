package com.pickmeup.client.android;

import com.pickmeup.client.android.data.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UserDataActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userdata);

		final Button button = (Button) findViewById(R.id.submitUserDataButton);
		button.setOnClickListener(this);
	}

	public void onClick(View v) {
		EditText userName = (EditText) findViewById(R.id.username);
		EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		if(userName.getText().length() > 0 && phoneNumber.getText().length() > 0) {
			DatabaseHandler databaseHandler = new DatabaseHandler(this);
			databaseHandler.open().storeUserData(userName.getText().toString(), phoneNumber.getText().toString());
			databaseHandler.close();
			
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
		}
		// TODO else, display error message "try again"
	}
}
