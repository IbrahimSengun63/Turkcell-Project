package com.turkcell.staj.dtos.packages.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GetAllResponsePackageDTO {

    private int id;
    private String packageName;
    private String description;
    private double price;
    private boolean status;
}
