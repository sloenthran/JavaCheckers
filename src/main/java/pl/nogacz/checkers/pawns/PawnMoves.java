package pl.nogacz.checkers.pawns;

import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dawid Nogacz on 18.05.2019
 */
public class PawnMoves {
    private Coordinates coordinates;
    private PawnClass pawn;

    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();

    public PawnMoves(Coordinates coordinates, PawnClass pawn) {
        this.coordinates = coordinates;
        this.pawn = pawn;

        calculateMoves();
    }

    private void calculateMoves() {
        if(pawn.getPawn().isPawn()) {
            int y = 0;

            if(pawn.getColor().isBlack()) {
                y = coordinates.getY() + 1;
            } else {
                y = coordinates.getY() - 1;
            }

            checkPawnMove(new Coordinates(coordinates.getX() + 1, y));
            checkPawnMove(new Coordinates(coordinates.getX() - 1, y));
        } else {

        }
    }

    private void checkPawnMove(Coordinates coordinates) {
        if(coordinates.isValid()) {
            if(!Board.isFieldNotNull(coordinates)) {
                if(pawn.getColor().isWhite() && coordinates.getY() == 0 || pawn.getColor().isBlack() && coordinates.getY() == 7) {
                    possiblePromote.add(coordinates);
                }

                possibleMoves.add(coordinates);
            }
        }
    }

    public Set<Coordinates> getPossibleMoves() {
        return possibleMoves;
    }

    public Set<Coordinates> getPossibleKick() {
        return possibleKick;
    }

    public Set<Coordinates> getPossiblePromote() {
        return possiblePromote;
    }
}