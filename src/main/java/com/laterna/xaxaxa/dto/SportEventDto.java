package com.laterna.xaxaxa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SportEventDto {
    private String id;
    private String name;
    private String location;
    private String dateStart;
    private String dateEnd;
    private int participants;
    private String description;

}