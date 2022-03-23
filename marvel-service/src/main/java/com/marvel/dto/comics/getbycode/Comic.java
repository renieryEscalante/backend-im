package com.marvel.dto.comics.getbycode;

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
public class Comic implements Serializable {
	private Long comicCode;
	private String name;
	private List<Character> characters;
	private static final long serialVersionUID = -2804954648535868849L;
}
