package com.pickmeup.driver.android;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomerOverlay extends ItemizedOverlay<OverlayItem> {

	private Context context;
	private List<OverlayItem> overlays = new ArrayList<OverlayItem>();

	public CustomerOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public CustomerOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int index) {
		if (index > overlays.size()) {
			Log.e(this.getClass().getName(), "No overlay with index " + index);
			return null;
		}
		return overlays.get(index);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}
}
