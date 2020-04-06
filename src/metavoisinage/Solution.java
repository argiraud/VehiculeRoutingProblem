package metavoisinage;

import java.util.List;

public class Solution {
    private List<Route> routes;

    public Solution(List<Route> routes) {
        this.routes = routes;
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
                "routes=" + routes + "distance=" + getDistanceTotal() +
                '}';
    }
}
