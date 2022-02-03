package com.unina.springnatour.specification;

import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.model.route.RouteStop;
import com.unina.springnatour.model.route.RouteStop_;
import com.unina.springnatour.model.route.Route_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.ListJoin;

/**
 * This class contains methods returning Specification<T> where T is Route,
 * each method performs specific query based on CriteriaBuilder methods.
 */
public class RouteSpecifications {

    /**
     * Entry method, the one called from the outside
     * @param filter the parameter of type RouteFilter, with a role of search criteria
     * @return Specification for Route
     */
    public static Specification<Route> createRouteQuery(RouteFilter filter) {

        // Get single fields from search criteria
        String title = filter.getTitle();
        Integer difficulty = filter.getAvgDifficulty();
        Float duration = filter.getAvgDuration();
        Boolean disability = filter.getDisabledFriendly();
        Double longitude = filter.getLongitude();
        Double latitude = filter.getLatitude();

        return Specification
                // Title
                .where(title == null
                        ? null
                        : titleContains(title))
                // Difficulty
                .and(difficulty == null
                        ? null
                        : isDifficultyGreaterThan(difficulty))
                // Duration
                .and(duration == null
                        ? null
                        : isDurationGreaterThan(duration))
                // Disability
                .and(disability == null
                        ? null
                        : isDisabledFriendly(disability))
                // Position
                .and(longitude == null || latitude == null
                        ? null :
                        positionIs(filter.getLongitude(), filter.getLatitude()));
    }

    // Keyword search
    public static Specification<Route> titleContains(String title) {
        return (root, query, builder) ->
                builder.like(root.get("title"), title);
    }

    // Difficulty greater than or equal
    public static Specification<Route> isDifficultyGreaterThan(Integer avgDifficulty) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("avgDifficulty"), avgDifficulty);
    }

    // Duration greater than or equal
    public static Specification<Route> isDurationGreaterThan(Float avgDuration) {
        return ((root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("avgDuration"), avgDuration));
    }

    // Disabled friendly?
    public static Specification<Route> isDisabledFriendly(Boolean disabledFriendly) {
        return (root, query, builder) ->
                builder.equal(root.get("disabledFriendly"), disabledFriendly);
    }

    // Position query
    public static Specification<Route> positionIs(Double longitude, Double latitude) {

        return (root, query, builder) -> {

            // Join between Route and RouteStop, on stops
            ListJoin<Route, RouteStop> stopJoin = root.join(Route_.stops);

            return
                    builder.and(
                            builder.equal(stopJoin.get(RouteStop_.longitude), longitude),
                            builder.equal(stopJoin.get(RouteStop_.latitude), latitude)
                    );
        };
    }
}
