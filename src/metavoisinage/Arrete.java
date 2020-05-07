package metavoisinage;

public class Arrete {
    private Client clientInitial;
    private Client clientFinal;

    public Arrete(Client clientInitial, Client clientFinal) {
        this.clientInitial = clientInitial;
        this.clientFinal = clientFinal;
    }

    public Arrete(Arrete arrete) {
        this.clientInitial = arrete.clientInitial;
        this.clientFinal = arrete.clientFinal;
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
        return Math.sqrt(Math.pow((clientFinal.getX() - clientInitial.getX()), 2) + Math.pow((clientFinal.getY() - clientInitial.getY()), 2));
    }

    public int getCharge() {
        return clientFinal.getQuantite();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arrete arrete = (Arrete) o;

        if (!clientInitial.equals(arrete.clientInitial)) return false;
        return clientFinal.equals(arrete.clientFinal);
    }

    @Override
    public int hashCode() {
        int result = clientInitial.hashCode();
        result = 31 * result + clientFinal.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return clientInitial.getId() + " -> " + clientFinal.getId();
    }
}
