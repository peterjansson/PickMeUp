package com.pickmeup.server.controller;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.pickmeup.server.data.Negotation;
import com.pickmeup.server.data.Offer;
import com.pickmeup.server.data.Status;

@Controller
public class QueryController {
	private static final Multimap<Long, Offer> demoOffers = LinkedListMultimap.create();
	private static final Logger LOG = Logger.getLogger(QueryController.class);
	private static final Random random = new Random();
	
	private void initiateDemoOffers(long negotiationId) {
		demoOffers.put(negotiationId, new Offer(random.nextLong(), negotiationId, "Minitaxi", 5400d));
		
		demoOffers.put(negotiationId, new Offer(random.nextLong(), negotiationId, "Taxi kurir", 2400d));
		
		demoOffers.put(negotiationId, new Offer(random.nextLong(), negotiationId, "020", 5900d));
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
    public void initiateNegotation(@RequestBody String input, HttpServletResponse response)
    {
		long negotiationId = random.nextLong();
		initiateDemoOffers(negotiationId);
		sendAsJson(new Negotation(negotiationId), response);
    }
	
	@RequestMapping(value = "/query/offers/{negotiationId}", method = RequestMethod.GET)
	public void queryForOffers(@PathVariable String negotiationId, HttpServletResponse response) {
		List<Offer> submittedOffersForNegotiation = getSubmittedOffersForNegotiation(Long.parseLong(negotiationId));
		LOG.debug(format("Querying for offers associated with id %s, found %s offer(s)", negotiationId, size(submittedOffersForNegotiation)));
		sendAsJson(submittedOffersForNegotiation, response);
		for (Offer offer : submittedOffersForNegotiation) {
			offer.setStatus(Status.OFFERED);
		}
	}
	
	@RequestMapping(value = "/query/offers/accept/{negotiationId}/{offerId}", method = RequestMethod.PUT)
	public void acceptOffer(@PathVariable final String negotiationId, @PathVariable final String offerId, final HttpServletResponse response) {
		Collection<Offer> collection = demoOffers.get(Long.parseLong(negotiationId));
		Offer acceptedOffer = Iterables.find(collection, new Predicate<Offer>() {
			@Override
			public boolean apply(Offer input) {
				return input.getId() == Long.parseLong(offerId);
			}
			
		}, null);
		demoOffers.put(Long.parseLong(negotiationId), null);
		LOG.info(String.format("Accepted offer %s for negotiation %s (%s)", offerId, negotiationId, acceptedOffer.getTaxiName()));
	}
	
	private List<Offer> getSubmittedOffersForNegotiation(final long negotiationId) {
		return newArrayList(filter(demoOffers.get(negotiationId), new Predicate<Offer>() {
			@Override
			public boolean apply(Offer input) {
				return input.getStatus() == Status.SUBMITTED;
			}
		}));
	}
	
	private void sendAsJson(Object obj, HttpServletResponse response) {
		try {
			response.getOutputStream().print(new Gson().toJson(obj));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
