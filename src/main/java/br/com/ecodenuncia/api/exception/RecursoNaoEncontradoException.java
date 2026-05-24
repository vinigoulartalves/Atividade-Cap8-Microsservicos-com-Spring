package br.com.ecodenuncia.api.exception;

/**
 * Lancada quando um recurso (entidade) nao e encontrado pelo identificador
 * informado. Mapeada para HTTP 404 NOT FOUND pelo GlobalExceptionHandler.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
