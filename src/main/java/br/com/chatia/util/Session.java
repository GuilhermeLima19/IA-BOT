package br.com.chatia.util;

import br.com.chatia.model.User;

public final class Session {
    private static User currentUser;

    private Session() {}

    public static void login(User user) {
        currentUser = user;
    }

    public static User getUser() {
        if (currentUser == null) throw new IllegalStateException("Nenhum usuário autenticado.");
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
