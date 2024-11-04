package br.com.fiap.previsaoSafra.Auth;

import br.com.fiap.previsaoSafra.service.UsuarioService;
import br.com.fiap.previsaoSafra.service.MensagemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class LoginListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UsuarioService usuarioService;
    private final MensagemService mensagemService;

    public LoginListener(UsuarioService usuarioService, MensagemService mensagemService) {
        this.usuarioService = usuarioService;
        this.mensagemService = mensagemService;
    }

    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof OAuth2User oAuth2User) {
            String name = Optional.ofNullable((String) oAuth2User.getAttribute("name")).orElse("Nome Desconhecido");
            String email = Optional.ofNullable((String) oAuth2User.getAttribute("email")).orElse("Email Desconhecido");

            usuarioService.create(oAuth2User);

            log.info("Usuário autenticado com sucesso - Nome: {}, Email: {}", name, email);

            String welcomeMessage = "Bem-vindo ao CPA, " + name + "!";
            mensagemService.enviarMensagem(welcomeMessage, email);
        } else {
            log.warn("Usuário autenticado não é uma instância de OAuth2User: {}", principal.getClass().getName());
        }
    }
}
