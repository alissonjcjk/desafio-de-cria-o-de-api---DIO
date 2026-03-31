package me.dio.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity(name = "tb_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Optei por validar o título diretamente no domínio
     * para garantir que toda tarefa tenha uma descrição mínima de contexto.
     */
    @NotBlank
    @Column(nullable = false)
    private String title;

    private String description;

    /**
     * Aqui eu uso FutureOrPresent para reforçar a ideia de planejamento
     * das tarefas, evitando prazos no passado.
     */
    @NotNull
    @FutureOrPresent
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /**
     * Usei um range simples de prioridade para manter o exemplo didático,
     * mas ainda assim mostrando como regras de negócio podem ser refletidas no modelo.
     */
    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer priority;

    /**
     * O status mostra o ciclo de vida da tarefa.
     * A validação de transição entre estados ficará concentrada no service.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}

