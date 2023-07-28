package com.leomac00.restwithspringbootandjava.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Person implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;

}
