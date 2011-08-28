package com.pickmeup.driver.android;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class AndroidDriverActivity extends MapActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        CustomerOverlay itemizedoverlay = new CustomerOverlay(this.getResources().getDrawable(R.drawable.androidmarker), this);
        
        itemizedoverlay.addOverlay(new OverlayItem(new GeoPoint(19240000,-99120000), "Hola, Mundo!", "I'm in Mexico City!"));
        itemizedoverlay.addOverlay(new OverlayItem(new GeoPoint(35410000, 139460000), "Sekai, konichiwa!", "I'm in Japan!"));
        
        mapView.getOverlays().add(itemizedoverlay);
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}