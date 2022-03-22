package com.marvel.service;

import java.util.List;

import com.marvel.common.CustomException;
import com.marvel.dto.characters.response.Result;

public interface CharacterStoryService {
	public Void saveCharacterStories(List<Result> characters) throws CustomException;
}
