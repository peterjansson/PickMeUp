package com.pickmeup.client.android;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pickmeup.client.android.data.Offer;
import com.pickmeup.client.android.data.Query;

public class FetchOffersActivity extends Activity implements OnClickListener {

	private boolean threadActive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offers);

		final Long negotiationId = (Long) getIntent().getExtras()
				.get("negotiationId");
		TextView negotiationIdText = (TextView) findViewById(R.id.negotiationIdText);
		negotiationIdText.setText("Negotiation id: " + negotiationId);

		final Queue<OfferViewRow> viewRows = setupViewRows();
		addClickListener(viewRows);
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				int size = msg.getData().getInt("size");
				for(int i = 0;i<size;i++) {
					Offer offer = msg.getData().getParcelable(Integer.toString(i));
					Log.i(FetchOffersActivity.class.getName(), offer.getTaxiName());
					OfferViewRow viewRow = viewRows.poll();
					if(viewRow == null) {
						threadActive = false;
						break;
					}
					else {
						viewRow.getButton().setVisibility(View.VISIBLE);
						viewRow.getTextView().setVisibility(View.VISIBLE);
						viewRow.getTextView().setText(String.format("%s (%s meter away)", offer.getTaxiName(), offer.getDistanceFromClientInMeters()));
						
						viewRow.getButton().setTag(offer);
					}
				}
			}
		};
		
		Thread fetchOfferThread = new Thread() {
			@Override
			public void run() {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpGet get = new HttpGet(format(
							"http://10.0.2.2:8080/Server/query/offers/%s",
							negotiationId));
					threadActive = true;
					while(threadActive) {
						try {
							List<Offer> result = httpclient.execute(get, new OfferResponseHandler());
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putInt("size", result.size());
							for (int i = 0;i<result.size();i++) {
								bundle.putParcelable(Integer.toString(i), result.get(i));
							}
							message.setData(bundle);
							handler.sendMessage(message);
						} catch (ClientProtocolException e) {
							Log.e(this.getClass().getName(), "Error sending request", e);
						} catch (IOException e) {
							Log.e(this.getClass().getName(), "Error sending request", e);
						}
						Thread.sleep(1000);
					}
					Log.d(FetchOffersActivity.class.getName(), "Stopping thread loop");
				} catch (InterruptedException e) {
					throw new RuntimeException("Thread interrupted");
				} finally {
					httpclient.getConnectionManager().shutdown();
				}
			}
		};
		
		fetchOfferThread.start();
	}

	private void addClickListener(Queue<OfferViewRow> viewRows) {
		for (OfferViewRow offerViewRow : viewRows) {
			offerViewRow.getButton().setOnClickListener(this);
		}
	}

	private Queue<OfferViewRow> setupViewRows() {
		// has to be done for each available offer
		// TODO is there any better/more dynamic way to do this?

		Queue<OfferViewRow> result = new ArrayBlockingQueue<OfferViewRow>(2);
		
		Button acceptOfferButton1 = (Button) findViewById(R.id.acceptOfferButton1);
		Button acceptOfferButton2 = (Button) findViewById(R.id.acceptOfferButton2);
		
		TextView offerText1 = (TextView) findViewById(R.id.offerText1);
		TextView offerText2 = (TextView) findViewById(R.id.offerText2);
		
		result.add(new OfferViewRow(acceptOfferButton1, offerText1));
		result.add(new OfferViewRow(acceptOfferButton2, offerText2));
		
		return result;
	}

	@Override
	public void onClick(View v) {
		HttpClient httpclient = new DefaultHttpClient();
		Offer offer = (Offer) v.getTag();
		HttpPut put = new HttpPut(
				format("http://10.0.2.2:8080/Server/query/offers/accept/%s/%s", offer.getNegotiationId(), offer.getId()));

		try {
			Query query = getIntent().getParcelableExtra("query");
			put.setEntity(new StringEntity(new Gson().toJson(query)));

			httpclient.execute(put, new BasicResponseHandler());
			
			Intent intent = new Intent(this, ConfirmationActivity.class);
			startActivity(intent);
		} catch (ClientProtocolException e) {
			Log.e(this.getClass().getName(), "Error sending request", e);
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "Error sending request", e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		threadActive = false;
	}

	private static class OfferResponseHandler implements ResponseHandler<List<Offer>> {
		private static final Type type = new TypeToken<List<Offer>>() {}.getType();

		@Override
		public List<Offer> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			return new Gson().fromJson(new InputStreamReader(response.getEntity().getContent()), type);
		}
		
	}
	
	private static class OfferViewRow {
		private final TextView textView;
		private final Button button;

		private OfferViewRow(Button button, TextView textView) {
			this.button = button;
			this.textView = textView;
		}

		public TextView getTextView() {
			return textView;
		}

		public Button getButton() {
			return button;
		}
	}
}
