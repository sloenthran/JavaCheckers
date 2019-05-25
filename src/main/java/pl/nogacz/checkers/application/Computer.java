package pl.nogacz.checkers.application;

import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnMoves;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dawid Nogacz on 25.05.2019
 */
public class Computer {
    private Random random = new Random();

    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private boolean isKickedMove = false;

    public void getGameData() {
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());

        possibleMoves.clear();
        possibleKick.clear();
        isKickedMove = false;

        for (Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if (entry.getValue().getColor().isBlack()) {
                PawnMoves moves = new PawnMoves(entry.getKey(), entry.getValue());

                if(moves.getPossibleMoves().size() > 0) {
                    possibleMoves.add(entry.getKey());
                }

                if(moves.getPossibleKick().size() > 0) {
                    possibleKick.add(entry.getKey());
                }
            }
        }
    }

    public Coordinates choosePawn() {
        if(possibleKick.size() > 0) {
            return selectRandom(possibleKick);
        } else {
            return selectRandom(possibleMoves);
        }
    }

    public Coordinates chooseMove(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(coordinates, pawn);

        Set<Coordinates> kickList = moves.getPossibleKick();
        Set<Coordinates> moveList = moves.getPossibleMoves();

        if(kickList.size() > 0) {
            isKickedMove = true;

            return selectRandom(kickList.stream()
                    .filter(entry -> Board.getPawn(entry) == null)
                    .collect(Collectors.toSet()));
        } else {
            return selectRandom(moveList);
        }
    }

    private Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }

    public boolean isKickedMove() {
        return isKickedMove;
    }
}
