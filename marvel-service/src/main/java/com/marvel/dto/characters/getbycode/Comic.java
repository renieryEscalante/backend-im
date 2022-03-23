package com.marvel.dto.characters.getbycode;

import java.io.Serializable;

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
	private static final long serialVersionUID = -2804954648535868849L;
}
