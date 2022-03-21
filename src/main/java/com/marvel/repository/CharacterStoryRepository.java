package com.marvel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvel.entity.Character;
import com.marvel.entity.CharacterStory;
import com.marvel.entity.Story;

public interface CharacterStoryRepository extends JpaRepository<CharacterStory, Long> {
	public Optional<CharacterStory> findByCharacterAndStory(Character character, Story story);
}
