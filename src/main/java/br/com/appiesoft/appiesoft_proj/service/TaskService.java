package br.com.appiesoft.appiesoft_proj.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.appiesoft.appiesoft_proj.dto.TaskDto;
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
}
