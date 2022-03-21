package com.marvel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvel.entity.Character;
import com.marvel.entity.CharacterSerie;
import com.marvel.entity.Serie;

public interface CharacterSerieRepository extends JpaRepository<CharacterSerie, Long> {
	public Optional<CharacterSerie> findByCharacterAndSerie(Character character, Serie serie);
}
