package com.pickmeup.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.pickmeup.client.android.data.DatabaseHandler;

public class RequestTaxiActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		 TextView textView = (TextView) findViewById(R.id.userInfoText);
		 textView.setText(String.format("Logged in as %s", new DatabaseHandler(this).getUserData().getUserName()));
	}

	public void onClick(View v) {
		/*HttpClient httpclient = new DefaultHttpClient();
		try {
		     HttpPost request = new HttpPost("localhost:9999/query");
		     
		     Query query = getIntent().getParcelableExtra("query");
		     request.setEntity(new StringEntity(new Gson().toJson(query)));
		     ResponseHandler<String> handler = new BasicResponseHandler();  
		     try {  
		         String result = httpclient.execute(request, handler);  
		     } catch (ClientProtocolException e) {  
		         e.printStackTrace();  
		     } catch (IOException e) {  
		         e.printStackTrace();  
		     }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
		     httpclient.getConnectionManager().shutdown();
		}*/
	}
}
