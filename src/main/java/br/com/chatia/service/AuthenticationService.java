package br.com.chatia.service;

import br.com.chatia.model.User;
import br.com.chatia.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationService {
    private final UserRepository repository = new UserRepository();

    public User register(String name, String email, String password, String confirmation) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Informe o nome.");
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Informe um e-mail válido.");
        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("A senha deve possuir pelo menos 8 caracteres.");
        if (!password.equals(confirmation))
            throw new IllegalArgumentException("As senhas não coincidem.");
        if (repository.existsByEmail(email))
            throw new IllegalArgumentException("Este e-mail já está cadastrado.");

        User user = new User(name.trim(), email.trim().toLowerCase(),
                BCrypt.hashpw(password, BCrypt.gensalt(12)));
        repository.save(user);
        return user;
    }

    public User login(String email, String password) {
        User user = repository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos."));

        if (!BCrypt.checkpw(password, user.getPasswordHash()))
            throw new IllegalArgumentException("Usuário ou senha inválidos.");

        return user;
    }
}
