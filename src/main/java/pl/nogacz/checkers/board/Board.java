package pl.nogacz.checkers.board;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Board {
    public void readMouseEvent(MouseEvent event) {

    }

    public void readKeyboard(KeyEvent event) {
        if(event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            //TODO Add end game
        }
    }
}
