package com.marvel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marvel.entity.StoryType;

public interface StoryTypeRepository extends JpaRepository<StoryType, Long> {
	public List<StoryType> findByNameIn(List<String> names);
}
