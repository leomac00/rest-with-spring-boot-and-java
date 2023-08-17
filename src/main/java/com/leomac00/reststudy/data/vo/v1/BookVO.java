package com.leomac00.reststudy.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@JsonPropertyOrder({"id", "author", "launch_date", "price", "title"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookVO extends RepresentationModel<BookVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;
    @JsonProperty("author")
    private String author;
    @JsonProperty("launch_date")
    private Date launchDate;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("title")
    private String title;
}
