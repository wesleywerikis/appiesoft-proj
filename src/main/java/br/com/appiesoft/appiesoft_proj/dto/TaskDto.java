package br.com.appiesoft.appiesoft_proj.dto;

import java.time.LocalDate;

import br.com.appiesoft.appiesoft_proj.model.Priority;
import lombok.Data;

@Data
public class TaskDto {
    
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;

}
