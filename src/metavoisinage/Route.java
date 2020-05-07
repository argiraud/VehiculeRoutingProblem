package metavoisinage;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private List<Arrete> arretes;

    public Route(Integer id,List<Arrete> arretes) {
        this.id = id;
        this.arretes = arretes;
    }

    public Route(Route route) {
        this.id = route.getId();
        arretes = new ArrayList<>(route.getArretes().size());
        route.getArretes().forEach(a -> arretes.add(new Arrete(a)));
    }

    public List<Arrete> getArretes() {
        return arretes;
    }

    public Arrete getArretesById(int i) {
        return arretes.get(i);
    }

    public void setArretes(List<Arrete> arretes) {
        this.arretes = arretes;
    }

    public void addArrete(Arrete arrete) {
        arretes.add(arrete);
    }


    public double getDistanceTotal() {
        return arretes.stream().mapToDouble(Arrete::getDistance).sum();
    }

    public double getChargeTotal() {
        return arretes.stream().mapToDouble(Arrete::getCharge).sum();
    }

    public int size() {
        return this.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (!id.equals(route.id)) return false;
        return arretes.equals(route.arretes);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + arretes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "arretes=" + arretes +
                ", " + getChargeTotal() + '}';
    }
}
