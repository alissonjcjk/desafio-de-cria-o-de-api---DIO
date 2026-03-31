package me.dio.service;

import me.dio.domain.model.Task;

/**
 * Aqui eu reaproveito o contrato genérico de CRUD
 * para manter o padrão do projeto e deixar o código mais enxuto.
 */
public interface TaskService extends CrudService<Long, Task> {
}

