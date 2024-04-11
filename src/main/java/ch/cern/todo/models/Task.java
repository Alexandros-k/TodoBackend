package ch.cern.todo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASK")
@Getter
@Setter
@ToString
@With
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @Column(name = "TASK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "TASK_NAME", length = 100)
    private String name;

    @Column(name = "TASK_DESCRIPTION", length = 500)
    private String description;

    @Column(name = "DEADLINE")
    private LocalDateTime deadline;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "TASK+CATEGORIES_FK"))
    private TaskCategory category;
}
