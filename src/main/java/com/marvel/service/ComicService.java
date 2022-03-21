package com.marvel.service;

import java.util.List;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Comic;

public interface ComicService {
	public Void saveComics(List<Result> characters);
	public List<Comic> findByComicCodeIn(List<Long> comicCodes);

}
