package pl.nogacz.checkers.application.computer;

import pl.nogacz.checkers.board.Board;
import pl.nogacz.checkers.board.Coordinates;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnMoves;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dawid Nogacz on 25.05.2019
 */
public class BoardPoint {
    public int calculateBoard() {
        HashMap<Coordinates, PawnClass> cacheBoard = new HashMap<>(Board.getBoard());

        int whitePoint = 0;
        int blackPoint = 0;

        for(Map.Entry<Coordinates, PawnClass> entry : cacheBoard.entrySet()) {
            PawnMoves pawnMoves = new PawnMoves(entry.getKey(), entry.getValue());

            if(entry.getValue().getColor().isWhite()) {
                if(pawnMoves.getPossibleKick().size() > 0) {
                    whitePoint += 5;
                }

                if(pawnMoves.getPossiblePromote().size() > 0) {
                    whitePoint += 3;
                }

                whitePoint += calculatePawn(entry.getKey(), entry.getValue());
            } else {
                if(pawnMoves.getPossibleKick().size() > 0) {
                    blackPoint += 5;
                }

                if(pawnMoves.getPossiblePromote().size() > 0) {
                    blackPoint += 3;
                }

                blackPoint += calculatePawn(entry.getKey(), entry.getValue());
            }
        }

        return blackPoint - whitePoint;
    }

    private int calculatePawn(Coordinates coordinates, PawnClass pawn) {
        int point = 1;

        if(pawn.getColor().isWhite() && pawn.getPawn() == Pawn.PAWN && coordinates.getY() > 4) {
            point++;
        } else if(pawn.getColor().isBlack() && pawn.getPawn() == Pawn.PAWN && coordinates.getY() < 4) {
            point++;
        }

        if(pawn.getPawn().isQueen()) {
            point += 3;
        }

        return point;
    }
}