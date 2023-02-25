package jacques.raul.uv.es;

import java.util.Comparator;

public class CampingsComparator implements Comparator<Camping> {
    @Override
    public int compare(Camping c1, Camping c2) {
        return c1.getNombre().compareToIgnoreCase(c2.getNombre());
    }
}
