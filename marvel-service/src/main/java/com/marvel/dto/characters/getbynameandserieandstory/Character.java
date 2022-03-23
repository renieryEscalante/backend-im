package com.marvel.dto.characters.getbynameandserieandstory;

import java.io.Serializable;
import java.util.List;

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
public class Character implements Serializable {
	private Long characterCode;
	private String name;
	private String description;
	private List<Story> stories;
	private List<Serie> series;
	private static final long serialVersionUID = 5562364737733387510L;
}
