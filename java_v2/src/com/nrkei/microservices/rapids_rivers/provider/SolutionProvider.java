package com.nrkei.microservices.rapids_rivers.provider;
/*
 * Copyright (c) 2016 by Fred George
 * May be used freely except for training; license required for training.
 * @author Fred George
 */

import java.util.ArrayList;
import java.util.List;

import com.nrkei.microservices.rapids_rivers.Packet;
import com.nrkei.microservices.rapids_rivers.PacketProblems;
import com.nrkei.microservices.rapids_rivers.RapidsConnection;
import com.nrkei.microservices.rapids_rivers.River;
import com.nrkei.microservices.rapids_rivers.rabbit_mq.RabbitMqRapids;

// Understands the messages on an event bus
public class SolutionProvider implements River.PacketListener {

	private RapidsConnection rapidsConnection = null;
	private River river = null;

	public SolutionProvider(RapidsConnection connection) {
		this.rapidsConnection = connection;
		//rapidsConnection = new RabbitMqRapids("solutionmonitor", host, port);
		//river = new River(rapidsConnection);
	}

	public static void main(String[] args) {
		String host = args[0];
		String port = args[1];

		final RapidsConnection rapidsConnection = new RabbitMqRapids("solutionProvider", host, port);
		final River river = new River(rapidsConnection);
		// See RiverTest for various functions River supports to aid in
		// filtering, like:
		river.requireValue("need", "car_rental_offer"); // Reject packet unless
														// it has
		// key:value pair
		// river.require("key1", "key2"); // Reject packet unless it has key1
		// and key2
		river.forbid("solutions"); // Reject packet if it does have key1
		river.forbid("source"); // Reject packet if it does have key1
		// or key2
		//river.interestedIn("offers"); // Allows key1 and key2 to be
		// queried and set in a packet
															// river to start
		// receiving traffic
		river.register(new SolutionProvider(rapidsConnection)); // Hook up to the
	}

	@Override
	public void packet(RapidsConnection connection, Packet packet, PacketProblems warnings) {
		System.out.println(String.format("SP: [*] %s", warnings));
		System.out.println(packet.toString());
		publishSolution(packet);
	}

	@Override
	public void onError(RapidsConnection connection, PacketProblems errors) {
		//System.out.println(String.format(" [x] %s", errors));
	}

	public void publishSolution(Packet packet) {
		publishSolution(packet, rapidsConnection);
	}

	public void publishSolution(Packet packet, RapidsConnection rapidsConnection) {
//		List<String> solutions = new ArrayList<>();
//		solutions.add("This is my first solution");
//		solutions.add("This is my second solution");
//		packet.put("solutions", solutions.toArray());
		packet.put("source", this.getClass().getSimpleName());
		packet.put("solutions", Offer.randomOffer());
		String jsonMessage = packet.toJson();
		System.out.println(String.format(" [<] %s", jsonMessage));
		rapidsConnection.publish(jsonMessage);
	}

}
