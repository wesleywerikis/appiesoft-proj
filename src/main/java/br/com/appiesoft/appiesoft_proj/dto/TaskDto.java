package br.com.appiesoft.appiesoft_proj.dto;

import java.time.LocalDate;

import br.com.appiesoft.appiesoft_proj.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskDto {
    
    @NotBlank(message = "O título é obrigatório")
    private String title;

    private String description;
    
    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dueDate;

    @NotNull(message = "A prioridade é obrigatória")
    private Priority priority;

}
