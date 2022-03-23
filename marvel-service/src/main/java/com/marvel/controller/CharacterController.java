package com.marvel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marvel.dto.characters.getbycode.Character;
import com.marvel.service.CharacterService;

@RestController
@RequestMapping(path = "/characters")
public class CharacterController {

	@Autowired
	private CharacterService characterService;

	@GetMapping(path = "/{characterCode}/comics")
	public ResponseEntity<Character> findByCode(@PathVariable("characterCode") Long characterCode) throws Exception {
		ResponseEntity<Character> response = null;
		Character characterResponse = null;
		try {
			characterResponse = characterService.findByCode(characterCode);
			response = new ResponseEntity<Character>(characterResponse, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@GetMapping
	public ResponseEntity<List<com.marvel.dto.characters.getbynameandserieandstory.Character>> getByNameAndSerieAndStory(
			@RequestParam(name = "character", required = false) String character,
			@RequestParam(name = "serie", required = false) String serie,
			@RequestParam(name = "story", required = false) String story) throws Exception {
		ResponseEntity<List<com.marvel.dto.characters.getbynameandserieandstory.Character>> response = null;
		List<com.marvel.dto.characters.getbynameandserieandstory.Character> characterResponse = null;
		try {
			characterResponse = characterService.getByNameAndSerieAndStory(character, serie, story);
			response = new ResponseEntity<List<com.marvel.dto.characters.getbynameandserieandstory.Character>>(
					characterResponse, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	@GetMapping(path = "/{characterCode}")
	public ResponseEntity<com.marvel.dto.characters.getcharacterbycode.Character> getCharacterByCode(
			@PathVariable("characterCode") Long characterCode) throws Exception {
		ResponseEntity<com.marvel.dto.characters.getcharacterbycode.Character> response = null;
		com.marvel.dto.characters.getcharacterbycode.Character characterResponse = null;
		try {
			characterResponse = characterService.getCharacterByCode(characterCode);
			response = new ResponseEntity<com.marvel.dto.characters.getcharacterbycode.Character>(characterResponse,
					HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

}
