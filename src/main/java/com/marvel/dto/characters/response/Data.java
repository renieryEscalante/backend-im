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
public class Data {
    private Integer offset;
    private Integer limit;
    private Integer total;
    private Integer count;
    private List<Result> results;
}
