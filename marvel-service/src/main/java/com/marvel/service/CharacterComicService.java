package com.marvel.service;

import java.util.List;

import com.marvel.common.CustomException;
import com.marvel.dto.characters.response.Result;

public interface CharacterComicService {
	public Void saveCharacterComics(List<Result> characters) throws CustomException;
}
