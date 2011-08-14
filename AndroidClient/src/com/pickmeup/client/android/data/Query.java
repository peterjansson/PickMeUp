package com.pickmeup.client.android.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Query implements Parcelable {
	private final String userName;
	private final double latitude;
	private final double longitude;
	
	public Query(String userName, double latitude, double longitude) {
		this.userName = userName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getUserName() {
		return userName;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeDoubleArray(new double[] {latitude, longitude});
	}
	
	public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        public Query createFromParcel(Parcel in) {
            return new Query(in);
        }

        public Query[] newArray(int size) {
            return new Query[size];
        }
    };

	
	private Query(Parcel in) {
		userName = in.readString();
		double[] result = new double[2];
		in.readDoubleArray(result);
		latitude = result[0];
		longitude = result[1];
	}
}
