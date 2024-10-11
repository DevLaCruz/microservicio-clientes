package com.nttdata.CustomerMs.exception;

public class ClienteException extends RuntimeException {
    public ClienteException(String mensaje) {
        super(mensaje);
    }

    public ClienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
