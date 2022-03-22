package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marvel.common.Constants;
import com.marvel.common.CustomException;
import com.marvel.dto.characters.response.Item;
import com.marvel.dto.characters.response.Result;
import com.marvel.dto.comics.getall.Character;
import com.marvel.entity.Comic;
import com.marvel.repository.ComicRepository;
import com.marvel.service.ComicService;

@Service
public class ComicImpl implements ComicService {
	
	@Autowired
	private ComicRepository comicRepository;
	
	private static final String URI_COMIC = "http://gateway.marvel.com/v1/public/comics/";

	@Override
	@Transactional
	public Void saveComics(List<Result> characters) throws CustomException {
		List<Comic> comicEntityList = null;
		try {
			comicEntityList = builComicEntityList(characters);
			comicEntityList = filterList(comicEntityList);

			if(!comicEntityList.isEmpty()) {
				comicRepository.saveAll(comicEntityList);
			}
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
		return null;
	}
	
	private List<Comic> builComicEntityList(List<Result> characters){
		List<Comic> comicEntityList = null;
		List<com.marvel.dto.characters.response.Comic> comics = null;
		List<Item> itemComicList = null;
		try {
			itemComicList = new ArrayList<Item>();
			
			// FILTRAMOS LOS PERSONAJES QUE TIENEN COMICS
			comics = characters
				.stream()
				.filter(it -> it.getComics().getAvailable() > 0)
				.map( it -> it.getComics())
				.collect(Collectors.toList());
			
			// RECORREMOS Y AGREGAMOS A UNA SOLA LISTA
			for(com.marvel.dto.characters.response.Comic itemComic: comics) {
				itemComicList.addAll(itemComic.getItems());
			}
			
			// RECORREMOS Y CONSTRUIMOS UNA LISTA DE ENTIDADES COMICS
			comicEntityList = itemComicList
				.stream()
				.distinct()
				.map(it -> {
					Comic comicEntity = new Comic();
					String id = it.getResourceURI();
					id = id.replace(URI_COMIC, "");					
					comicEntity.setComicCode(Long.parseLong(id));
					comicEntity.setName(it.getName());
					
					return comicEntity;
				}).collect(Collectors.toList());
			
		} catch (Exception e) {
			throw e;
		}		
		return comicEntityList;
	}
	
	private List<Comic> filterList(List<Comic> comicEntityList){
		List<Long> comicCodes = null;
		List<Comic> comicsInDataBase = null;
		List<Comic> comicListFiltered = null;
		List<Comic> comicEntityListFiltered = null;
		try {
			comicListFiltered = new ArrayList<>();
			comicEntityListFiltered = new ArrayList<>();
			
			// OBTENEMOS TODOS LOS CODIGOS DE LOS COMICS
			comicCodes = comicEntityList
					.stream()
					.map(it -> it.getComicCode())
					.distinct()
					.collect(Collectors.toList());
			
			// FILTRAMOS LOS DATOS REPETIDOS
			for(Long comicCode: comicCodes) {
				Comic comicAux = comicEntityList
						.stream()
						.filter( it -> it.getComicCode() == comicCode)
						.findFirst().orElse(null);
				if(!Objects.isNull(comicAux)) {
					comicEntityListFiltered.add(comicAux);
				}
			}
			
			// BUSCAMOS EN BASE DE DATOS
			comicsInDataBase = comicRepository.findByComicCodeIn(comicCodes);
			
			// REUTILIZAMOS LA VARIABLE Y OBTENEMOS LOS CODIGOS DE LOS REGISTROS QUE OBTUVIMOS DE LA BASE DE DATOS
			comicCodes = comicsInDataBase
					.stream()
					.map(it -> it.getComicCode())
					.collect(Collectors.toList());
			
			// FILTRAMOS Y AGREGAMOS A UNA NUEVA LISTA LOS COMICS QUE NO ESTAN EN BASE DE DATOS
			for(Comic itemComic: comicEntityListFiltered) {
				if(!comicCodes.contains(itemComic.getComicCode())) {
					comicListFiltered.add(itemComic);
				}
			}			
			
		} catch (Exception e) {
			throw e;
		}
		return comicListFiltered;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comic> findByComicCodeIn(List<Long> comicCodes) throws CustomException {
		try {
			return comicRepository.findByComicCodeIn(comicCodes);
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<com.marvel.dto.comics.getall.Comic> findAll(Integer offset, Integer limit) throws CustomException {
		Page<Comic> comicsFromDataBase = null;
		Page<com.marvel.dto.comics.getall.Comic> response = null;
		Pageable pageable = null;
		try {
			pageable = PageRequest.of(offset, limit);
			comicsFromDataBase = comicRepository.findAll(pageable);
			response = buildResponseFromFindAll(comicsFromDataBase);
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
		return response;
	}
	
	private Page<com.marvel.dto.comics.getall.Comic> buildResponseFromFindAll(Page<Comic> comicsFromDataBase){
		Page<com.marvel.dto.comics.getall.Comic> response = null;
		try {
			response = comicsFromDataBase
					.map( it -> {
						List<Character> characters = null; 
						com.marvel.dto.comics.getall.Comic comic = new com.marvel.dto.comics.getall.Comic();
						comic.setComicCode(it.getComicCode());
						comic.setName(it.getName());
						
						characters = it.getCharacterComics()
							.stream()
							.map( characterComicsFromDataBase -> {
								Character character = new Character();
								com.marvel.entity.Character characterFromDataBase = characterComicsFromDataBase.getCharacter();
								character.setCharacterCode(characterFromDataBase.getCharacterCode());
								character.setDescription(characterFromDataBase.getDescription());
								character.setName(characterFromDataBase.getName());
								
								return character;
							}).collect(Collectors.toList());
						comic.setCharacters(characters);
						
						return comic;
					});
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@Override
	@Transactional(readOnly = true)
	public com.marvel.dto.comics.getbycode.Comic findByCode(Long comicCode) throws CustomException {
		Comic comicFromDataBase = null;
		com.marvel.dto.comics.getbycode.Comic response = null;
		try {
			comicFromDataBase = comicRepository.findByComicCode(comicCode)
				.orElseThrow(()->  new CustomException(Constants.MSG_NOTFOUND, HttpStatus.NOT_FOUND));
			response = buildResponseForFindByCode(comicFromDataBase);
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
		return response;
	}
	
	private com.marvel.dto.comics.getbycode.Comic buildResponseForFindByCode(Comic comicFromDataBase){
		List<com.marvel.dto.comics.getbycode.Character> characters = null; 
		com.marvel.dto.comics.getbycode.Comic comic = null;
		try {
			comic = new com.marvel.dto.comics.getbycode.Comic();
			comic.setComicCode(comicFromDataBase.getComicCode());
			comic.setName(comicFromDataBase.getName());
			
			characters = comicFromDataBase
			.getCharacterComics()
			.stream()
			.map( characterComicsFromDataBase -> {
				com.marvel.dto.comics.getbycode.Character character = new com.marvel.dto.comics.getbycode.Character();
				com.marvel.entity.Character characterFromDataBase = characterComicsFromDataBase.getCharacter();
				character.setCharacterCode(characterFromDataBase.getCharacterCode());
				character.setDescription(characterFromDataBase.getDescription());
				character.setName(characterFromDataBase.getName());
				
				return character;
			}).collect(Collectors.toList());
			comic.setCharacters(characters);
		} catch (Exception e) {
			throw e;
		}
		return comic;
	}

}
