package dev.raul.biometria_api.web.controller;

import dev.raul.biometria_api.model.Colaborador;
import dev.raul.biometria_api.service.ColaboradorService;
import dev.raul.biometria_api.web.dto.ColaboradorCreateDto;
import dev.raul.biometria_api.web.dto.ColaboradorIdentifyDto;
import dev.raul.biometria_api.web.dto.ColaboradorResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/biometria")
public class ColaboradorController {
    private final ColaboradorService colaboradorService;

    @PostMapping
    public ResponseEntity<ColaboradorResponseDto> create(@Valid @RequestBody ColaboradorCreateDto createDto){
        byte[] templateBytes = Base64.getDecoder().decode(createDto.getTemplateBase64());
        Colaborador colaboradorSalvo = colaboradorService.salvarComBiometria(createDto.getNome(), templateBytes);

        ColaboradorResponseDto responseDto = new ColaboradorResponseDto(
                colaboradorSalvo.getId(),
                colaboradorSalvo.getNome()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/identify")
    public ResponseEntity<ColaboradorResponseDto> identify(@Valid @RequestBody ColaboradorIdentifyDto identifyDto){
        byte[] templateCapturado = Base64.getDecoder().decode(identifyDto.getTemplateBase64());
        return colaboradorService.identificar(templateCapturado)
                .map(colaborador -> ResponseEntity
                        .ok(new ColaboradorResponseDto(colaborador.getId(), colaborador.getNome())))
                .orElse(ResponseEntity.notFound().build());
    }

}
