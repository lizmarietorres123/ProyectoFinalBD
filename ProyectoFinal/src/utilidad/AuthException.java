package utilidad;

public class AuthException extends Exception {
    private static final long serialVersionUID = 1L;

    public AuthException() {
        super("Usuario o contraseña incorrectos.");
    }

    public AuthException(String message) {
        super(message);
    }
}