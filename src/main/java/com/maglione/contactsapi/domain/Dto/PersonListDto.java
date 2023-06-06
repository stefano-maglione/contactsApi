package com.maglione.contactsapi.domain.Dto;

import java.util.List;

public class PersonListDto {

    List<PersonDto> persons;


    public PersonListDto(List<PersonDto> persons) {
        this.persons = persons;
    }

    public List<PersonDto> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDto> persons) {
        this.persons = persons;
    }


}
