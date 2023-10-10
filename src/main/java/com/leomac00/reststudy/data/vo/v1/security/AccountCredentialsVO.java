package com.leomac00.reststudy.data.vo.v1.security;

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
