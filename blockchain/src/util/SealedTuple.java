package util;

public final class SealedTuple<X, Y> {
    private final X fst;
    private final Y pass;

    public SealedTuple(X fst, Y pass) {
        this.fst = fst;
        this.pass = pass;
    }

    public X getFst(String pass) {
        return (this.pass.equals(pass)) ? fst : null;
    }
}
