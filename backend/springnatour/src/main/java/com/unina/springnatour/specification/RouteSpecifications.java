package com.unina.springnatour.specification;

import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.model.route.RouteStop;
import com.unina.springnatour.model.route.RouteStop_;
import com.unina.springnatour.model.route.Route_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;

public class RouteSpecifications {

    public static Specification<Route> createRouteQuery(RouteFilter filter) {

        String title = filter.getTitle();
        Integer difficulty = filter.getAvgDifficulty();
        Float duration = filter.getAvgDuration();
        Boolean disability = filter.getDisabilitySafe();
        Double longitude = filter.getLongitude();
        Double latitude = filter.getLatitude();

        return Specification
                .where(title == null ? null : titleContains(title))
                .and(difficulty == null ? null : isDifficultyGreaterThan(difficulty))
                .and(duration == null ? null : isDurationGreaterThan(duration))
                .and(disability == null ? null : isDisabilitySafe(disability))
                .and(longitude == null || latitude == null ? null : positionIs(filter.getLongitude(), filter.getLatitude()));
    }

    public static Specification<Route> titleContains(String title) {
        return (root, query, builder) ->
                builder.like(root.get("title"), title);
    }

    public static Specification<Route> isDifficultyGreaterThan(Integer avgDifficulty) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("avgDifficulty"), avgDifficulty);
    }

    public static Specification<Route> isDurationGreaterThan(Float avgDuration) {
        return ((root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("avgDuration"), avgDuration));
    }

    public static Specification<Route> isDisabilitySafe(Boolean disabilitySafe) {
        return (root, query, builder) ->
                builder.equal(root.get("disabilitySafe"), disabilitySafe);
    }

    public static Specification<Route> positionIs(Double longitude, Double latitude) {

        return (root, query, builder) -> {
            ListJoin<Route, RouteStop> stopJoin = root.join(Route_.stops);

            return
                    builder.and(
                            builder.equal(stopJoin.get(RouteStop_.longitude), longitude),
                            builder.equal(stopJoin.get(RouteStop_.latitude), latitude)
                    );
        };
    }
}
