package br.com.appiesoft.appiesoft_proj.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.appiesoft.appiesoft_proj.dto.TaskDto;
import br.com.appiesoft.appiesoft_proj.model.Priority;
import br.com.appiesoft.appiesoft_proj.model.Task;
import br.com.appiesoft.appiesoft_proj.model.User;
import br.com.appiesoft.appiesoft_proj.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getUserTasks(User user) {
        return taskRepository.findByUser(user);
    }

    public Task createTask(TaskDto taskDto, User user) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());
        task.setCompleted(false);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        taskRepository.delete(task);
    }

    public Task toggleComplete(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        task.setCompleted(!task.getCompleted());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, TaskDto taskDto, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());
        return taskRepository.save(task);
    }

    public List<Task> filterTasks(User user, Boolean completed, Priority priority) {
        if (completed != null) {
            return taskRepository.findByUserAndCompleted(user, completed);
        }

        if (priority != null) {
            return taskRepository.findByUserAndPriority(user, priority);
        }
        return taskRepository.findByUser(user);
    }

    public Page<Task> getPagedTasks(User user, Pageable pageable) {
        return taskRepository.findByUser(user, pageable);
    }

    public Map<String, Long> getStats(User user) {
        List<Task> tasks = taskRepository.findByUser(user);
        long total = tasks.size();
        long completed = tasks.stream().filter(Task::getCompleted).count();
        long pending = total - completed;

        return Map.of(
                "total", total,
                "completed", completed,
                "pending", pending);
    }
}
