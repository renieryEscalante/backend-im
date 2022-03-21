package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marvel.dto.characters.response.Item;
import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Story;
import com.marvel.entity.StoryType;
import com.marvel.repository.StoryRepository;
import com.marvel.service.StoryService;
import com.marvel.service.StoryTypeService;

@Service
public class StoryImpl implements StoryService {
	
	@Autowired
	private StoryRepository storyRepository;
	@Autowired
	private StoryTypeService storyTypeService;
	private List<StoryType> storyTypeList = null;
	
	private static final String URI_STORY = "http://gateway.marvel.com/v1/public/stories/";

	@Override
	@Transactional
	public Void saveStory(List<Result> characters) {
		List<Story> storyEntityList = null;
		try {
			storyEntityList = builStoryEntityList(characters);
			storyEntityList = filterList(storyEntityList);
			if(!storyEntityList.isEmpty()) {
				storyRepository.saveAll(storyEntityList);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private List<Story> builStoryEntityList(List<Result> characters){
		List<Story> storyEntityList = null;
		List<com.marvel.dto.characters.response.Story> stories = null;
		List<Item> itemStoryList = null;
		try {
			itemStoryList = new ArrayList<Item>();
			
			storyTypeList = storyTypeService.getAll();
			
			// FILTRAMOS LOS PERSONAJES QUE TIENEN STORY
			stories = characters
				.stream()
				.filter(it -> it.getStories().getAvailable() > 0)
				.map( it -> it.getStories())
				.collect(Collectors.toList());
			
			// RECORREMOS Y AGREGAMOS A UNA SOLA LISTA
			for(com.marvel.dto.characters.response.Story itemStory: stories) {
				itemStoryList.addAll(itemStory.getItems());
			}
			
			// RECORREMOS Y CONSTRUIMOS UNA LISTA DE ENTIDADES STORY
			storyEntityList = itemStoryList
				.stream()
				.distinct()
				.map(it -> {
					Story storyEntity = new Story();
					String id = it.getResourceURI();
					id = id.replace(URI_STORY, "");					
					storyEntity.setStoryCode(Long.parseLong(id));
					storyEntity.setName(it.getName());
					
					StoryType storyType = getStoryType(it.getType());
					storyEntity.setType(storyType);
					
					return storyEntity;
				}).collect(Collectors.toList());
			
		} catch (Exception e) {
			throw e;
		}		
		return storyEntityList;
	}
	
	private StoryType getStoryType(String StoryTypeName) {
		try {
			return storyTypeList
				.stream()
				.filter( storyType -> storyType.getName().equalsIgnoreCase(StoryTypeName))
				.findFirst()
				.orElse(null);
		} catch (Exception e) {
			throw e;
		}
	}

	private List<Story> filterList(List<Story> storyEntityList){
		List<Long> storyCodes = null;
		List<Story> storiesInDataBase = null;
		List<Story> storyListFiltered = null;
		List<Story> storyEntityListFiltered = null;
		try {
			storyListFiltered = new ArrayList<>();
			storyEntityListFiltered = new ArrayList<>();
			
			// OBTENEMOS TODOS LOS CODIGOS DE LOS STORY
			storyCodes = storyEntityList
					.stream()
					.map(it -> it.getStoryCode())
					.distinct()
					.collect(Collectors.toList());
			
			for(Long storyCode:storyCodes) {
				Story storyAux = storyEntityList
					.stream()
					.filter(it -> it.getStoryCode() == storyCode)
					.findFirst()
					.orElse(null);
				
				if(!Objects.isNull(storyAux)) {
					storyEntityListFiltered.add(storyAux);
				}
				
			}
			
			// BUSCAMOS EN BASE DE DATOS
			storiesInDataBase = storyRepository.findByStoryCodeIn(storyCodes);
			
			// REUTILIZAMOS LA VARIABLE Y OBTENEMOS LOS CODIGOS DE LOS REGISTROS QUE OBTUVIMOS DE LA BASE DE DATOS
			storyCodes = storiesInDataBase
					.stream()
					.map(it -> it.getStoryCode())
					.collect(Collectors.toList());
			
			// FILTRAMOS Y AGREGAMOS A UNA NUEVA LISTA LOS STORY QUE NO ESTAN EN BASE DE DATOS
			for(Story itemSerie: storyEntityListFiltered) {
				if(!storyCodes.contains(itemSerie.getStoryCode())) {
					storyListFiltered.add(itemSerie);
				}
			}			
	
		} catch (Exception e) {
			throw e;
		}
		return storyListFiltered;
	}

	
	@Override
	public List<Story> findByStoryCodes(List<Long> StoryCodes) {
		try {
			return storyRepository.findByStoryCodeIn(StoryCodes);
		} catch (Exception e) {
			throw e;
		}
	}
}
