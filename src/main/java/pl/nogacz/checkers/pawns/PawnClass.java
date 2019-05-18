package pl.nogacz.checkers.pawns;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.nogacz.checkers.application.Resources;

import java.io.Serializable;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class PawnClass implements Serializable {
    private Pawn pawn;
    private PawnColor color;

    public PawnClass(Pawn pawn, PawnColor color) {
        this.pawn = pawn;
        this.color = color;
    }

    public PawnColor getColor() {
        return color;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public ImageView getImage() {
        Image image = new Image(Resources.getPath("pawns/" + color + "_" + pawn + ".png"));
        return new ImageView(image);
    }

    public ImageView getLightImage() {
        Image image = new Image(Resources.getPath("pawns/light_" + color + "_" + pawn + ".png"));
        return new ImageView(image);
    }
}
