package com.marvel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marvel.common.Constants;
import com.marvel.common.CustomException;
import com.marvel.dto.characters.getbycode.Comic;
import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Character;
import com.marvel.entity.Serie;
import com.marvel.repository.CharacterRepository;
import com.marvel.service.CharacterService;

@Service
public class CharacterImpl implements CharacterService {

	@Autowired
	private CharacterRepository repository;

	@Override
	@Transactional
	public Void saveCharacters(List<Result> characters) throws CustomException {
		List<Character> characterEntityList = null;
		try {

			characterEntityList = builCharacterEntityList(characters);
			characterEntityList = filterList(characterEntityList);

			if (!characterEntityList.isEmpty()) {
				Iterable<Character> characterListIterable = (Iterable<Character>) characterEntityList;
				repository.saveAll(characterListIterable);
			}

		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
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
				if(!Objects.isNull(it.getThumbnail())) {
					String urlImg = String.format("%s.%s", it.getThumbnail().getPath(),
							it.getThumbnail().getExtension());
					character.setImage(urlImg);
				}
				
				return character;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			throw e;
		}
		return characterEntityList;
	}

	private List<Character> filterList(List<Character> characterEntityList) {
		List<Long> characterCodes = null;
		List<Character> charactersInDataBase = null;
		List<Character> characterListFiltered = null;
		try {
			characterListFiltered = new ArrayList<>();

			// OBTENEMOS TODOS LOS CODIGOS DE LOS PERSONAJES
			characterCodes = characterEntityList.stream().map(it -> it.getCharacterCode()).collect(Collectors.toList());

			// BUSCAMOS EN BASE DE DATOS
			charactersInDataBase = repository.findByCharacterCodeIn(characterCodes);

			// REUTILIZAMOS LA VARIABLE Y OBTENEMOS LOS CODIGOS DE LOS REGISTROS QUE
			// OBTUVIMOS DE LA BASE DE DATOS
			characterCodes = charactersInDataBase.stream().map(it -> it.getCharacterCode())
					.collect(Collectors.toList());

			// FILTRAMOS Y AGREGAMOS A UNA NUEVA LISTA LOS PERSONAJES QUE NO ESTAN EN BASE
			// DE DATOS
			for (Character itemCharacter : characterEntityList) {
				if (!characterCodes.contains(itemCharacter.getCharacterCode())) {
					characterListFiltered.add(itemCharacter);
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return characterListFiltered;
	}

	@Override
	@Transactional(readOnly = true)
	public Character findByCharacterCode(Long characterCode) throws CustomException {
		try {
			return repository.findByCharacterCode(characterCode).orElse(null);
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public com.marvel.dto.characters.getbycode.Character findByCode(Long characterCode) throws CustomException {
		Character characterFromDataBase = null;
		com.marvel.dto.characters.getbycode.Character response = null;
		try {
			characterFromDataBase = repository.findByCharacterCode(characterCode)
					.orElseThrow(() -> new CustomException(Constants.MSG_NOTFOUND, HttpStatus.NOT_FOUND));

			response = buildResponseForFindByCode(characterFromDataBase);
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
		return response;
	}

	private com.marvel.dto.characters.getbycode.Character buildResponseForFindByCode(Character characterFromDataBase) {
		com.marvel.dto.characters.getbycode.Character response = null;
		List<Comic> comics = null;
		try {
			response = new com.marvel.dto.characters.getbycode.Character();
			response.setCharacterCode(characterFromDataBase.getCharacterCode());
			response.setName(characterFromDataBase.getName());
			response.setDescription(characterFromDataBase.getDescription());

			comics = characterFromDataBase.getCharacterComics().stream().map(it -> {
				Comic comic = new Comic();
				com.marvel.entity.Comic comicFromDataBase;
				comicFromDataBase = it.getComic();

				comic.setComicCode(comicFromDataBase.getComicCode());
				comic.setName(comicFromDataBase.getName());

				return comic;
			}).collect(Collectors.toList());

			response.setComics(comics);

		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@Override
	public List<com.marvel.dto.characters.getbynameandserieandstory.Character> getByNameAndSerieAndStory(
			String characterName, String serieName, String storyName) throws CustomException {
		List<com.marvel.dto.characters.getbynameandserieandstory.Character> response = null;
		List<Character> charactersFromDataBase = null;
		String sintaxLike = "%#filter%";
		try {

			if (Objects.isNull(characterName) && Objects.isNull(serieName) && Objects.isNull(storyName)) {
				throw new CustomException("One filter is required (character, story, serie).", HttpStatus.BAD_REQUEST);
			}

			if (!Objects.isNull(characterName)) {
				characterName = sintaxLike.replace("#filter", characterName);
			}

			if (!Objects.isNull(serieName)) {
				serieName = sintaxLike.replace("#filter", serieName);
			}

			if (!Objects.isNull(storyName)) {
				storyName = sintaxLike.replace("#filter", storyName);
			}

			charactersFromDataBase = repository.getByNameAndSerieAndStory(characterName, serieName, storyName);
			response = builResponseFromGetByNameAndSerieAndStory(charactersFromDataBase);

		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
		return response;
	}

	public List<com.marvel.dto.characters.getbynameandserieandstory.Character> builResponseFromGetByNameAndSerieAndStory(
			List<Character> charactersFromDataBase) throws CustomException {
		List<com.marvel.dto.characters.getbynameandserieandstory.Character> response = null;
		try {
			response = charactersFromDataBase.stream().map(it -> {
				com.marvel.dto.characters.getbynameandserieandstory.Character character;
				character = new com.marvel.dto.characters.getbynameandserieandstory.Character();
				List<com.marvel.dto.characters.getbynameandserieandstory.Story> stories = null;
				List<com.marvel.dto.characters.getbynameandserieandstory.Serie> series = null;

				character.setCharacterCode(it.getCharacterCode());
				character.setName(it.getName());
				character.setDescription(it.getDescription());

				stories = it.getCharacterStories().stream().map(characterStoryFromDataBase -> {
					com.marvel.dto.characters.getbynameandserieandstory.Story story;
					story = new com.marvel.dto.characters.getbynameandserieandstory.Story();
					com.marvel.entity.Story storyFromDataBase = characterStoryFromDataBase.getStory();

					story.setName(storyFromDataBase.getName());
					story.setStoryCode(storyFromDataBase.getStoryCode());

					return story;
				}).collect(Collectors.toList());
				character.setStories(stories);

				series = it.getCharacterSeries().stream().map(characterSeriesFromDataBase -> {
					com.marvel.dto.characters.getbynameandserieandstory.Serie serie;
					serie = new com.marvel.dto.characters.getbynameandserieandstory.Serie();
					Serie serieFromDataBase = characterSeriesFromDataBase.getSerie();

					serie.setName(serieFromDataBase.getName());
					serie.setSerieCode(serieFromDataBase.getSerieCode());

					return serie;
				}).collect(Collectors.toList());
				character.setSeries(series);

				return character;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			throw new CustomException(Constants.MSG_CONFLIC_ERROR, HttpStatus.CONFLICT);
		}
		return response;
	}
}
