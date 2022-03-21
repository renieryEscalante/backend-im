package com.marvel.service;

import java.util.List;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.StoryType;

public interface StoryTypeService {
	public Void saveStoryType(List<Result> characters);
	public List<StoryType> getAll();
}
