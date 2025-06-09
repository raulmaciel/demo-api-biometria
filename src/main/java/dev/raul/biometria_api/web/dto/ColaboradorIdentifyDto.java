package dev.raul.biometria_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class ColaboradorIdentifyDto {
    @NotBlank
    private String templateBase64;
}
