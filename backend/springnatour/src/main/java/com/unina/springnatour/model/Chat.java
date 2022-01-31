package com.unina.springnatour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", referencedColumnName = "id")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", referencedColumnName = "id")
    private User user2;

    @OneToMany(
            mappedBy = "chat",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Message> messages;
}
