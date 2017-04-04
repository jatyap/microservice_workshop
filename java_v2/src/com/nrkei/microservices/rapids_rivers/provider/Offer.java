package com.nrkei.microservices.rapids_rivers.provider;

import java.util.Random;

public class Offer {

	private final String offerId;
	private final int limit;
	
	public static Offer randomOffer(){
		Random gen = new Random();
		String randomId = "" + gen.nextInt(5000);
		Offer newOffer = new Offer(randomId);
		newOffer.setValue(new Double(new Random().nextInt(100)));
		newOffer.setLikelihood(new Double(new Random().nextInt(100)));
		return newOffer;
	}
	
	public static Offer limitedOffer(int limit){
		Random gen = new Random();
		String randomId = "" + gen.nextInt(5000);
		Offer newOffer = new Offer(randomId);
		newOffer.setValue(new Double(
				new Random().nextInt(limit) / new Random().nextInt(100)));
		newOffer.setLikelihood(new Double(new Random().nextInt(limit) / 100));
		return newOffer;
	}

	public Offer(String id){
		this(id, 100);
	}
	
	public Offer(String id, int limit){
		this.offerId = id;
		this.limit = limit;
	}
	private double value;
	private double likelihood;
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getLikelihood() {
		return likelihood;
	}
	public void setLikelihood(double likelihood) {
		if(likelihood > 100){
			likelihood = 100;
		}
		this.likelihood = likelihood;
	}

	public int getLimit() {
		return limit;
	}
}
