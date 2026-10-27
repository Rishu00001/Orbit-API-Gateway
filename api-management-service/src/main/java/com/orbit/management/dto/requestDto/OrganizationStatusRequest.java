package com.orbit.management.dto.requestDto;

import com.orbit.management.entity.OrganizationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationStatusRequest {
    @NotNull
    private OrganizationStatus status;
}
