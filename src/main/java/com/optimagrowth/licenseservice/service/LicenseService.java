package com.optimagrowth.licenseservice.service;

import com.optimagrowth.licenseservice.configuration.ServiceConfig;
import com.optimagrowth.licenseservice.model.License;
import com.optimagrowth.licenseservice.model.Organization;
import com.optimagrowth.licenseservice.repository.LicenseRepository;
import com.optimagrowth.licenseservice.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.licenseservice.service.client.OrganizationFeignClient;
import com.optimagrowth.licenseservice.service.client.OrganizationRestTemplateClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {

    private final MessageSource messages;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig config;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;
    private final OrganizationFeignClient organizationFeignClient;
    private final OrganizationRestTemplateClient organizationRestClient;

    public LicenseService(@Qualifier("messageSource") MessageSource messages, LicenseRepository licenseRepository,
                          ServiceConfig config, OrganizationDiscoveryClient organizationDiscoveryClient,
                          OrganizationFeignClient organizationFeignClient, OrganizationRestTemplateClient organizationRestClient) {
        this.messages = messages;
        this.licenseRepository = licenseRepository;
        this.config = config;
        this.organizationDiscoveryClient = organizationDiscoveryClient;
        this.organizationFeignClient = organizationFeignClient;
        this.organizationRestClient = organizationRestClient;
    }

    public License getLicense(String licenseId, String organizationId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messages.getMessage("license search error message", null, null),
                    licenseId, organizationId));
        }
        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign": {
                System.out.println("I'm using the fign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            }
            case "discovery": {
                System.out.println("I'm using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            }
            case "rest": {
                System.out.println("I'm using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            }
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }
        return organization;
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null), licenseId);
        return responseMessage;
    }

}
