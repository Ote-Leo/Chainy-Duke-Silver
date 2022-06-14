package util;

import java.io.Serializable;

public final class Tuple<X, Y> implements Serializable {
    public final X fst;
    public final Y snd;

    public Tuple(X fst, Y snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fst == null) ? 0 : fst.hashCode());
        result = prime * result + ((snd == null) ? 0 : snd.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple other = (Tuple) obj;
        if (fst == null) {
            if (other.fst != null)
                return false;
        } else if (!fst.equals(other.fst))
            return false;
        if (snd == null) {
            if (other.snd != null)
                return false;
        } else if (!snd.equals(other.snd))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Tuple [fst=" + fst + ", snd=" + snd + "]";
    }
}