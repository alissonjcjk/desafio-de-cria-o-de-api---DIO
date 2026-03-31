package me.dio.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.dio.domain.model.Task;
import me.dio.domain.model.TaskStatus;

/**
 * Neste DTO eu centralizo o contrato da API,
 * separando o modelo de domínio da forma como os dados trafegam na borda HTTP.
 */
public record TaskDto(
        Long id,
        @NotBlank String title,
        String description,
        @NotNull @FutureOrPresent LocalDate dueDate,
        @NotNull @Min(1) @Max(5) Integer priority,
        @NotNull TaskStatus status
) {

    public TaskDto(Task model) {
        this(
                model.getId(),
                model.getTitle(),
                model.getDescription(),
                model.getDueDate(),
                model.getPriority(),
                model.getStatus()
        );
    }

    public Task toModel() {
        Task model = new Task();
        model.setId(this.id);
        model.setTitle(this.title);
        model.setDescription(this.description);
        model.setDueDate(this.dueDate);
        model.setPriority(this.priority);
        model.setStatus(this.status);
        return model;
    }
}

