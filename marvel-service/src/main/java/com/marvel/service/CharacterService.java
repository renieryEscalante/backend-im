package com.marvel.service;

import java.util.List;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Character;

public interface CharacterService {
	public Void saveCharacters(List<Result> characters);
	public Character findByCharacterCode(Long characterCode);
}
