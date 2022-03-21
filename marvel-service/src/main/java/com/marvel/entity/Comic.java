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
@Entity(name = "comics")
public class Comic implements Serializable {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @Column(name = "comic_code")
	private Long comicCode;
	private String name;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "comic")
	private List<CharacterComic> characterComics;
	private static final long serialVersionUID = 2940436717638004364L;
}
