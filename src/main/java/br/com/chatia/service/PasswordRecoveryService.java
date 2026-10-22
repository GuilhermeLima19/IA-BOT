package br.com.chatia.service;

import br.com.chatia.model.PasswordResetToken;
import br.com.chatia.model.User;
import br.com.chatia.repository.PasswordResetRepository;
import br.com.chatia.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.time.Instant;

public class PasswordRecoveryService {
    private final UserRepository userRepository = new UserRepository();
    private final PasswordResetRepository tokenRepository = new PasswordResetRepository();
    private final SecureRandom random = new SecureRandom();

    public String createToken(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("E-mail não encontrado."));

        String raw = String.format("%08d", random.nextInt(100_000_000));
        tokenRepository.save(new PasswordResetToken(
                user.getId(),
                BCrypt.hashpw(raw, BCrypt.gensalt(12)),
                Instant.now().plusSeconds(900)
        ));

        // No ambiente real este token deve ser enviado por e-mail/SMS.
        // Para a demonstração acadêmica ele é retornado para ser exibido na tela.
        return raw;
    }

    public void reset(String email, String token, String newPassword) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Dados de recuperação inválidos."));

        throw new UnsupportedOperationException(
                "Para produção, conecte o envio de e-mail e a validação do token ao PasswordResetRepository."
        );
    }
}
