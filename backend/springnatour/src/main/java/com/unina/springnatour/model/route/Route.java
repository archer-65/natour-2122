package com.unina.springnatour.model.route;

import com.unina.springnatour.model.*;
import com.unina.springnatour.model.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "route")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Route implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Size(min = 1, max = 3, message = "Difficulty must be between 1 and 3")
    @Column(name = "avg_difficulty", nullable = false)
    private Integer avgDifficulty;

    @NotNull
    @Max(value = 16, message = "Duration should not be greater than 16")
    @Column(name = "avg_duration", nullable = false)
    private Float avgDuration;

    @NotNull
    @Column(name = "disability_safe", nullable = false)
    private Boolean disabilitySafe = false;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RoutePhoto> photos;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RouteStop> stops;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Rating> ratings;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> posts;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Report> reports;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Compilation> compilations;
}










