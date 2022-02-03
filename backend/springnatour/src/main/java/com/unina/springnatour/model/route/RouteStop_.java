package com.unina.springnatour.model.route;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RouteStop.class)
public class RouteStop_ {
    public static volatile SingularAttribute<RouteStop, Double> longitude;
    public static volatile SingularAttribute<RouteStop, Double> latitude;
}
