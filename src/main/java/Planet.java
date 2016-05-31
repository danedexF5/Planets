import java.util.ArrayList;

/**
 * Created by danedexheimer on 5/12/16.
 */
public class Planet {
    String id;
    String name;
    int radius;
    boolean supportsLife;
    double distanceFromSun;

    ArrayList<Moon> moons = new ArrayList<>();
}
