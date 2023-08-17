package com.leomac00.reststudy.config;

import com.leomac00.reststudy.data.vo.v1.BookVO;
import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.models.Person;
import com.leomac00.reststudy.models.Book;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Configuration
public class CustomModelMapper {

    Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
        @Override
        public LocalDate get() {
            return LocalDate.now();
        }
    };

    Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
        @Override
        protected LocalDate convert(String source) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(source, format);
            return localDate;
        }
    };


    @Bean
    public ModelMapper customMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(Person.class, PersonVO.class).addMapping(Person::getId, PersonVO::setKey);
        mapper.typeMap(PersonVO.class, Person.class).addMapping(PersonVO::getKey, Person::setId);

        mapper.typeMap(Book.class, BookVO.class).addMapping(Book::getId, BookVO::setKey);
        mapper.typeMap(BookVO.class, Book.class).addMapping(BookVO::getKey, Book::setId);

        mapper.createTypeMap(String.class, LocalDate.class);
        mapper.addConverter(toStringDate);
        mapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);

        return mapper;
    }
}
