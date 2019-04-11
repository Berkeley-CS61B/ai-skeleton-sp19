package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2c.routing.RouteSolverFactory;
import bearmaps.proj2c.Router;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bearmaps.proj2c.utils.Constants.GRAPH_DB_INSTANCE;
import static bearmaps.proj2c.utils.Constants.ROUTE_LIST;

/**
 * Created by rahul
 */
public class RoutingAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each route request to the bearmaps.proj2c.server will have the following parameters
     * as keys in the params map.<br>
     * start_lat : start point latitude,<br> start_lon : start point longitude,<br>
     * end_lat : end point latitude, <br>end_lon : end point longitude.
     **/
    private static final String[] REQUIRED_ROUTE_REQUEST_PARAMS = {"start_lat", "start_lon",
            "end_lat", "end_lon"};

    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_ROUTE_REQUEST_PARAMS);
    }

    @Override
    protected Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        List<Long> route = Router.shortestPath(RouteSolverFactory.getRouteSolver(RoutingAlgo.A_STAR),
                GRAPH_DB_INSTANCE,
                requestParams.get("start_lon"), requestParams.get("start_lat"),
                requestParams.get("end_lon"), requestParams.get("end_lat"));
        ROUTE_LIST.clear();
        ROUTE_LIST.addAll(route);
        String directions = getDirectionsText();

        Map<String, Object> routeParams = new HashMap<>();
        routeParams.put("routing_success", !route.isEmpty());
        routeParams.put("directions_success", directions.length() > 0);
        routeParams.put("directions", directions);
        return routeParams;
    }

    /**
     * Takes the route of this bearmaps.proj2c.MapServer and converts it into an HTML friendly
     * String to be passed to the frontend.
     */
    private String getDirectionsText() {

        List<Router.NavigationDirection> directions = Router.routeDirections(GRAPH_DB_INSTANCE, ROUTE_LIST);
        if (directions == null || directions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int step = 1;
        for (Router.NavigationDirection d: directions) {
            sb.append(String.format("%d. %s <br>", step, d));
            step += 1;
        }
        return sb.toString();
    }
}
