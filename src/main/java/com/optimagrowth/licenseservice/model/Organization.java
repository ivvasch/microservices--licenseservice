package com.optimagrowth.licenseservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Organization extends RepresentationModel<Organization> {
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

}
