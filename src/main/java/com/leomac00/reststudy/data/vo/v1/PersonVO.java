package com.leomac00.reststudy.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@JsonPropertyOrder({"id", "address", "first_name", "last_name"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String address;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String gender;
}
