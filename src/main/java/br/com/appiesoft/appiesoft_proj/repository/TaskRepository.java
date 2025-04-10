package br.com.appiesoft.appiesoft_proj.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appiesoft.appiesoft_proj.model.Priority;
import br.com.appiesoft.appiesoft_proj.model.Task;
import br.com.appiesoft.appiesoft_proj.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    List<Task> findByUserAndCompleted(User user, Boolean completed);

    List<Task> findByUserAndPriority(User user, Priority priority);

    Page<Task> findByUser(User user, Pageable pageable);
}
