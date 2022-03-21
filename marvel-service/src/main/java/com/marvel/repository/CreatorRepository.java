package com.marvel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvel.entity.Creator;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
	public List<Creator> findByCreatorCodeIn(List<Long> creatorCodes);
}
