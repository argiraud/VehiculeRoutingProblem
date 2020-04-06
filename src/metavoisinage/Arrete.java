package metavoisinage;

import metavoisinage.Client;

public class Arrete {
    private metavoisinage.Client clientInitial;
    private metavoisinage.Client clientFinal;

    public Arrete(metavoisinage.Client clientInitial, metavoisinage.Client clientFinal) {
        this.clientInitial = clientInitial;
        this.clientFinal = clientFinal;
    }

    public metavoisinage.Client getClientInitial() {
        return clientInitial;
    }

    public void setClientInitial(metavoisinage.Client clientInitial) {
        this.clientInitial = clientInitial;
    }

    public metavoisinage.Client getClientFinal() {
        return clientFinal;
    }

    public void setClientFinal(Client clientFinal) {
        this.clientFinal = clientFinal;
    }

    public double getDistance() {
        return Math.sqrt(Math.pow((clientFinal.getX() - clientInitial.getX()), 2) + Math.pow((clientFinal.getY() - clientInitial.getY()), 2));
    }

    public int getCharge(){
        return clientFinal.getQuantite();
    }

    @Override
    public String toString() {
        return clientInitial.getId() + " -> " + clientFinal.getId();
    }
}
