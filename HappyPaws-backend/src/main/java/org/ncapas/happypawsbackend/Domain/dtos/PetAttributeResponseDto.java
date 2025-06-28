package org.ncapas.happypawsbackend.Domain.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PetAttributeResponseDto {
    private Integer id;
    private String attributeName;
    private String attributeValue;
    private List<PetInfo> pets;

    @Data
    @Builder
    public static class PetInfo {
        private UUID id;
        private String name;
    }
}
