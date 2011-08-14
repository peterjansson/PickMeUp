package com.pickmeup.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

public class FetchOffersActivity extends Activity {
	public void onClick(View v) {
		setContentView(R.layout.offers);

		Long negotiationId = (Long) getIntent().getExtras().get("negotiationId");
		TextView negotiationIdText = (TextView) findViewById(R.id.negotiationIdText);
		negotiationIdText.setText("Negotiation id: "+negotiationId);
		/*HttpClient httpclient = new DefaultHttpClient();
		try {
		     HttpPost post = new HttpPost("http://10.0.2.2:8080/Server/query/%s.json");
		     
		     Query query = getIntent().getParcelableExtra("query");
		     post.setEntity(new StringEntity(new Gson().toJson(query)));
		     ResponseHandler<String> handler = new BasicResponseHandler();  
		     try {  
		         Object result = httpclient.execute(post, handler); 
		         alertUser(result.toString());
		     } catch (ClientProtocolException e) { 
		    	 Log.e(this.getClass().getName(), "Error sending request", e);
		    	 alertUser(e.getMessage());
		     } catch (IOException e) {  
		    	 Log.e(this.getClass().getName(), "Error sending request", e);
		    	 alertUser(e.getMessage());
		     }
		} catch (UnsupportedEncodingException e) {
			Log.e(this.getClass().getName(), "Error sending request", e);
			alertUser(e.getMessage());
		} finally {
		     httpclient.getConnectionManager().shutdown();
		}*/
	}
	
	private void alertUser(String s) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(s)
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}
