package com.marvel.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
