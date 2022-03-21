package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marvel.dto.characters.response.Item;
import com.marvel.dto.characters.response.Result;
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
	public Void saveComics(List<Result> characters) {
		List<Comic> comicEntityList = null;
		try {
			comicEntityList = builComicEntityList(characters);
			comicEntityList = filterList(comicEntityList);
			if(!comicEntityList.isEmpty()) {
				comicRepository.saveAll(comicEntityList);
			}
		} catch (Exception e) {
			throw e;
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
		try {
			comicListFiltered = new ArrayList<>();
			
			// OBTENEMOS TODOS LOS CODIGOS DE LOS COMICS
			comicCodes = comicEntityList
					.stream()
					.map(it -> it.getComicCode())
					.collect(Collectors.toList());
			
			// BUSCAMOS EN BASE DE DATOS
			comicsInDataBase = comicRepository.findByComicCodeIn(comicCodes);
			
			// REUTILIZAMOS LA VARIABLE Y OBTENEMOS LOS CODIGOS DE LOS REGISTROS QUE OBTUVIMOS DE LA BASE DE DATOS
			comicCodes = comicsInDataBase
					.stream()
					.map(it -> it.getComicCode())
					.collect(Collectors.toList());
			
			// FILTRAMOS Y AGREGAMOS A UNA NUEVA LISTA LOS COMICS QUE NO ESTAN EN BASE DE DATOS
			for(Comic itemComic: comicEntityList) {
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
	public List<Comic> findByComicCodeIn(List<Long> comicCodes) {
		try {
			return comicRepository.findByComicCodeIn(comicCodes);
		} catch (Exception e) {
			throw e;
		}
	}

}
