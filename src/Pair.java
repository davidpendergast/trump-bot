import java.util.List;
import java.util.Objects;

public class Pair<X,Y> {
    public X x;
    public Y y;
    
    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair p = (Pair)other;
            return Objects.equals(x, p.x) && Objects.equals(y, p.y);
            
        }
        
        return false;
    }
    
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
