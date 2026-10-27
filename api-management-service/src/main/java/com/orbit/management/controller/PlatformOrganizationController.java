
package com.orbit.management.controller;

import com.orbit.management.dto.requestDto.OrganizationRequest;
import com.orbit.management.dto.requestDto.OrganizationStatusRequest;
import com.orbit.management.dto.responseDto.OrganizationResponse;
import com.orbit.management.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform/organization")
public class PlatformOrganizationController {

    private final OrganizationService organizationService;

    public PlatformOrganizationController(
            OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // Create organization
    @PostMapping
    public ResponseEntity<OrganizationResponse> create(
            @Valid @RequestBody OrganizationRequest organizationRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizationService.create(organizationRequest));
    }

    // Read all organizations
    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> findAll() {

        return ResponseEntity.ok(organizationService.findAll());
    }

    // Read one organization
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(organizationService.findById(id));
    }

    // Update organization
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequest organizationRequest) {

        return ResponseEntity.ok(
                organizationService.update(id, organizationRequest));
    }

    // Change organization status
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrganizationResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody
            OrganizationStatusRequest organizationStatusRequest) {

        return ResponseEntity.ok(
                organizationService.changeStatus(
                        id, organizationStatusRequest));
    }

    // Soft delete organization
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
