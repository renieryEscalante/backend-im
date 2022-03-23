package com.marvel.service;

import java.util.List;

import com.marvel.common.CustomException;
import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Character;

public interface CharacterService {
	public Void saveCharacters(List<Result> characters) throws CustomException;

	public Character findByCharacterCode(Long characterCode) throws CustomException;

	public com.marvel.dto.characters.getbycode.Character findByCode(Long characterCode) throws CustomException;

	public List<com.marvel.dto.characters.getbynameandserieandstory.Character> getByNameAndSerieAndStory(
			String characterName, String serieName, String storyName) throws CustomException;

	public com.marvel.dto.characters.getcharacterbycode.Character getCharacterByCode(Long characterCode)
			throws CustomException;
}
