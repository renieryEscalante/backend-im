package com.marvel.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Comic;

public interface ComicService {
	public Void saveComics(List<Result> characters);
	public List<Comic> findByComicCodeIn(List<Long> comicCodes);
	public Page<com.marvel.dto.comics.getall.Comic> findAll(Integer offset, Integer limit);
	public com.marvel.dto.comics.getbycode.Comic findByCode(Long comicCode) throws Exception;
}
