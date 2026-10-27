package com.orbit.management.service;

import com.orbit.management.dto.requestDto.OrganizationRequest;
import com.orbit.management.dto.requestDto.OrganizationStatusRequest;
import com.orbit.management.dto.responseDto.OrganizationResponse;
import com.orbit.management.entity.Organization;
import com.orbit.management.entity.OrganizationStatus;
import com.orbit.management.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository
                                       organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public OrganizationResponse create(OrganizationRequest
                                               organizationRequest) {
        Long startedAt = System.nanoTime();
        checkUnique(
                organizationRequest.getName(),
                organizationRequest.getSlug()
        );
        Organization organization = new Organization();
        organization.setName(organizationRequest.getName());
        organization.setSlug(organizationRequest.getSlug());
        organization.setStatus(OrganizationStatus.PENDING);
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());

        organizationRepository.save(organization);

//        auditService.record(organization,
//                "CREATE_ORGANIZATION",
//                "ORGANIZATION",
//                organization.getId().toString(),
//                startedAt,
//                "Organization created in PENDING status"
//                );
        return OrganizationResponse.from(organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAll() {
        List<OrganizationResponse> response = new ArrayList<>();

        for (Organization organization : organizationRepository
                .findAllByStatusNot(OrganizationStatus.DELETED)
        ) {
            response.add(OrganizationResponse.from(organization));
        }
        return response;
    }

    // Find organization by ID
    @Transactional(readOnly = true)
    public OrganizationResponse findById(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found with ID: " + id
                ));

        if (organization.getStatus() == OrganizationStatus.DELETED) {
            throw new EntityNotFoundException(
                    "Organization not found with ID: " + id
            );
        }

        return OrganizationResponse.from(organization);
    }

    // Update organization name and slug
    @Transactional
    public OrganizationResponse update(
            Long id,
            OrganizationRequest organizationRequest) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found with ID: " + id
                ));

        if (organization.getStatus() == OrganizationStatus.DELETED) {
            throw new EntityNotFoundException(
                    "Organization not found with ID: " + id
            );
        }

        checkUniqueForUpdate(
                organizationRequest.getName(),
                organizationRequest.getSlug(),
                id
        );

        organization.setName(organizationRequest.getName());
        organization.setSlug(organizationRequest.getSlug());
        organization.setUpdatedAt(LocalDateTime.now());

        Organization updatedOrganization =
                organizationRepository.save(organization);

        return OrganizationResponse.from(updatedOrganization);
    }

    // Change organization status
    @Transactional
    public OrganizationResponse changeStatus(
            Long id,
            OrganizationStatusRequest organizationStatusRequest) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found with ID: " + id
                ));

        if (organization.getStatus() == OrganizationStatus.DELETED) {
            throw new EntityNotFoundException(
                    "Organization not found with ID: " + id
            );
        }

        OrganizationStatus newStatus =
                organizationStatusRequest.getStatus();

        if (newStatus == null || newStatus == OrganizationStatus.DELETED) {
            throw new IllegalArgumentException(
                    "Invalid status. Use the delete endpoint to delete an organization."
            );
        }

        organization.setStatus(newStatus);
        organization.setUpdatedAt(LocalDateTime.now());

        Organization updatedOrganization =
                organizationRepository.save(organization);

        return OrganizationResponse.from(updatedOrganization);
    }

    // Soft delete organization
    @Transactional
    public void delete(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Organization not found with ID: " + id
                ));

        if (organization.getStatus() == OrganizationStatus.DELETED) {
            throw new EntityNotFoundException(
                    "Organization not found with ID: " + id
            );
        }

        organization.setStatus(OrganizationStatus.DELETED);
        organization.setUpdatedAt(LocalDateTime.now());

        organizationRepository.save(organization);
    }


    private void checkUnique(String name, String slug) {

        if (organizationRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "Organization name already exists"
            );
        }

        if (organizationRepository.existsBySlugIgnoreCase(slug)) {
            throw new IllegalArgumentException(
                    "Organization slug already exists"
            );
        }
    }

    private void checkUniqueForUpdate(
            String name, String slug, Long id) {

        if (organizationRepository
                .existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new IllegalArgumentException(
                    "Organization name already exists"
            );
        }

        if (organizationRepository
                .existsBySlugIgnoreCaseAndIdNot(slug, id)) {
            throw new IllegalArgumentException(
                    "Organization slug already exists"
            );
        }
    }
}
