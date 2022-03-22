package com.marvel.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marvel.dto.comics.getall.Comic;
import com.marvel.service.ComicService;

@RestController
@RequestMapping(path = "/comics")
public class ComicController {
	
	@Autowired
	private ComicService comicService;

	@GetMapping
	public ResponseEntity<Page<Comic>> findAll(@RequestParam(name = "offset", required = false) Optional<Integer> offset,
			@RequestParam(name = "limit", required = false) Optional<Integer> limit) throws Exception{
		ResponseEntity<Page<Comic>> response = null;
		Page<Comic> comicsResponse = null;
		try {
			comicsResponse = comicService.findAll(offset.orElse(0), limit.orElse(20));
			response = new ResponseEntity<Page<Comic>>(comicsResponse, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		return response;
	}
	
	@GetMapping("/{comicCode}")
	public ResponseEntity<com.marvel.dto.comics.getbycode.Comic> finByCode(@PathVariable("comicCode") Long comicCode ) throws Exception{
		ResponseEntity<com.marvel.dto.comics.getbycode.Comic> response = null;
		com.marvel.dto.comics.getbycode.Comic comicResponse = null;
		try {
			comicResponse = comicService.findByCode(comicCode);
			response = new ResponseEntity<com.marvel.dto.comics.getbycode.Comic>(comicResponse, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
		return response;
	}
}
