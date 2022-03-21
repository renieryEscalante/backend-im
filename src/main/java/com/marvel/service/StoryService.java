package com.marvel.service;

import java.util.List;

import com.marvel.dto.characters.response.Result;
import com.marvel.entity.Story;

public interface StoryService {
	public Void saveStory(List<Result> characters);
	public List<Story> findByStoryCodes(List<Long> StoryCode);
}
