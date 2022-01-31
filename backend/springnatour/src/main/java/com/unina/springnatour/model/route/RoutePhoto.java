package com.unina.springnatour.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "route_photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutePhoto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @URL
    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Route route;
}
