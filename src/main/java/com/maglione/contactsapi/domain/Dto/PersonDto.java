package com.maglione.contactsapi.domain.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maglione.contactsapi.domain.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

public class PersonDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotEmpty
    @Schema(description = "Firstname", example = "Mark")
    private String firstname;
    @NotEmpty
    @Schema(description = "Lastname", example = "Rain")
    private String lastname;
    @Email
    @NotEmpty
    @Schema(description = "Email", example = "mark.rain@gmail.com")
    private String email;

    @Schema(description = "Mobile phone number", example = "0762014200")
    private String mobilePhone;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String personUrl;

    @Schema(description = "City", example = "Zurich")
    private String city;
    @Schema(description = "Address", example = "Heggerstrasse 14")
    private String address;
    @Schema(hidden = true)
    @JsonIgnore
    private Set<Skill> skills = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPersonUrl() {
        return personUrl;
    }

    public void setPersonUrl(String personUrl) {
        this.personUrl = personUrl;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
