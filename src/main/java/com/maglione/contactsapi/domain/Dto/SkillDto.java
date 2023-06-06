package com.maglione.contactsapi.domain.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


public class SkillDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty(message = "Name is mandatory")
    @Schema(description = "Skill's name", example = "java")
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Generated uri resource", example = "api/v1/skills/1")
    private String skillUrl;
    @NotEmpty(message = "Level is mandatory")
    @Pattern(regexp = "junior|medium|senior", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSkillUrl() {
        return skillUrl;
    }

    public void setSkillUrl(String skillUrl) {
        this.skillUrl = skillUrl;
    }

}
