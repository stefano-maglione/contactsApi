package com.maglione.contactsapi.controllers;

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
@RequestMapping(SkillController.BASE_URL)
public class SkillController {
    public static final String BASE_URL = "/api/v1/skills";
    private final SkillService skillService;

    private final PersonService personService;

    public SkillController(SkillService skillService, PersonService personService) {
        this.skillService = skillService;
        this.personService = personService;
    }

    @PostMapping( produces = {"application/json"})
    @Operation(summary = "Add a new skill", description = "Add a new skill", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successful operation", content = @Content(schema = @Schema(implementation = SkillDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody SkillDto skillDto) {

        skillService.verifyUniqueSkill(skillDto);
        SkillDto skillDtoSaved = skillService.createNewSkill(skillDto);
        // Set the headers for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(skillDtoSaved.getId()).toUri());

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(produces = {"application/json"})
    @Operation(summary = "Retrieve all skills", description = "Retrieve all skills", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SkillListDto.class))),
    })
    public ResponseEntity<SkillListDto> getSkillList() {
        SkillListDto skillListDto = skillService.getAllSkills();

        return new ResponseEntity<>(skillListDto, HttpStatus.OK);
    }


    @GetMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Find skill by ID", description = "Returns a single skill", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SkillDto.class))),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id) {
        SkillDto skillDto = skillService.getSkillById(id);

        return new ResponseEntity<>(skillDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Update given Skill", description = "Update given Skill", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SkillDto.class))),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    public ResponseEntity<SkillDto> updateSkill(@PathVariable Long id, @Valid @RequestBody SkillDto skillDto) {
        skillService.saveSkillByDTO(id, skillDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Patch a given Skill", description = "Patch a given Skill", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = SkillDto.class))),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    public ResponseEntity<SkillDto> patchSkill(@PathVariable Long id, @Valid @RequestBody SkillDto skillDto) {
        SkillDto skillDtoPatched = skillService.patchSkill(id, skillDto);

        return new ResponseEntity<>(skillDtoPatched, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @Operation(summary = "Delete a given Skill", description = "Delete a given Skill", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    public ResponseEntity<SkillDto> deleteSkill(@PathVariable Long id) {

        skillService.deleteSkillById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/persons", produces = {"application/json"})
    @Operation(summary = "Retrieve persons with the skill", description = "Returns a single skill", tags = {"Skill"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = PersonListDto.class))),
            @ApiResponse(responseCode = "404", description = "Skill not found", content = @Content)
    })
    public ResponseEntity<PersonListDto> getPersonsBySkillId(@PathVariable Long id) {
        SkillDto skillDto = skillService.getSkillById(id);

        PersonListDto personListDto = skillService.getPersonsBySkill(id);
        return new ResponseEntity<>(personListDto, HttpStatus.OK);
    }


}
