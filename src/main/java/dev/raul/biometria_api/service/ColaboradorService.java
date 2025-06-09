package dev.raul.biometria_api.service;

import com.futronic.SDKHelper.*;
import dev.raul.biometria_api.model.Colaborador;
import dev.raul.biometria_api.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;

    @Transactional
    public Colaborador salvarComBiometria(String nome, byte[] templateCapturado){
        try {
            Colaborador colaborador = Colaborador.builder()
                    .nome(nome)
                    .template(templateCapturado)
                    .uniqueId(gerarUniqueId())
                    .build();
            return colaboradorRepository.save(colaborador);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("Erro ao salvar colaborador com biometria: violação de integridade", e);
        }
    }

    @Transactional
    public Optional<Colaborador> identificar (byte[] templateCapturado) {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();

        FtrIdentifyRecord[] rgRecords = new FtrIdentifyRecord[colaboradores.size()];
        for (int i = 0; i < colaboradores.size(); i++) {
            rgRecords[i] = new FtrIdentifyRecord();
            rgRecords[i].m_KeyValue = colaboradores.get(i).getUniqueId();
            rgRecords[i].m_Template = colaboradores.get(i).getTemplate();
        }

        Optional<Colaborador> optionalColaborador = Optional.empty();

        try {
            FutronicIdentification identification = new FutronicIdentification();
            identification.setBaseTemplate(templateCapturado);

            FtrIdentifyResult result = new FtrIdentifyResult();
            int sdkResult = identification.Identification(rgRecords, result);

            if (sdkResult == FutronicSdkBase.RETCODE_OK && result.m_Index != -1){
                optionalColaborador = Optional.of(colaboradores.get(result.m_Index));
            }
        } catch (FutronicException e) {
            throw new RuntimeException("Erro ao identificar colaborador: ", e);
        }

        return optionalColaborador;
    }

    public byte[] gerarUniqueId(){
        byte[] uniqueId = new byte[16];
        UUID uuid = UUID.randomUUID();
        long high = uuid.getMostSignificantBits();
        long low = uuid.getLeastSignificantBits();

        for (int i = 7; i >= 0 ; i--) {
            uniqueId[i] = (byte) (high & 0xFF);
            high >>>= 8;
            uniqueId[8 + i] = (byte) (low & 0xFF);
            low >>>= 8;
        }

        return uniqueId;
    }
}
