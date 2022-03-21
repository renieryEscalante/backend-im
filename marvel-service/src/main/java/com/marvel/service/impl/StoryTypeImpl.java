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
import com.marvel.entity.StoryType;
import com.marvel.repository.StoryTypeRepository;
import com.marvel.service.StoryTypeService;

@Service
public class StoryTypeImpl implements StoryTypeService {
	
	@Autowired
	private StoryTypeRepository storyTypeRepository;
	
	@Override
	@Transactional
	public Void saveStoryType(List<Result> characters) {
		List<StoryType> storyEntityList = null;
		try {
			storyEntityList = builStoryTypeEntityList(characters);
			storyEntityList = filterList(storyEntityList);
			if(!storyEntityList.isEmpty()) {
				storyTypeRepository.saveAll(storyEntityList);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private List<StoryType> builStoryTypeEntityList(List<Result> characters){
		List<StoryType> storyTypeEntityList = null;
		List<com.marvel.dto.characters.response.Story> stories = null;
		List<Item> itemStoryList = null;
		try {
			itemStoryList = new ArrayList<Item>();
			
			// FILTRAMOS LOS PERSONAJES QUE TIENEN STORIES
			stories = characters
				.stream()
				.filter(it -> it.getStories().getAvailable() > 0)
				.map( it -> it.getStories())
				.collect(Collectors.toList());
			
			// RECORREMOS Y AGREGAMOS A UNA SOLA LISTA
			for(com.marvel.dto.characters.response.Story itemStory: stories) {
				itemStoryList.addAll(itemStory.getItems());
			}
			
			// RECORREMOS Y CONSTRUIMOS UNA LISTA DE ENTIDADES STORY TYPES
			storyTypeEntityList = itemStoryList
				.stream()
				.map(it -> it.getType())
				.filter(it -> !Objects.isNull(it))
				.filter(it -> !it.equals(""))
				.distinct()
				.map(it -> {
					StoryType storyTypeEntity = new StoryType();
					storyTypeEntity.setName(it);
					
					return storyTypeEntity;
				}).collect(Collectors.toList());
			
		} catch (Exception e) {
			throw e;
		}		
		return storyTypeEntityList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoryType> getAll() {
		try {
			return storyTypeRepository.findAll();
		} catch (Exception e) {
			throw e;
		}
	}

	private List<StoryType> filterList(List<StoryType> storyEntityList){
		List<String> storyTypeNames = null;
		List<StoryType> storyTypesInDataBase = null;
		List<StoryType> storyTypeListFiltered = null;
		try {
			storyTypeListFiltered = new ArrayList<>();
			
			// OBTENEMOS TODOS LOS NOMBRES DE LOS STORY TYPE
			storyTypeNames = storyEntityList
					.stream()
					.map(it -> it.getName())
					.collect(Collectors.toList());
			
			// BUSCAMOS EN BASE DE DATOS
			storyTypesInDataBase = storyTypeRepository.findByNameIn(storyTypeNames);
			
			// REUTILIZAMOS LA VARIABLE Y OBTENEMOS LOS NOMBRES DE LOS REGISTROS QUE OBTUVIMOS DE LA BASE DE DATOS
			storyTypeNames = storyTypesInDataBase
					.stream()
					.map(it -> it.getName())
					.collect(Collectors.toList());
			
			// FILTRAMOS Y AGREGAMOS A UNA NUEVA LISTA LOS STORY TYPES QUE NO ESTAN EN BASE DE DATOS
			for(StoryType itemSerie: storyEntityList) {
				if(!storyTypeNames.contains(itemSerie.getName())) {
					storyTypeListFiltered.add(itemSerie);
				}
			}			
	
		} catch (Exception e) {
			throw e;
		}
		return storyTypeListFiltered;
	}
}
