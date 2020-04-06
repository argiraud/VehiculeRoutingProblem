package metavoisinage;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List<Route> routes;

    public Solution(List<Route> routes) {
        this.routes = routes;
    }

    public Solution(Solution s) {
        routes = new ArrayList<Route>(s.getRoutes().size());
        s.getRoutes().forEach(route -> routes.add(new Route(route)));
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public double getDistanceTotal() {
        return routes.stream().mapToDouble(Route::getDistanceTotal).sum();
    }

    @Override
    public String toString() {
        return "Solution{" +
                "routes=" + routes.toString() + "distance=" + getDistanceTotal() +
                '}';
    }
}
