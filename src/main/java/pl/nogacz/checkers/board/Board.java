package pl.nogacz.checkers.board;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Board {
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    public Board() {
        addStartPawn();
    }

    private void addStartPawn() {
        for(int i = 0; i < 8; i++) {
            board.put(new Coordinates(i, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
            board.put(new Coordinates(i, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
            board.put(new Coordinates(i, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
            board.put(new Coordinates(i, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        }

        for(Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            Design.addPawn(entry.getKey(), entry.getValue());
        }
    }

    public void readMouseEvent(MouseEvent event) {

    }

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            //TODO Add end game
        }
    }
}
