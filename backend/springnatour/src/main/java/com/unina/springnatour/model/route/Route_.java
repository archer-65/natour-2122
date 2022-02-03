package com.unina.springnatour.model.route;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Route.class)
public class Route_ {
    public static volatile ListAttribute<Route, RouteStop> stops;
}
