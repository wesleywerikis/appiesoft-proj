package br.com.appiesoft.appiesoft_proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appiesoft.appiesoft_proj.model.Task;
import br.com.appiesoft.appiesoft_proj.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);
}
