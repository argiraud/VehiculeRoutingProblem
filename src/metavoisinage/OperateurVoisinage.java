package metavoisinage;

import java.util.Random;

public class OperateurVoisinage {

    public static final Random r = new Random();

    public OperateurVoisinage() {

    }

    public static Solution inversePointsArretes(Solution routes) {
        int c = r.nextInt(routes.getRoutes().size());
        Route route = routes.getRoutes().get(c);
        int i = r.nextInt(route.getArretes().size());
        while (i <= 1 || i >= route.getArretes().size() - 2) {
            i = r.nextInt(route.getArretes().size());
        }
        Client client1 = route.getArretes().get(i).getClientInitial();
        Client client2 = route.getArretes().get(i).getClientFinal();

        route.getArretes().get(i - 1).setClientFinal(client2);
        route.getArretes().get(i + 1).setClientInitial(client1);

        route.getArretes().get(i).setClientInitial(client2);
        route.getArretes().get(i).setClientFinal(client1);

        return routes;
    }

    public static Solution echangePointsBetweenRoutes(Solution routes) {
        int m = r.nextInt(routes.getRoutes().size() - 1);
        int n = r.nextInt(routes.getRoutes().size() - 1);
        while (n == m) {
            n = r.nextInt(routes.getRoutes().size() - 1);
        }
        Route route1 = routes.getRoutes().get(m);
        Route route2 = routes.getRoutes().get(n);

        int i = r.nextInt(route1.getArretes().size());
        while (i <= 1 || i >= route1.getArretes().size() - 2) {
            i = r.nextInt(route1.getArretes().size());
        }
        int j = r.nextInt(route2.getArretes().size());
        while (j <= 1 || j >= route2.getArretes().size() - 2) {
            j = r.nextInt(route2.getArretes().size());
        }
        while ((route1.getChargeTotal() + route2.getArretes().get(j).getCharge() - route1.getArretes().get(i).getCharge()) > 100 ||
                (route2.getChargeTotal() + route1.getArretes().get(i).getCharge() - route2.getArretes().get(j).getCharge()) > 100) {
            m = r.nextInt(routes.getRoutes().size());
            n = r.nextInt(routes.getRoutes().size());
            while (n == m) {
                n = r.nextInt(routes.getRoutes().size());
            }
            route1 = routes.getRoutes().get(m);
            route2 = routes.getRoutes().get(n);
            i = r.nextInt(route1.getArretes().size());
            while (i <= 1 || i >= route1.getArretes().size() - 2) {
                i = r.nextInt(route1.getArretes().size());
            }
            j = r.nextInt(route2.getArretes().size());
            while (j <= 1 || j >= route2.getArretes().size() - 2) {
                j = r.nextInt(route2.getArretes().size());
            }
        }
        Client client1 = route1.getArretes().get(i).getClientFinal();
        Client client2 = route2.getArretes().get(j).getClientFinal();

        route1.getArretes().get(i).setClientFinal(client2);
        route1.getArretes().get(i + 1).setClientInitial(client2);

        route2.getArretes().get(j).setClientFinal(client1);
        route2.getArretes().get(j + 1).setClientInitial(client1);

        return routes;
    }


    public static Solution crossArreteInsideRoute(Solution routes) {
        int c = r.nextInt(routes.getRoutes().size());
        Route route = routes.getRoutes().get(c);
        int i = r.nextInt(route.getArretes().size());
        while (i <= 1 || i >= route.getArretes().size() - 2) {
            i = r.nextInt(route.getArretes().size());
        }
        int j = r.nextInt(route.getArretes().size());
        while (j <= 1 || j >= route.getArretes().size() - 2 || j == i || j == i + 1 || j == i - 1) {
            j = r.nextInt(route.getArretes().size());
        }
        Client client1 = route.getArretes().get(i).getClientInitial();
        Client client2 = route.getArretes().get(i).getClientFinal();
        Client client3 = route.getArretes().get(j).getClientInitial();
        Client client4 = route.getArretes().get(j).getClientFinal();

        route.getArretes().get(i - 1).setClientFinal(client3);
        route.getArretes().get(i + 1).setClientInitial(client4);

        route.getArretes().get(j - 1).setClientFinal(client1);
        route.getArretes().get(j + 1).setClientInitial(client2);

        route.getArretes().get(i).setClientInitial(client3);
        route.getArretes().get(i).setClientFinal(client4);

        route.getArretes().get(j).setClientInitial(client1);
        route.getArretes().get(j).setClientFinal(client2);

        return routes;
    }

    public static Solution crossArreteBetweenRoutes(Solution routes) {
        int m = r.nextInt(routes.getRoutes().size());
        int n = r.nextInt(routes.getRoutes().size());
        while (n == m) {
            n = r.nextInt(routes.getRoutes().size());
        }
        Route route1 = routes.getRoutes().get(m);
        Route route2 = routes.getRoutes().get(n);

        int i = r.nextInt(route1.getArretes().size());
        while (i <= 1 || i >= route1.getArretes().size() - 2) {
            i = r.nextInt(route1.getArretes().size());
        }
        int j = r.nextInt(route2.getArretes().size());
        while (j <= 1 || j >= route2.getArretes().size() - 2) {
            j = r.nextInt(route2.getArretes().size());
        }
        while ((route1.getChargeTotal() + route2.getArretes().get(j - 1).getCharge() + route2.getArretes().get(j).getCharge()
                - route1.getArretes().get(i - 1).getCharge() - route1.getArretes().get(i - 1).getCharge()) > 100 ||
                (route2.getChargeTotal() + route1.getArretes().get(i - 1).getCharge() + route1.getArretes().get(i).getCharge()
                        - route2.getArretes().get(j - 1).getCharge() - route2.getArretes().get(j - 1).getCharge()) > 100) {
            i = r.nextInt(route1.getArretes().size());
            while (i <= 1 || i >= route1.getArretes().size() - 2) {
                i = r.nextInt(route1.getArretes().size());
            }
            j = r.nextInt(route2.getArretes().size());
            while (j <= 1 || j >= route2.getArretes().size() - 2 || j == i || j == i + 1 || j == i - 1) {
                j = r.nextInt(route2.getArretes().size());
            }
        }
        Client client1 = route1.getArretes().get(i).getClientInitial();
        Client client2 = route1.getArretes().get(i).getClientFinal();
        Client client3 = route2.getArretes().get(j).getClientInitial();
        Client client4 = route2.getArretes().get(j).getClientFinal();

        route1.getArretes().get(i - 1).setClientFinal(client3);
        route1.getArretes().get(i + 1).setClientInitial(client4);

        route2.getArretes().get(j - 1).setClientFinal(client1);
        route2.getArretes().get(j + 1).setClientInitial(client2);

        route1.getArretes().get(i).setClientInitial(client3);
        route1.getArretes().get(i).setClientFinal(client4);

        route2.getArretes().get(j).setClientInitial(client1);
        route2.getArretes().get(j).setClientFinal(client2);

        return routes;
    }
}
