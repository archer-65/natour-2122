package com.unina.springnatour.model.route;

import com.unina.springnatour.model.Compilation;
import com.unina.springnatour.model.Rating;
import com.unina.springnatour.model.Report;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Range(min = 1, max = 3, message = "Difficulty must be between 1 and 3")
    @Column(name = "avg_difficulty", nullable = false)
    private Integer avgDifficulty;

    @NotNull
    @Max(value = 16, message = "Duration should not be greater than 16")
    @Column(name = "avg_duration", nullable = false)
    private Float avgDuration;

    @NotNull
    @Column(name = "disabled_friendly", nullable = false)
    private Boolean disabledFriendly = false;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.PERSIST,
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
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Rating> ratings;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Post> posts;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Report> reports;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Compilation> compilations;
}










