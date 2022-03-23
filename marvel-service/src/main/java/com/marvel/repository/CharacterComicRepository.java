package com.marvel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvel.entity.Character;
import com.marvel.entity.CharacterComic;
import com.marvel.entity.Comic;

public interface CharacterComicRepository extends JpaRepository<CharacterComic, Long> {
	public Optional<CharacterComic> findByComicAndCharacter(Comic comic, Character character);
}
