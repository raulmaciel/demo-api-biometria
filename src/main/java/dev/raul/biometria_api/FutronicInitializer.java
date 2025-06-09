package dev.raul.biometria_api;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FutronicInitializer {

    @PostConstruct
    public void load(){
        try {
            System.loadLibrary("ftrJSDK");
            log.info("\n\n-=-=-=- SDK da Futronic foi carregado com sucesso! -=-=-=-\n\n");
        }catch (UnsatisfiedLinkError er){
            log.error(er.getMessage());
        }
    }
}
