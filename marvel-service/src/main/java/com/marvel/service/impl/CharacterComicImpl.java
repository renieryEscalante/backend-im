package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.CharacterComic;
import com.marvel.repository.CharacterComicRepository;
import com.marvel.service.CharacterComicService;
import com.marvel.service.CharacterService;
import com.marvel.service.ComicService;

@Service
public class CharacterComicImpl implements CharacterComicService {
	
	@Autowired
	private CharacterService characterService;
	@Autowired
	private ComicService comicService;
	@Autowired
	private CharacterComicRepository characterComicRepository;
	
	private static final String URI_COMIC = "http://gateway.marvel.com/v1/public/comics/";

	@Override
	public Void saveCharacterComics(List<Result> characters) {
		List<CharacterComic> detailEntityList = null;
		try {
			detailEntityList = builEntityList(characters);
			if(!detailEntityList.isEmpty()) {
				characterComicRepository.saveAll(detailEntityList);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private List<CharacterComic> builEntityList(List<Result> characters){
		List<CharacterComic> detailEntityList = null;
		try {
			detailEntityList = new ArrayList<>();
			
			// FILTRAMOS LOS CHARACTER QUE TIENE COMICS DISPONIBLES
			characters = characters
					.stream()
					.filter( it -> it.getComics().getAvailable() > 0)
					.collect(Collectors.toList());
			
			// RECORREMOS LOS CHARACTERS PARA CREAR EL DETALLE
			for(Result character: characters) {
				List<com.marvel.entity.Comic> comicsFromDataBase = null;
				List<Long> comicCodes = null;
				com.marvel.entity.Character characterFromDataBase = null;
				
				// OBTENEMOS EL CHARACTER DE BASE DE DATOS
				characterFromDataBase = characterService.findByCharacterCode(Long.parseLong(character.getId().toString()));
								
				if(!Objects.isNull(characterFromDataBase)) {
					comicCodes = character
							.getComics()
							.getItems()
							.stream()
							.map(it -> {
								String id = it.getResourceURI();
								id = id.replace(URI_COMIC, "");		
								return Long.parseLong(id);
							}).collect(Collectors.toList());
					
					// OBTENEMOS LOS COMICS DE LA BASE DE DATOS 
					comicsFromDataBase = comicService.findByComicCodeIn(comicCodes);
					
					// RECORREMOS LOS COMICS OBTENIDOS PARA CREAR LOS DETALLES
					for(com.marvel.entity.Comic comicFromDataBase: comicsFromDataBase) {
						if(!existInDataBase(comicFromDataBase, characterFromDataBase)) {
							CharacterComic characterComic = new CharacterComic();
							characterComic.setCharacter(characterFromDataBase);
							characterComic.setComic(comicFromDataBase);
							detailEntityList.add(characterComic);
						}
					}
				}
				
			}			
		} catch (Exception e) {
			throw e;
		}		
		return detailEntityList;
	}

	private Boolean existInDataBase(com.marvel.entity.Comic comic, com.marvel.entity.Character character) {
		try {
			return characterComicRepository.findByComicAndCharacter(comic, character).isPresent();
		} catch (Exception e) {
			throw e;
		}
	}
}
