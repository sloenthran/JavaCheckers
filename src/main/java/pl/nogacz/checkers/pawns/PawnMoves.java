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

    private boolean isKick = false;
    private Coordinates kickedCoordinates = null;

    public PawnMoves(Coordinates coordinates, PawnClass pawn) {
        this.coordinates = coordinates;
        this.pawn = pawn;

        calculateMoves();
    }

    private void calculateMoves() {
        if(pawn.getPawn().isPawn()) {
            if(pawn.getColor().isBlack()) {
                checkBottomLeft();
                checkBottomRight();
            } else {
                checkUpLeft();
                checkUpRight();
            }
        } else {
            checkUpLeft();
            checkUpRight();
            checkBottomLeft();
            checkBottomRight();
        }
    }

    private void checkUpLeft() {
        boolean checkUpLeft = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpLeft) {
                checkUpLeft = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY() - i));
            }
        }
    }

    private void checkUpRight() {
        boolean checkUpRight = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkUpRight) {
                checkUpRight = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY() - i));
            }
        }
    }

    private void checkBottomLeft() {
        boolean checkBottomLeft = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkBottomLeft) {
                checkBottomLeft = checkCoordinates(new Coordinates(coordinates.getX() - i, coordinates.getY() + i));
            }
        }
    }

    private void checkBottomRight() {
        boolean checkBottomRight = true;
        isKick = false;

        for(int i = 1; i < 8; i++) {
            if(checkBottomRight) {
                checkBottomRight = checkCoordinates(new Coordinates(coordinates.getX() + i, coordinates.getY() + i));
            }
        }
    }

    private boolean checkCoordinates(Coordinates coordinates) {
        if(!coordinates.isValid()) {
            return false;
        }

        if(Board.isFieldNotNull(coordinates)) {
            if(!Board.isThisSameColor(coordinates, pawn.getColor())) {
                kickedCoordinates = coordinates;
                isKick = true;
                return true;
            }
        } else {
            if((pawn.getColor().isWhite() && coordinates.getY() == 0 || pawn.getColor().isBlack() && coordinates.getY() == 7) && pawn.getPawn().isPawn()) {
                possiblePromote.add(coordinates);
            }

            if(isKick) {
                isKick = false;

                possibleKick.add(coordinates);
                possibleKick.add(kickedCoordinates);
                return true;
            } else {

                possibleMoves.add(coordinates);

                if(pawn.getPawn().isQueen()) {
                    return true;
                }
            }
        }

        return false;
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