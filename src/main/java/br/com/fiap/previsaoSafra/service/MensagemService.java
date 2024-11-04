package br.com.fiap.previsaoSafra.service;

import br.com.fiap.previsaoSafra.mail.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Slf4j
public class MensagemService {

    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;
    
    private final Queue<Mensagem> filaMensagensPendentes = new LinkedList<>();

    @Value("${spring.rabbitmq.queue}")
    private String emailQueue;

    public MensagemService(RabbitTemplate rabbitTemplate, EmailService emailService) {
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
    }

    private static class Mensagem {
        String conteudo;
        String destinatarioEmail;

        Mensagem(String conteudo, String destinatarioEmail) {
            this.conteudo = conteudo;
            this.destinatarioEmail = destinatarioEmail;
        }
    }

    public void enviarMensagem(String mensagem, String destinatarioEmail) {
        Mensagem novaMensagem = new Mensagem(mensagem, destinatarioEmail);
        filaMensagensPendentes.add(novaMensagem);
        tentarEnviarMensagem(novaMensagem);
    }

    private void tentarEnviarMensagem(Mensagem mensagem) {
        try {
            rabbitTemplate.convertAndSend(emailQueue, mensagem.conteudo);
            log.info("Mensagem enviada ao RabbitMQ: {}", mensagem.conteudo);
        } catch (AmqpConnectException e) {
            log.warn("Falha na conexão com RabbitMQ. Enviando e-mail diretamente: {}", e.getMessage());
            emailService.sendEmail(mensagem.destinatarioEmail, "Bem-vindo ao CPA", mensagem.conteudo);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processarFila() {
        if (!filaMensagensPendentes.isEmpty()) {
            log.info("Tentando reenviar mensagens da fila pendente...");
            while (!filaMensagensPendentes.isEmpty()) {
                Mensagem mensagemPendente = filaMensagensPendentes.poll();
                tentarEnviarMensagem(mensagemPendente);
            }
        }
    }
}
