package com.unina.springnatour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "sent_on", nullable = false)
    private LocalDateTime sentOn;

//    @Column(
//            name = "sender",
//            columnDefinition = " char NOT NULL CHECK (sender IN ('1', '2'))"
//    )
//    private String sender;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;
}
