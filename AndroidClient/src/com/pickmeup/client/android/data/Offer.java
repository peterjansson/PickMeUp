package com.pickmeup.client.android.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {
	private long id;
	private String taxiName;
	private double distanceFromClientInMeters;
	private long negotiationId;
	
	public String getTaxiName() {
		return taxiName;
	}
	public void setTaxiName(String taxiName) {
		this.taxiName = taxiName;
	}
	public double getDistanceFromClientInMeters() {
		return distanceFromClientInMeters;
	}
	public void setDistanceFromClientInMeters(double distanceFromClientInMeters) {
		this.distanceFromClientInMeters = distanceFromClientInMeters;
	}
	public long getNegotiationId() {
		return negotiationId;
	}
	public void setNegotiationId(long negotiationId) {
		this.negotiationId = negotiationId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(taxiName);
		dest.writeDouble(distanceFromClientInMeters);
		dest.writeLong(negotiationId);
	}
	public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
		@Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }
		@Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
	private Offer(Parcel in) {
		id = in.readLong();
		taxiName = in.readString();
		distanceFromClientInMeters = in.readDouble();
		negotiationId = in.readLong();
	}
}
