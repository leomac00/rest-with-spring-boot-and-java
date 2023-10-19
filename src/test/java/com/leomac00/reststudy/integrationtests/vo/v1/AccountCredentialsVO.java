package com.leomac00.reststudy.integrationtests.vo.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountCredentialsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
}
