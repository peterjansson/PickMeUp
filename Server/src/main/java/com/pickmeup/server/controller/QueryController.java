package com.pickmeup.server.controller;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.io.IOException;
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
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.pickmeup.server.data.Negotation;
import com.pickmeup.server.data.Offer;
import com.pickmeup.server.data.Status;

@Controller
public class QueryController {
	private Multimap<Long, Offer> demoOffers = LinkedListMultimap.create();
	private static final Logger LOG = Logger.getLogger(QueryController.class);
	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
    public void initiateNegotation(@RequestBody String input, HttpServletResponse response)
    {
		long negotiationId = new Random().nextLong();
		LOG.info("Initiating negotiation with id "+negotiationId);
		demoOffers.put(negotiationId, new Offer(negotiationId, "Minitaxi", 5400d));
		
		sendAsJson(new Negotation(negotiationId), response);
    }
	
	@RequestMapping(value = "/query/offers/{negotiationId}", method = RequestMethod.GET)
	public void queryForOffers(@PathVariable String negotiationId, HttpServletResponse response) {
		List<Offer> submittedOffersForNegotiation = getSubmittedOffersForNegotiation(Long.parseLong(negotiationId));
		LOG.debug(format("Querying for offers associated with id %s, found %s offer(s)", negotiationId, size(submittedOffersForNegotiation)));
		sendAsJson(submittedOffersForNegotiation, response);
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
