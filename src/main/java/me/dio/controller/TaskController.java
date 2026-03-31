package me.dio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.dio.controller.dto.TaskDto;
import me.dio.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks Controller", description = "RESTful API for managing tasks.")
public record TaskController(TaskService taskService) {

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all registered tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<TaskDto>> findAll() {
        var tasks = taskService.findAll();
        var tasksDto = tasks.stream().map(TaskDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(tasksDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID", description = "Retrieve a specific task based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<TaskDto> findById(@PathVariable Long id) {
        var task = taskService.findById(id);
        return ResponseEntity.ok(new TaskDto(task));
    }

    /**
     * Aqui construo a URI do recurso criado seguindo a recomendação REST,
     * para deixar claro no retorno onde a nova tarefa pode ser consultada.
     */
    @PostMapping
    @Operation(summary = "Create a new task", description = "Create a new task and return the created task data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid task data provided")
    })
    public ResponseEntity<TaskDto> create(@Valid @RequestBody TaskDto taskDto) {
        var task = taskService.create(taskDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TaskDto(task));
    }

    /**
     * No update eu sempre uso o id da URL como fonte da verdade,
     * para evitar que um body malformado atualize o recurso errado.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Update the data of an existing task based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "422", description = "Invalid task data provided")
    })
    public ResponseEntity<TaskDto> update(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        var task = taskService.update(id, taskDto.toModel());
        return ResponseEntity.ok(new TaskDto(task));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete an existing task based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

