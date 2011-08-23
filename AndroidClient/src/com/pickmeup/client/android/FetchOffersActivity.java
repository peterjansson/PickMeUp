package com.pickmeup.client.android;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FetchOffersActivity extends Activity implements OnClickListener {

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offers);

		Long negotiationId = (Long) getIntent().getExtras()
				.get("negotiationId");
		TextView negotiationIdText = (TextView) findViewById(R.id.negotiationIdText);
		negotiationIdText.setText("Negotiation id: " + negotiationId);

		FetchOfferTask fetchOfferTask = new FetchOfferTask(negotiationId);
		timer = new Timer("FetchOfferTaskExecutor");
		// poll every 10 seconds
		timer.scheduleAtFixedRate(fetchOfferTask, 0, 1000 * 10);

		// has to be done for each available offer
		// TODO is there any better/more dynamic way to do this?
		Button acceptButton1 = (Button) findViewById(R.id.acceptOfferButton1);
		Button acceptButton2 = (Button) findViewById(R.id.acceptOfferButton2);
		acceptButton1.setOnClickListener(this);
		acceptButton2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

	private class FetchOfferTask extends TimerTask {

		private final long negotiationId;

		private FetchOfferTask(final long negotiationId) {
			this.negotiationId = negotiationId;
		}

		@Override
		public void run() {
			Log.i("tag", "thread start");
			Looper.prepare();
			HttpClient httpclient = new DefaultHttpClient();
			try {
				HttpGet get = new HttpGet(format(
						"http://10.0.2.2:8080/Server/query/offers/%s",
						negotiationId));

				ResponseHandler<String> handler = new BasicResponseHandler();
				
				try {
					Object result = httpclient.execute(get, handler);
					Log.i("tag", result.toString());
				} catch (ClientProtocolException e) {
					Log.e(this.getClass().getName(), "Error sending request", e);
					alertUser(e.getMessage());
				} catch (IOException e) {
					Log.e(this.getClass().getName(), "Error sending request", e);
					alertUser(e.getMessage());
				}
			} finally {
				Looper.myLooper().quit();
				httpclient.getConnectionManager().shutdown();
			}
			Looper.loop();
		}

	}

	private void alertUser(String s) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(s).setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// RequestTaxiActivity.this.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
