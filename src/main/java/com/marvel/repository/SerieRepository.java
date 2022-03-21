package com.marvel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvel.entity.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
	public List<Serie> findBySerieCodeIn(List<Long> serieCodes);
	public Optional<Serie> findBySerieCode(Long creatorCode);
}
