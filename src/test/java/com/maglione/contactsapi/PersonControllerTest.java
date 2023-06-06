package com.maglione.contactsapi;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maglione.contactsapi.controllers.PersonController;
import com.maglione.contactsapi.domain.Dto.PersonDto;
import com.maglione.contactsapi.domain.Dto.PersonListDto;
import com.maglione.contactsapi.domain.Dto.SkillDto;
import com.maglione.contactsapi.domain.Dto.SkillListDto;
import com.maglione.contactsapi.services.PersonService;
import com.maglione.contactsapi.services.SkillService;
import com.maglione.contactsapi.services.exceptions.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class PersonControllerTest {

    private PersonDto personDto;

    @MockBean
    private PersonService personService;
    @MockBean
    private SkillService skillService;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        personDto = new PersonDto();
        personDto.setEmail("test@email.com");
        personDto.setFirstname("Firstname");
        personDto.setLastname("Lastname");
        personDto.setId(1L);

    }

    @Test
    void whenCreatePersonThenStatusCreated() throws Exception {

        String json = mapper.writeValueAsString(personDto);
        when(personService.createNewPerson(ArgumentMatchers.any())).thenReturn(personDto);
        mockMvc.perform(post(PersonController.BASE_URL).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header()
                        .stringValues("Location", "http://localhost/api/v1/persons/1"));

    }

    @Test
    void whenGetPersonListThenStatusOk() throws Exception {

        PersonListDto personListDto = new PersonListDto(new ArrayList<>());
        when(personService.getAllPersons()).thenReturn(personListDto);
        mockMvc.perform(get(PersonController.BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"persons\":[]}"));

    }

    @Test
    void whenGetPersonByIdThenStatusOk() throws Exception {

        when(personService.getPersonById(1L)).thenReturn(personDto);
        mockMvc.perform(get(PersonController.BASE_URL + "/" + "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", Matchers.equalTo("Firstname")))
                .andExpect(jsonPath("$.lastname", Matchers.equalTo("Lastname")))
                .andExpect(jsonPath("$.email", Matchers.equalTo("test@email.com")));

    }


    @Test
    void whenUpdatePersonThenStatusOk() throws Exception {

        String json = mapper.writeValueAsString(personDto);

        when(personService.savePersonByDTO(1L, personDto)).thenReturn(personDto);
        mockMvc.perform(put(PersonController.BASE_URL + "/" + "1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeletePersonThenStatusOk() throws Exception {

        mockMvc.perform(delete(PersonController.BASE_URL + "/" + "1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }


    @Test
    void whenGetSkillsByPersonThenStatusOk() throws Exception {

        SkillListDto skillListDto = new SkillListDto(new ArrayList<>());

        when(skillService.getSkillByPerson(1L)).thenReturn(skillListDto);
        mockMvc.perform(get(PersonController.BASE_URL + "/" + "1" + "/" + "skills"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"skills\":[]}"));
    }

    @Test
    void whenAddSkillByIdToPersonThenStatusOk() throws Exception {

        SkillDto skillDto = new SkillDto();
        skillDto.setName("JAVA");
        skillDto.setLevel("SENIOR");

        when(personService.getPersonById(1L)).thenReturn(personDto);
        when(skillService.getSkillById(1L)).thenReturn(skillDto);

        mockMvc.perform(put(PersonController.BASE_URL + "/" + "1" + "/" + "skills" + "/" + "1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAddSkillByIdToPersonAndSkillIdNotExistsThenNotFound() throws Exception {

        when(personService.getPersonById(1L)).thenReturn(personDto);
        when(skillService.getSkillById(1L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put(PersonController.BASE_URL + "/" + "1" + "/" + "skills" + "/" + "1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddSkillByIdToPersonAndPersonIdNotExistsThenNotFound() throws Exception {

        SkillDto skillDto = new SkillDto();
        skillDto.setName("JAVA");
        skillDto.setLevel("SENIOR");

        when(personService.getPersonById(1L)).thenThrow(ResourceNotFoundException.class);
        when(skillService.getSkillById(1L)).thenReturn(skillDto);

        mockMvc.perform(put(PersonController.BASE_URL + "/" + "1" + "/" + "skills" + "/" + "1").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}