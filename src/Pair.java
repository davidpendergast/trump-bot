import java.util.Objects;

public class Pair<X, Y> {
    public X x;
    public Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public static <X, Y> Pair<X, Y> get(X x, Y y) {
        return new Pair<X, Y>(x, y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair p = (Pair) other;
            return Objects.equals(x, p.x) && Objects.equals(y, p.y);
        }

        return false;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

}
