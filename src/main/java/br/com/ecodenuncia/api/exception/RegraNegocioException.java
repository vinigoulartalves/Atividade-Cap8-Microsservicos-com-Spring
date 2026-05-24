package br.com.ecodenuncia.api.exception;

/**
 * Lancada quando uma regra de negocio e violada (ex.: cadastro com email
 * duplicado, nome de categoria ja existente, etc.). Mapeada para HTTP 400
 * BAD REQUEST pelo GlobalExceptionHandler.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
