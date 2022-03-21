package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Character;
import com.marvel.repository.CharacterRepository;
import com.marvel.service.CharacterService;

@Service
public class CharacterImpl implements CharacterService {
	
	@Autowired
	private CharacterRepository repository;

	@Override
	@Transactional
	public Void saveCharacters(List<Result> characters) {
		List<Character> characterEntityList = null;
		try {
			
			characterEntityList = builCharacterEntityList(characters);
			characterEntityList = filterList(characterEntityList);
			
			if(!characterEntityList.isEmpty()) {
				Iterable<Character> characterListIterable = (Iterable<Character>) characterEntityList;
				repository.saveAll(characterListIterable);
			}

		} catch (Exception e) {
			throw e;
		}
		
		return null;
	}
	
	private List<Character> builCharacterEntityList(List<Result> characters){
		List<Character> characterEntityList = null;
		try {
			characterEntityList = characters
			.stream()
			.map( it -> {
				Character character = new Character();
				character.setCharacterCode(Long.parseLong(it.getId().toString()));
				character.setDescription(it.getDescription());
				character.setName(it.getName());
				return character;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			throw e;
		}
		return characterEntityList;
	}

	private List<Character> filterList(List<Character> characterEntityList){
		List<Long> characterCodes = null;
		List<Character> charactersInDataBase = null;
		List<Character> characterListFiltered = null;
		try {
			characterListFiltered = new ArrayList<>();
			
			// OBTENEMOS TODOS LOS CODIGOS DE LOS PERSONAJES
			characterCodes = characterEntityList
					.stream()
					.map(it -> it.getCharacterCode())
					.collect(Collectors.toList());
			
			// BUSCAMOS EN BASE DE DATOS
			charactersInDataBase = repository.findByCharacterCodeIn(characterCodes);
			
			// REUTILIZAMOS LA VARIABLE Y OBTENEMOS LOS CODIGOS DE LOS REGISTROS QUE OBTUVIMOS DE LA BASE DE DATOS
			characterCodes = charactersInDataBase
					.stream()
					.map(it -> it.getCharacterCode())
					.collect(Collectors.toList());
			
			// FILTRAMOS Y AGREGAMOS A UNA NUEVA LISTA LOS PERSONAJES QUE NO ESTAN EN BASE DE DATOS
			for(Character itemCharacter: characterEntityList) {
				if(!characterCodes.contains(itemCharacter.getCharacterCode())) {
					characterListFiltered.add(itemCharacter);
				}
			}			
	
		} catch (Exception e) {
			throw e;
		}
		return characterListFiltered;
	}

	
	@Override
	public Character findByCharacterCode(Long characterCode) {
		try {
			return repository.findByCharacterCode(characterCode).orElse(null);
		} catch (Exception e) {
			throw e;
		}
	}
}
