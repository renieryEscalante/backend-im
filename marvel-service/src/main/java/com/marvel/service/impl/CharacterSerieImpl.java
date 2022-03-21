package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.CharacterSerie;
import com.marvel.repository.CharacterSerieRepository;
import com.marvel.service.CharacterSerieService;
import com.marvel.service.CharacterService;
import com.marvel.service.SerieService;

@Service
public class CharacterSerieImpl implements CharacterSerieService {
	
	@Autowired
	private CharacterService characterService;
	@Autowired
	private SerieService serieService;
	@Autowired
	private CharacterSerieRepository characterSerieRepository;
	
	private static final String URI_SERIE = "http://gateway.marvel.com/v1/public/series/";
	
	@Override
	public Void saveCharacterSeries(List<Result> characters) {
		List<CharacterSerie> detailEntityList = null;
		try {
			detailEntityList = builEntityList(characters);
			if(!detailEntityList.isEmpty()) {
				characterSerieRepository.saveAll(detailEntityList);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private List<CharacterSerie> builEntityList(List<Result> characters){
		List<CharacterSerie> detailEntityList = null;
		try {
			detailEntityList = new ArrayList<>();
			
			// FILTRAMOS LOS CHARACTER QUE TIENE COMICS DISPONIBLES
			characters = characters
					.stream()
					.filter( it -> it.getSeries().getAvailable() > 0)
					.collect(Collectors.toList());
			
			// RECORREMOS LOS CHARACTERS PARA CREAR EL DETALLE
			for(Result character: characters) {
				List<com.marvel.entity.Serie> seriesFromDataBase = null;
				List<Long> serieCodes = null;
				com.marvel.entity.Character characterFromDataBase = null;
				
				// OBTENEMOS EL CHARACTER DE BASE DE DATOS
				characterFromDataBase = characterService.findByCharacterCode(Long.parseLong(character.getId().toString()));
								
				if(!Objects.isNull(characterFromDataBase)) {
					serieCodes = character
							.getSeries()
							.getItems()
							.stream()
							.map(it -> {
								String id = it.getResourceURI();
								id = id.replace(URI_SERIE, "");		
								return Long.parseLong(id);
							}).collect(Collectors.toList());
					
					// OBTENEMOS LAS SERIES DE LA BASE DE DATOS 
					seriesFromDataBase = serieService.findBySerieCodes(serieCodes);
					
					// RECORREMOS LOS COMICS OBTENIDOS PARA CREAR LOS DETALLES
					for(com.marvel.entity.Serie serieFromDataBase: seriesFromDataBase) {
						if(!existInDataBase(serieFromDataBase, characterFromDataBase)) {
							CharacterSerie characterComic = new CharacterSerie();
							characterComic.setCharacter(characterFromDataBase);
							characterComic.setSerie(serieFromDataBase);
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
	
	private Boolean existInDataBase(com.marvel.entity.Serie serie, com.marvel.entity.Character character) {
		try {
			return characterSerieRepository.findByCharacterAndSerie(character, serie).isPresent();
		} catch (Exception e) {
			throw e;
		}
	}

}
