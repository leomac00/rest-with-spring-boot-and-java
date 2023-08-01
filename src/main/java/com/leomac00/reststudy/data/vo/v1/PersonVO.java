package com.leomac00.reststudy.data.vo.v1;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}
