package com.unina.springnatour.model.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "route_stop")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteStop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @NotNull
    @Column(name = "stop_number")
    private Integer stopNumber;
}