package com.unina.springnatour.model.chat;

import com.unina.springnatour.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @NotNull
    @Column(name = "sent_on", nullable = false)
    private LocalDateTime sentOn;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;
}
