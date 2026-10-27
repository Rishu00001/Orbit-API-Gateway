package com.orbit.management.repository;

import com.orbit.management.entity.Organization;
import com.orbit.management.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository
extends JpaRepository<Organization,Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCase(String slug);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);
    List<Organization> findAllByStatusNot(OrganizationStatus status);

    Optional<Organization> findByIdAndStatusNot(Long id, OrganizationStatus status);
}
