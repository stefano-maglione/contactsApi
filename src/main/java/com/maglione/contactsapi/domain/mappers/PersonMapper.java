package com.maglione.contactsapi.domain.mappers;

import com.maglione.contactsapi.domain.Dto.PersonDto;
import com.maglione.contactsapi.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto convertToDto(Person person);

    Person convertToEntity(PersonDto personDto);
}
