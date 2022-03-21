package com.marvel.service;

import java.util.List;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Serie;

public interface SerieService {
	public Void saveSeries(List<Result> characters);
	public List<Serie> findBySerieCodes(List<Long> SerieCodes);
}
