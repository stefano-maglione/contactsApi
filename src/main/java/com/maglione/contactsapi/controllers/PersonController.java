package com.maglione.contactsapi.controllers;

import com.maglione.contactsapi.domain.Dto.PersonDto;
import com.maglione.contactsapi.domain.Dto.PersonListDto;
import com.maglione.contactsapi.domain.Dto.SkillDto;
import com.maglione.contactsapi.domain.Dto.SkillListDto;
import com.maglione.contactsapi.services.PersonService;
import com.maglione.contactsapi.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(PersonController.BASE_URL)
public class PersonController {
    public static final String BASE_URL = "/api/v1/persons";
    private final PersonService personService;

    private final SkillService skillservice;

    public PersonController(PersonService personService, SkillService skillservice) {
        this.personService = personService;
        this.skillservice = skillservice;
    }

    @PostMapping(produces = {"application/json"})
    @Operation(summary = "Add a new person", description = "Add a new person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    public ResponseEntity<PersonDto> createPerson(@Valid @RequestBody PersonDto personDto) {

        personService.verifyUniquePerson(personDto);
        PersonDto personDtoSaved = personService.createNewPerson(personDto);
        // Set the headers for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(personDtoSaved.getId()).toUri());

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(produces = {"application/json"})
    @Operation(summary = "Retrieve all persons", description = "Retrieve all persons", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonListDto.class))),
    })
    public ResponseEntity<PersonListDto> getPersonList() {
        PersonListDto personListDto = personService.getAllPersons();

        return new ResponseEntity<>(personListDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Find person by ID", description = "Returns a single person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonDto.class))),
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    public ResponseEntity<PersonDto> getPersonById(@PathVariable Long id) {
        PersonDto personDto = personService.getPersonById(id);

        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Update given person", description = "Update given Person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonDto.class))),
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    public ResponseEntity<PersonDto> updatePerson(@PathVariable Long id, @Valid @RequestBody PersonDto personDto) {
        personService.savePersonByDTO(id, personDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Patch a given person", description = "Patch a given Skill", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)

    })
    public ResponseEntity<PersonDto> patchPerson(@PathVariable Long id, @Valid @RequestBody PersonDto personDto) {
        PersonDto personDtoPatched = personService.patchPerson(id, personDto);

        return new ResponseEntity<>(personDtoPatched, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Delete a given person", description = "Delete a given person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    public ResponseEntity<PersonDto> patchPerson(@PathVariable Long id) {

        personService.deletePersonById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/skills", produces = {"application/json"})
    @Operation(summary = "Retrieve all skills a person have", description = "Retrieve all skills a person have", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SkillListDto.class))),
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    public ResponseEntity<SkillListDto> getSkillsByPerson(@PathVariable Long id) {

        SkillListDto skillListDto = skillservice.getSkillByPerson(id);

        return new ResponseEntity<>(skillListDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{personId}/skills/{skillId}", produces = {"application/json"})
    @Operation(summary = "Add a skill to a person", description = "Add a skill to a person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonDto.class))),
            @ApiResponse(responseCode = "404", description = "Person or Skill not found", content = @Content)
    })
    public ResponseEntity<SkillDto> addSkillByIdToPerson(@PathVariable Long personId, @PathVariable Long skillId) {

        SkillDto skillDto = skillservice.getSkillById(skillId);
        PersonDto personDto = personService.getPersonById(personId);
        personService.addSkillByDto(personDto, skillDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping({"/{personId}/skills/{skillId}"})
    @Operation(summary = "Remove a skill from a person", description = "Remove a skill from a person", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Person or Skill not found", content = @Content)
    })
    public ResponseEntity<SkillDto> deleteSkillFromPerson(@PathVariable Long personId, @PathVariable Long skillId) {
        SkillDto skillDto = skillservice.getSkillById(skillId);
        PersonDto personDto = personService.getPersonById(personId);

        personService.removeSkillByDto(personDto, skillDto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
