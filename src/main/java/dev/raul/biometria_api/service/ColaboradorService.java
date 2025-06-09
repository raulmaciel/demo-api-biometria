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

@RequiredArgsConstructor
@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;

    @Transactional
    public Colaborador salvar(Colaborador colaborador){
        try {
            return colaboradorRepository.save(colaborador);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("Erro ao salvar colaborador: violação de integridade de dados", e);
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
}
