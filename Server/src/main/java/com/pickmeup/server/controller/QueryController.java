package com.pickmeup.server.controller;

import static com.google.common.collect.Iterables.filter;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.pickmeup.server.data.Negotation;
import com.pickmeup.server.data.Offer;
import com.pickmeup.server.data.Status;

@Controller
public class QueryController {
	private Multimap<Long, Offer> demoOffers = LinkedListMultimap.create();
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
    public Negotation initiateNegotation(@RequestBody String input)
    {
		System.out.println(input);
		long negotiationId = new Random().nextLong();
		demoOffers.put(negotiationId, new Offer(negotiationId, "Minitaxi", 5400d));
		return new Negotation(negotiationId);
    }
	
	@RequestMapping(value = "/query/{negotationId}", method = RequestMethod.POST)
	public Iterable<Offer> queryForOffers(@PathVariable long negotiationId) {
		return getSubmittedOffersForNegotiation(negotiationId);
	}
	
	private Iterable<Offer> getSubmittedOffersForNegotiation(final long negotiationId) {
		return filter(demoOffers.get(negotiationId), new Predicate<Offer>() {
			@Override
			public boolean apply(Offer input) {
				return input.getStatus() == Status.SUBMITTED;
			}
		});
	}
}
