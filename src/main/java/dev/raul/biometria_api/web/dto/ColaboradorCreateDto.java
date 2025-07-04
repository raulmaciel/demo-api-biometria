package dev.raul.biometria_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class ColaboradorCreateDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String nome;

    @NotBlank
    private String templateBase64;
}
