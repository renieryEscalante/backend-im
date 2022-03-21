package com.marvel.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.marvel.dto.characters.response.Response;

@FeignClient(name = "marvel", url = "${data-access.marvel-url}")
public interface FeingClient {

	@RequestMapping(method = RequestMethod.GET, value = "/characters")
	Response getCharacters(@RequestParam("apikey") String apikey, @RequestParam("ts") Integer ts,
			@RequestParam("hash") String hash, @RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset);
}
