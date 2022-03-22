package com.marvel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.marvel.common.Constants;
import com.marvel.entity.Character;

public interface CharacterRepository extends JpaRepository<Character, Long> {
	public List<Character> findByCharacterCodeIn(List<Long> characterCodes);
	public Optional<Character> findByCharacterCode(Long characterCode);
	
	@Query(nativeQuery = true, value = Constants.SQL_GETBYNAMEANDSERIEANDSTORY)
	public List<Character> getByNameAndSerieAndStory(String characterName, String serieName, String storyName);
}
