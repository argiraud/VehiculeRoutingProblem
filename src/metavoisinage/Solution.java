package metavoisinage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    private List<Route> routes;

    public Solution(){

    };

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

    public Map<Client,Integer> getAllClients(){
        Map<Client,Integer> map = new HashMap<> ();
        routes.forEach(route -> {
            route.getArretes().forEach(arrete -> {
                map.put(arrete.getClientFinal(),route.getId());
            });
        });
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Solution solution = (Solution) o;

        return routes.equals(solution.routes);
    }

    @Override
    public int hashCode() {
        return routes.hashCode();
    }

    @Override
    public String toString() {
        return "Solution{" +
                "routes=" + routes.toString() + "distance=" + getDistanceTotal() +
                '}';
    }
}
