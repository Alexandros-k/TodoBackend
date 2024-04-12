package ch.cern.todo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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
    private Instant deadline;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "TASK+CATEGORIES_FK"))
    @JsonIgnore
    private TaskCategory category;
}
