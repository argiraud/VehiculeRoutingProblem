package metavoisinage;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<Arrete> arretes;

    public Route(List<Arrete> arretes) {
        this.arretes = arretes;
    }

    public Route(Route route) {
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
    public String toString() {
        return "Route{" +
                "arretes=" + arretes +
                ", " + getChargeTotal() + '}';
    }
}
