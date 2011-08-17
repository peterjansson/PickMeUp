package com.pickmeup.client.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pickmeup.client.android.data.DatabaseHandler;
import com.pickmeup.client.android.data.Negotation;
import com.pickmeup.client.android.data.Query;

public class RequestTaxiActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		TextView textView = (TextView) findViewById(R.id.userInfoText);
		textView.setText(String.format("Logged in as %s", new DatabaseHandler(
				this).getUserData().getUserName()));

		Button button = (Button) findViewById(R.id.pickMeUpQueryButton);
		button.setOnClickListener(this);
	}

	public void onClick(View v) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(
					"http://10.0.2.2:8080/Server/query");

			try {
				Query query = getIntent().getParcelableExtra("query");
				post.setEntity(new StringEntity(new Gson().toJson(query)));

				String response = httpclient.execute(post, new BasicResponseHandler());
				
				Negotation negotation = new Gson().fromJson(response, Negotation.class);

				Intent intent = new Intent(this, FetchOffersActivity.class);
				intent.putExtra("negotiationId", negotation.getId());
				startActivity(intent);
			} catch (ClientProtocolException e) {
				Log.e(this.getClass().getName(), "Error sending request", e);
				alertUser(e.getMessage());
			} catch (IOException e) {
				Log.e(this.getClass().getName(), "Error sending request", e);
				alertUser(e.getMessage());
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	private void alertUser(String s) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(s)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// RequestTaxiActivity.this.finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
