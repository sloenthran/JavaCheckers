package pl.nogacz.checkers.pawns;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public enum Pawn {
    PAWN,
    QUEEN;

    public boolean isPawn() {
        return this == PAWN;
    }

    public boolean isQueen() {
        return this == QUEEN;
    }
}
