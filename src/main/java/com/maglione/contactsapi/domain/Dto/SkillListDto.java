package com.maglione.contactsapi.domain.Dto;

import java.util.List;

public class SkillListDto {

    List<SkillDto> skills;

    public SkillListDto(List<SkillDto> skills) {
        this.skills = skills;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }
}
