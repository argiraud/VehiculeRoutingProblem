package metavoisinage;

public class Client {
    private Integer id;
    private Integer x;
    private Integer y;
    private Integer quantite;

    public Client(Integer id, int x, int y, Integer quantite) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.quantite = quantite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public double getDistance(Client client1, Client client2) {
        return Math.sqrt(Math.pow((client2.getX() - client1.getX()), 2) + Math.pow((client2.getY() - client1.getY()), 2));
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", quantite=" + quantite +
                '}';
    }
}
