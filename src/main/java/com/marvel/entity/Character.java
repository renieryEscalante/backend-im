package com.marvel.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table
@Entity(name = "characters")
public class Character implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "character_code", unique = true)
	private Long characterCode;
	private String name;
	@Column(columnDefinition = "text")
	private String description;
	private String image;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "character")
	private List<CharacterComic> characterComics;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "character")
	private List<CharacterSerie> characterSeries;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "character")
	private List<CharacterStory> characterStories;
	private static final long serialVersionUID = 7126076742505912034L;
}
