package com.unina.springnatour.model;

import com.unina.springnatour.model.route.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "compilation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compilation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @URL
    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Route> routes;
}
