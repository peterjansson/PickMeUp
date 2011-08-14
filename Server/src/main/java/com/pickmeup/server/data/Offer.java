package com.pickmeup.server.data;

public class Offer {
	private final String taxiName;
	private final double distanceFromClientInMeters;
	private final long negotiationId;
	private Status status = Status.SUBMITTED;
	
	public Offer(long negotiationId, String taxiName, double distanceFromClientInMeters) {
		super();
		this.negotiationId = negotiationId;
		this.taxiName = taxiName;
		this.distanceFromClientInMeters = distanceFromClientInMeters;
	}

	public String getTaxiName() {
		return taxiName;
	}

	public double getDistanceFromClientInMeters() {
		return distanceFromClientInMeters;
	}

	public long getNegotiationId() {
		return negotiationId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
