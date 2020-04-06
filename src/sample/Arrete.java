package sample;

public class Arrete {
    private Client clientInitial;
    private Client clientFinal;
    private double distance;

    public Arrete(Client clientInitial, Client clientFinal, double distance) {
        this.clientInitial = clientInitial;
        this.clientFinal = clientFinal;
        this.distance = distance;
    }

    public Client getClientInitial() {
        return clientInitial;
    }

    public void setClientInitial(Client clientInitial) {
        this.clientInitial = clientInitial;
    }

    public Client getClientFinal() {
        return clientFinal;
    }

    public void setClientFinal(Client clientFinal) {
        this.clientFinal = clientFinal;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Arrete{" +
                "clientInitial=" + clientInitial +
                ", clientFinal=" + clientFinal +
                ", distance=" + distance +
                '}';
    }
}
