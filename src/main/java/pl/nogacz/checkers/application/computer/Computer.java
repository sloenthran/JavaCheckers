package pl.nogacz.checkers.application.computer;

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
    private BoardPoint boardPoint = new BoardPoint();

    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKickAndNotIsEnemyKickMe = new HashSet<>();

    public void getGameData() {
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());

        possibleMoves.clear();
        possibleKick.clear();
        possibleKickAndNotIsEnemyKickMe.clear();

        for (Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            if (entry.getValue().getColor().isBlack()) {
                PawnMoves moves = new PawnMoves(entry.getKey(), entry.getValue());
                if (moves.getPossibleMoves().size() > 0) {
                    possibleMoves.add(entry.getKey());
                }

                if (moves.getPossibleKick().size() > 0) {
                    possibleKick.add(entry.getKey());
                }
            }
        }
    }

    public Coordinates choosePawn() {
        int minNumber = -1000;

        Set<Coordinates> cachePawn = new HashSet<>();
        cachePawn.addAll(possibleKick);
        cachePawn.addAll(possibleMoves);

        Set<Coordinates> cachePossiblePawn = new HashSet<>();

        for(Coordinates coordinates : cachePawn) {
            PawnMoves moves = new PawnMoves(coordinates, Board.getPawn(coordinates));

            Set<Coordinates> cacheMoves = new HashSet<>();
            cacheMoves.addAll(moves.getPossibleKick().stream().filter(entry -> Board.getPawn(entry) != null).collect(Collectors.toSet()));
            cacheMoves.addAll(moves.getPossibleMoves());

            int point = getMinNumber(cacheMoves, Board.getPawn(coordinates));

            if(point > minNumber) {
                minNumber = point;
            }
        }

        for(Coordinates coordinates : cachePawn) {
            PawnMoves moves = new PawnMoves(coordinates, Board.getPawn(coordinates));

            Set<Coordinates> cacheMoves = new HashSet<>();
            cacheMoves.addAll(moves.getPossibleKick().stream().filter(entry -> Board.getPawn(entry) != null).collect(Collectors.toSet()));
            cacheMoves.addAll(moves.getPossibleMoves());

            for(Coordinates moveCoordinates : cacheMoves) {
                PawnClass oldPawn = Board.addPawnWithoutDesign(moveCoordinates, Board.getPawn(coordinates));

                int point = boardPoint.calculateBoard();

                if (point == minNumber) {
                    cachePossiblePawn.add(coordinates);
                }

                Board.removePawnWithoutDesign(moveCoordinates);

                if (oldPawn != null) {
                    Board.addPawnWithoutDesign(moveCoordinates, oldPawn);
                }
            }
        }

        return selectRandom(cachePossiblePawn);
    }

    public Coordinates chooseMove(Coordinates coordinates) {
        PawnClass pawn = Board.getPawn(coordinates);
        PawnMoves moves = new PawnMoves(coordinates, pawn);

        Set<Coordinates> possibleMove = new HashSet<>();
        possibleMove.addAll(moves.getPossibleMoves());
        possibleMove.addAll(moves.getPossibleKick());

        Set<Coordinates> listWithOnlyMinNumber = getListWithOnlyMinNumber(possibleMove, pawn);

        listWithOnlyMinNumber.stream()
                .filter(entry -> Board.getPawn(entry)!= null)
                .forEach(entry -> checkEnemyKickField(entry, pawn));

        if(possibleKickAndNotIsEnemyKickMe.size() > 0) {
            return selectRandom(possibleKickAndNotIsEnemyKickMe);
        } else {
            return selectRandom(listWithOnlyMinNumber);
        }
    }

    private int getMinNumber(Set<Coordinates> list, PawnClass actualPawn) {
        int minNumber = -10000;

        for(Coordinates coordinates : list) {
            PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

            int point = boardPoint.calculateBoard();

            if(point > minNumber) {
                minNumber = point;
            }

            Board.removePawnWithoutDesign(coordinates);

            if (oldPawn != null) {
                Board.addPawnWithoutDesign(coordinates, oldPawn);
            }
        }

        return minNumber;
    }

    private Set<Coordinates> getListWithOnlyMinNumber(Set<Coordinates> list, PawnClass actualPawn) {
        int minNumber = getMinNumber(list, actualPawn);
        Set<Coordinates> returnList = new HashSet<>();

        for(Coordinates coordinates : list) {
            PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

            int point = boardPoint.calculateBoard();

            if(point == minNumber) {
                returnList.add(coordinates);
            }

            Board.removePawnWithoutDesign(coordinates);

            if (oldPawn != null) {
                Board.addPawnWithoutDesign(coordinates, oldPawn);
            }
        }

        return returnList;
    }

    private void checkEnemyKickField(Coordinates coordinates, PawnClass actualPawn) {
        PawnClass oldPawn = Board.addPawnWithoutDesign(coordinates, actualPawn);

        Set<Coordinates> possibleEnemyKick = new HashSet<>();

        for (Map.Entry<Coordinates, PawnClass> entry : Board.getBoard().entrySet()) {
            if (!Board.isThisSameColor(entry.getKey(), actualPawn.getColor())) {
                PawnMoves moves = new PawnMoves(entry.getKey(), entry.getValue());
                possibleEnemyKick.addAll(moves.getPossibleKick());
            }
        }

        Board.removePawnWithoutDesign(coordinates);

        if(oldPawn != null) {
            Board.addPawnWithoutDesign(coordinates, oldPawn);
        }

        if(!possibleEnemyKick.contains(coordinates)) {
            possibleKickAndNotIsEnemyKickMe.add(coordinates);
        }
    }


    private Coordinates selectRandom(Set<Coordinates> list) {
        Object[] object = list.toArray();
        return (Coordinates) object[random.nextInt(object.length)];
    }
}
