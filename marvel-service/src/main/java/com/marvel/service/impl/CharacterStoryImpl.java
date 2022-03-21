package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.CharacterStory;
import com.marvel.repository.CharacterStoryRepository;
import com.marvel.service.CharacterService;
import com.marvel.service.CharacterStoryService;
import com.marvel.service.StoryService;

@Service
public class CharacterStoryImpl implements CharacterStoryService {
	
	@Autowired
	private CharacterService characterService;
	@Autowired
	private StoryService storyService;
	@Autowired
	private CharacterStoryRepository characterStoryRepository;
	
	private static final String URI_STORY = "http://gateway.marvel.com/v1/public/stories/";
	
	@Override
	public Void saveCharacterStories(List<Result> characters) {
		List<CharacterStory> detailEntityList = null;
		try {
			detailEntityList = builEntityList(characters);
			if(!detailEntityList.isEmpty()) {
				characterStoryRepository.saveAll(detailEntityList);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private List<CharacterStory> builEntityList(List<Result> characters){
		List<CharacterStory> detailEntityList = null;
		try {
			detailEntityList = new ArrayList<>();
			
			// FILTRAMOS LOS CHARACTER QUE TIENE COMICS DISPONIBLES
			characters = characters
					.stream()
					.filter( it -> it.getStories().getAvailable() > 0)
					.collect(Collectors.toList());
			
			// RECORREMOS LOS CHARACTERS PARA CREAR EL DETALLE
			for(Result character: characters) {
				List<com.marvel.entity.Story> storiesFromDataBase = null;
				List<Long> storyCodes = null;
				com.marvel.entity.Character characterFromDataBase = null;
				
				// OBTENEMOS EL CHARACTER DE BASE DE DATOS
				characterFromDataBase = characterService.findByCharacterCode(Long.parseLong(character.getId().toString()));
								
				if(!Objects.isNull(characterFromDataBase)) {
					storyCodes = character
							.getStories()
							.getItems()
							.stream()
							.map(it -> {
								String id = it.getResourceURI();
								id = id.replace(URI_STORY, "");		
								return Long.parseLong(id);
							}).collect(Collectors.toList());
					
					// OBTENEMOS LAS STORY DE LA BASE DE DATOS 
					storiesFromDataBase = storyService.findByStoryCodes(storyCodes);
					
					// RECORREMOS LOS STORY OBTENIDOS PARA CREAR LOS DETALLES
					for(com.marvel.entity.Story storyFromDataBase: storiesFromDataBase) {
						if(!existInDataBase(storyFromDataBase, characterFromDataBase)) {
							CharacterStory characterStory = new CharacterStory();
							characterStory.setCharacter(characterFromDataBase);
							characterStory.setStory(storyFromDataBase);
							detailEntityList.add(characterStory);
						}
					}
				}
				
			}			
		} catch (Exception e) {
			throw e;
		}		
		return detailEntityList;
	}
	
	public Boolean existInDataBase(com.marvel.entity.Story story, com.marvel.entity.Character character) {
		try {
			return characterStoryRepository.findByCharacterAndStory(character, story).isPresent();
		} catch (Exception e) {
			throw e;
		}
	}

}
