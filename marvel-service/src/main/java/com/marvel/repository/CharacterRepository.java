package com.marvel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.marvel.entity.Character;

public interface CharacterRepository extends JpaRepository<Character, Long> {
	public List<Character> findByCharacterCodeIn(List<Long> characterCodes);
	public Optional<Character> findByCharacterCode(Long characterCode);
	
	@Query(nativeQuery = true, value = 
			" select distinct id, character_code, description, image, name from "
			+ "	(select "
			+ "		c.*, "
			+ "		ifnull(s.name, 'null') seriename, "
			+ "        ifnull(sto.name, 'null') storyname "
			+ "        from characters c"
			+ "		left join character_series cs on c.id = cs.character_id"
			+ "		left join series s on s.id = cs.serie_id"
			+ "		left join character_stories cst on c.id = cst.character_id "
			+ "		left join stories sto on sto.id = cst.story_id"
			+ "        group by seriename, storyname) x"
			+ " where upper(name) like upper(ifnull(?1, name))"
			+ " and  upper(seriename) like upper(ifnull(?2, seriename))"
			+ " and upper(storyname) like upper(ifnull(?3, storyname))")
	public List<Character> getByNameAndSerieAndStory(String characterName, String serieName, String storyName);
}
