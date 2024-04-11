package ch.cern.todo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "TASK_CATEGORY",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"CATEGORY_NAME"},
                name = "CATEGORY_NAME_UK")
        })
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCategory {

    @Id
    @Column(name = "CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "CATEGORY_NAME", length = 100, unique = true)
    private String name;
    @Column(name = "CATEGORY_DESCRIPTION", length = 500)
    private String description;
}
