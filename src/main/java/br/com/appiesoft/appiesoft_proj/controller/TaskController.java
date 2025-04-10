package br.com.appiesoft.appiesoft_proj.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.appiesoft.appiesoft_proj.dto.TaskDto;
import br.com.appiesoft.appiesoft_proj.model.Priority;
import br.com.appiesoft.appiesoft_proj.model.Task;
import br.com.appiesoft.appiesoft_proj.model.User;
import br.com.appiesoft.appiesoft_proj.security.UserDetailsImpl;
import br.com.appiesoft.appiesoft_proj.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private User getCurrentUser(Authentication auth) {
        return ((UserDetailsImpl) auth.getPrincipal()).getUser();
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Authentication auth) {
        return ResponseEntity.ok(taskService.getUserTasks(getCurrentUser(auth)));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDto taskDto, Authentication auth) {
        return ResponseEntity.ok(taskService.createTask(taskDto, getCurrentUser(auth)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, Authentication auth) {
        taskService.deleteTask(id, getCurrentUser(auth));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleComplete(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(taskService.toggleComplete(id, getCurrentUser(auth)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto,
            Authentication auth) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto, getCurrentUser(auth)));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filter(Authentication auth,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Priority priority) {
        return ResponseEntity.ok(taskService.filterTasks(getCurrentUser(auth), completed, priority));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Task>> getPaged(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(taskService.getPagedTasks(getCurrentUser(auth), pageable));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> stats(Authentication auth) {
        return ResponseEntity.ok(taskService.getStats(getCurrentUser(auth)));
    }
}
