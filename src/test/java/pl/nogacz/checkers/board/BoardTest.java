package pl.nogacz.checkers.board;

import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    HashMap<Coordinates, PawnClass> boardMap = new HashMap<>();
    @BeforeEach
    void setUp(){
        boardMap.put(new Coordinates(1, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(3, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(5, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(7, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(0, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(2, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(4, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(6, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(1, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(3, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(5, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        boardMap.put(new Coordinates(7, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));

        boardMap.put(new Coordinates(0, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(2, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(4, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(6, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(1, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(3, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(5, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(7, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(0, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(2, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(4, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        boardMap.put(new Coordinates(6, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
    }

    @Test void abc(){
        assertTrue(false);
    }
    
}   