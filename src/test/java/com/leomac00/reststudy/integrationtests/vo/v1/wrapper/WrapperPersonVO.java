package com.leomac00.reststudy.integrationtests.vo.v1.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leomac00.reststudy.integrationtests.vo.v1.PersonVO;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement
public class WrapperPersonVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("_embedded")
    private PersonEmbeddedVO embeddedVO;
    @JsonProperty("page")
    private Page page;

    @Data
    @NoArgsConstructor
    public class PersonEmbeddedVO implements Serializable {
        private static final long serialVersionUID = 1L;
        @JsonProperty("personVOList")
        private List<PersonVO> personVOList;
    }

    @Data
    @NoArgsConstructor
    public class Page implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long size;
        private Long totalElements;
        private Long totalPages;
        private Long number;
    }
}
