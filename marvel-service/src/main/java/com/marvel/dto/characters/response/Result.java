package com.marvel.dto.characters.response;

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
public class Result {
    private Integer id;
    private String name;
    private String description;
//    private Date modified;
    private Thumbnail thumbnail;
    private String resourceURI;
    private Comic comics;
    private Serie series;
    private Story stories;
}
