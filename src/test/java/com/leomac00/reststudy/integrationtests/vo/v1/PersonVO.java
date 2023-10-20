package com.leomac00.reststudy.integrationtests.vo.v1;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String first_name;
    private String last_name;
    private String address;
    private String gender;
    private Boolean enabled;

}
