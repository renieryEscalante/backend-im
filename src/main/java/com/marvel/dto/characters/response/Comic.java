package com.marvel.dto.characters.response;

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
public class Comic {
    private Integer available;
    private String collectionURI;
    private List<Item> items;
    private Integer returned;
}
