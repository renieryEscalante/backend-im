package com.marvel.service;

import java.util.List;

import com.marvel.dto.characters.response.Result;

public interface CharacterSerieService {
	public Void saveCharacterSeries(List<Result> characters);
}
