package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.marvel.consumer.FeingClient;
import com.marvel.dto.characters.response.Data;
import com.marvel.dto.characters.response.Response;
import com.marvel.dto.characters.response.Result;
import com.marvel.service.CharacterComicService;
import com.marvel.service.CharacterSerieService;
import com.marvel.service.CharacterService;
import com.marvel.service.CharacterStoryService;
import com.marvel.service.ComicService;
import com.marvel.service.InitService;
import com.marvel.service.SerieService;
import com.marvel.service.StoryService;
import com.marvel.service.StoryTypeService;

@Service
public class InitImpl implements InitService {
	
	@Autowired
	private FeingClient feingClient;
	@Autowired
	private CharacterService characterService;
	@Autowired
	private ComicService comicService;
	@Autowired
	private SerieService serieService;
	@Autowired
	private StoryService storyService;
	@Autowired
	private StoryTypeService storyTypeService;
	@Autowired
	private CharacterComicService characterComicService;
	@Autowired
	private CharacterSerieService characterSerieService;
	@Autowired
	private CharacterStoryService characterStoryService;
	@Autowired
	private Environment env;
	
	private static final String TS = "config.marvel-api.ts";
	private static final String PRIVATEKEY = "config.marvel-api.privatekey";
	private static final String PUBLICKEY = "config.marvel-api.publickey";
	private static final String LIMIT = "config.marvel-api.limit";
	private static final Logger LOG = LoggerFactory.getLogger(InitImpl.class);
	
	@Override
	@Async
	public Void init() {
		List<Response> responseList;
		String logMsg = "totalRecors: %s, count: %s, offset: %s";
		String log;
		try {
			responseList = getCharacterData();
			for(Response response: responseList) {
				Data data = response.getData();
				log = String.format(logMsg, data.getTotal(), data.getCount(), data.getOffset());
				LOG.info(log);
				List<Result> results = response.getData().getResults();
				LOG.info(results.toString());
				LOG.info("Saving characters...");
				characterService.saveCharacters(results);
				LOG.info("Saving comics...");
				comicService.saveComics(results);
				LOG.info("Saving series...");
				serieService.saveSeries(results);
				LOG.info("Saving story types...");
				storyTypeService.saveStoryType(results);
				LOG.info("Savin stories...");
				storyService.saveStory(results);
				LOG.info("Saving character comics...");
				characterComicService.saveCharacterComics(results);
				LOG.info("Saving character series...");
				characterSerieService.saveCharacterSeries(results);
				LOG.info("Saving character stories...");
				characterStoryService.saveCharacterStories(results);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Response> getCharacterData() throws Exception {
		List<Response> responseList;
		Response response;
		String logMsg = "publicKey: %s, ts: %s, hash: %s, limit: %s, offset: %s";
		String log;
		try {
			responseList = new ArrayList<Response>();
			String hash = createHash();
			Integer ts = Integer.parseInt(env.getProperty(TS));
			String publicKey = env.getProperty(PUBLICKEY);
			Integer limit = Integer.parseInt(env.getProperty(LIMIT));
			
			log = String.format(logMsg, publicKey, ts, hash , limit, 0);
			LOG.info(log);
			response = feingClient.getCharacters(publicKey, ts, hash , limit, 0);
			LOG.info(response.toString());
			
			
			responseList.add(response);
			Integer offset =  response.getData().getTotal() / limit;
			for(Integer i = 1; i < offset; i++) {
				log = String.format(logMsg, publicKey, ts, hash , limit, i);
				LOG.info(log);
				response = feingClient.getCharacters(publicKey, ts, hash , limit, i);
				LOG.info(response.toString());
				responseList.add(response);
			}
			
		} catch (Exception e) {
			throw e;
		}
		return responseList;
	}
	
	private String createHash() throws Exception {
		try {
			String hash = "%s%s%s";
			hash = String.format(hash, env.getProperty(TS), env.getProperty(PRIVATEKEY), env.getProperty(PUBLICKEY));
			hash = DigestUtils.md5Hex(hash);
			return hash;
		} catch (Exception e) {
			throw e;
		}
	}

}
