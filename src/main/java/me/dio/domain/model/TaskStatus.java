package me.dio.domain.model;

/**
 * Enum para o status da tarefa.
 * Escolhi um enum para deixar o domínio mais expressivo e
 * limitar os valores possíveis diretamente no tipo.
 */
public enum TaskStatus {

    PENDING,
    IN_PROGRESS,
    DONE
}

