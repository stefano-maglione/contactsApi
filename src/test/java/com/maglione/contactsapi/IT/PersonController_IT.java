package com.maglione.contactsapi.IT;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maglione.contactsapi.ContactsapiApplication;
import com.maglione.contactsapi.controllers.PersonController;
import com.maglione.contactsapi.domain.Person;
import com.maglione.contactsapi.domain.Skill;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Base64;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = ContactsapiApplication.class)
@AutoConfigureMockMvc
public class PersonController_IT {

    private static ObjectMapper mapper = new ObjectMapper();

    private String baseAuthHeaderUser = "mickey:cheese";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    public void givenEmployeesWIthSkillsWhenGetEmployeesSkillsThenReturnSkills() throws Exception {

        Person person1 = new Person();
        person1.setEmail("test1@gmail.com");
        person1.setFirstname("stefano");

        Skill skill = new Skill();
        skill.setName("java");
        skill.setLevel("senior");

        entityManager.persist(skill);

        person1.addSkill(skill);


        entityManager.persist(person1);
        entityManager.flush();


        mockMvc.perform(get(PersonController.BASE_URL + "/1/" + "skills").header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString(baseAuthHeaderUser.getBytes())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skills", hasSize(1)))
                .andExpect(jsonPath("$.skills.[0].name", Matchers.equalTo("java")));

        entityManager.remove(person1);
        entityManager.remove(skill);
        entityManager.flush();

    }

    @Test
    @Transactional
    public void givenEmployeesWhenGetEmployeesThenGetEmployees() throws Exception {

        Person person = new Person();
        person.setEmail("test@email.com");
        person.setFirstname("stefano");

        entityManager.persist(person);
        entityManager.flush();

        mockMvc.perform(get(PersonController.BASE_URL).header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString(baseAuthHeaderUser.getBytes())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons", hasSize(1)))
                .andExpect(jsonPath("$.persons.[0].email", Matchers.equalTo("test@email.com")));


        entityManager.remove(person);
        entityManager.flush();
    }

}
