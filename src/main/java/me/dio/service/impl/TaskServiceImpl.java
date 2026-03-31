package me.dio.service.impl;

import me.dio.domain.model.Task;
import me.dio.domain.model.TaskStatus;
import me.dio.domain.repository.TaskRepository;
import me.dio.service.TaskService;
import me.dio.service.exception.BusinessException;
import me.dio.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return this.taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return this.taskRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Task create(Task taskToCreate) {
        ofNullable(taskToCreate).orElseThrow(() -> new BusinessException("Task to create must not be null."));
        // Aqui eu centralizo as validações de negócio da criação.
        validateRequiredFields(taskToCreate);
        return this.taskRepository.save(taskToCreate);
    }

    @Transactional
    public Task update(Long id, Task taskToUpdate) {
        Task dbTask = this.findById(id);
        if (taskToUpdate.getId() != null && !id.equals(taskToUpdate.getId())) {
            throw new BusinessException("Path ID and body ID must be the same.");
        }

        // Para manter o exemplo focado, eu apenas atualizo todos os campos
        // e delego a validação de transição de status para um método dedicado.
        applyUpdatableFields(dbTask, taskToUpdate);
        validateStatusTransition(dbTask.getStatus(), taskToUpdate.getStatus());
        validateRequiredFields(dbTask);

        return this.taskRepository.save(dbTask);
    }

    @Transactional
    public void delete(Long id) {
        Task dbTask = this.findById(id);
        this.taskRepository.delete(dbTask);
    }

    private void validateRequiredFields(Task task) {
        ofNullable(task.getTitle()).orElseThrow(() -> new BusinessException("Task title must not be null."));
        ofNullable(task.getDueDate()).orElseThrow(() -> new BusinessException("Task due date must not be null."));
        ofNullable(task.getPriority()).orElseThrow(() -> new BusinessException("Task priority must not be null."));
        ofNullable(task.getStatus()).orElseThrow(() -> new BusinessException("Task status must not be null."));
    }

    private void applyUpdatableFields(Task dbTask, Task source) {
        dbTask.setTitle(source.getTitle());
        dbTask.setDescription(source.getDescription());
        dbTask.setDueDate(source.getDueDate());
        dbTask.setPriority(source.getPriority());
        dbTask.setStatus(source.getStatus());
    }

    /**
     * Para fins didáticos, eu defini uma regra bem simples:
     * PENDING -> IN_PROGRESS -> DONE. Qualquer outra combinação é considerada inválida.
     */
    private void validateStatusTransition(TaskStatus current, TaskStatus next) {
        if (current == null || next == null) {
            return;
        }
        if (current == next) {
            return;
        }

        boolean valid =
                (current == TaskStatus.PENDING && next == TaskStatus.IN_PROGRESS) ||
                (current == TaskStatus.IN_PROGRESS && next == TaskStatus.DONE);

        if (!valid) {
            throw new BusinessException("Invalid status transition from %s to %s.".formatted(current, next));
        }
    }
}

