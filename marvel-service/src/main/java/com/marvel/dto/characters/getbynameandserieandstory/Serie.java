package com.marvel.dto.characters.getbynameandserieandstory;

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
public class Serie implements Serializable {
	private Long serieCode;
	private String name;
	private static final long serialVersionUID = -616410577102040479L;
}
