package com.pickmeup.client.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.pickmeup.client.android.data.DatabaseHandler;
import com.pickmeup.client.android.data.Query;
import com.pickmeup.client.android.data.UserData;

public class LocationActivity extends Activity implements LocationListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.location);
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	public void onLocationChanged(Location location) {
		UserData userData = new DatabaseHandler(this).getUserData();
		Query query = new Query(userData.getUserName(), location.getLatitude(), location.getLongitude());
		
		Intent intent = new Intent(this, RequestTaxiActivity.class);
		intent.putExtra("query", query);

		startActivity(intent);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
