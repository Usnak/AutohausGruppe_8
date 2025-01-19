import java.util.ArrayList;
import java.util.List;

public class AutoManager {
    private final List<Auto> autos;

    public AutoManager() {
        autos = new ArrayList<>();
    }

    public void addAuto(Auto auto) {
        autos.add(auto);
    }

    public void removeAuto(int index) {
        if (index >= 0 && index < autos.size()) {
            autos.remove(index);
        }
    }

    public List<Auto> getAutos() {
        return autos;
    }
}
