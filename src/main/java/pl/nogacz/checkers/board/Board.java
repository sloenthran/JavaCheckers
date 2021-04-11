package pl.nogacz.checkers.board;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import pl.nogacz.checkers.application.Design;
import pl.nogacz.checkers.application.Computer;
import pl.nogacz.checkers.application.EndGame;
import pl.nogacz.checkers.application.RestartGame;
import pl.nogacz.checkers.pawns.Pawn;
import pl.nogacz.checkers.pawns.PawnClass;
import pl.nogacz.checkers.pawns.PawnColor;
import pl.nogacz.checkers.pawns.PawnMoves;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dawid Nogacz on 17.05.2019
 */
public class Board {
    private static HashMap<Coordinates, PawnClass> board = new HashMap<>();

    private boolean isSelected = false;
    private boolean newKick = false;
    private Coordinates selectedCoordinates;

    private Set<Coordinates> possibleMoves = new HashSet<>();
    private Set<Coordinates> possibleKick = new HashSet<>();
    private Set<Coordinates> possiblePromote = new HashSet<>();

    private boolean isGameEnd = false;
    private int roundWithoutKick = 0;
    private boolean isMenuActive = false;
    private boolean isComputerRound = false;
    private Computer computer = new Computer();

    public Board() {
        addStartPawn();
    }

    public static HashMap<Coordinates, PawnClass> getBoard() {
        return board;
    }

    private void addStartPawn() {
        board.put(new Coordinates(1, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(3, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(5, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(7, 0), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(0, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(2, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(4, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(6, 1), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(1, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(3, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(5, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));
        board.put(new Coordinates(7, 2), new PawnClass(Pawn.PAWN, PawnColor.BLACK));

        board.put(new Coordinates(0, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6, 5), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(1, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(3, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(5, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(7, 6), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(0, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(2, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(4, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));
        board.put(new Coordinates(6, 7), new PawnClass(Pawn.PAWN, PawnColor.WHITE));

        for (Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            Design.addPawn(entry.getKey(), entry.getValue());
        }
    }

    public void readMouseEvent(MouseEvent event) {
        if (isMenuActive) {
            return;
        }
        if (isComputerRound) {
            return;
        }

        checkGameEnd();

        if (isGameEnd) {
            return;
        }

        Coordinates eventCoordinates = new Coordinates((int) ((event.getX() - 37) / 85),
                (int) ((event.getY() - 37) / 85));

        if (isSelected) {
            if (selectedCoordinates.equals(eventCoordinates) && !newKick) {
                unLightSelect(selectedCoordinates);

                selectedCoordinates = null;
                isSelected = false;
            } else if (possibleMoves.contains(eventCoordinates)) {
                roundWithoutKick++;

                unLightSelect(selectedCoordinates);
                movePawn(selectedCoordinates, eventCoordinates);
                selectedCoordinates = null;
                isSelected = false;

                computerMove();
            } else if (possibleKick.contains(eventCoordinates) && !isFieldNotNull(eventCoordinates)) {
                roundWithoutKick = 0;

                unLightSelect(selectedCoordinates);

                if (!kickPawn(selectedCoordinates, eventCoordinates)) {
                    isSelected = false;
                    newKick = false;
                    computerMove();
                } else {
                    newKick = true;
                    selectedCoordinates = eventCoordinates;
                }
            }
        } else if (eventCoordinates.isValid()) {
            if (isFieldNotNull(eventCoordinates)) {
                if (getPawn(eventCoordinates).getColor().isWhite()
                        && isPossiblePawn(eventCoordinates, PawnColor.WHITE)) {
                    isSelected = true;
                    selectedCoordinates = eventCoordinates;
                    lightSelect(eventCoordinates);
                }
            }
        }
    }

    public void readKeyboard(KeyEvent event) {
        if (event.getCode().equals(KeyCode.R) || event.getCode().equals(KeyCode.N)) {
            RestartGame.restartGame();
        }
        if (event.getCode().equals(KeyCode.ESCAPE) && !isMenuActive) {
            isMenuActive = true;
            Menu menu = new Menu();
        }
    }

    private void computerMove() {
        checkGameEnd();
        if (isMenuActive) {
            return;
        }
        if (isGameEnd) {
            return;
        }

        Task<Void> computerSleep = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                return null;
            }
        };

        computerSleep.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Coordinates moveCoordinates = computer.chooseMove(selectedCoordinates);
                unLightSelect(selectedCoordinates);

                if (computer.isKickedMove()) {
                    if (!kickPawn(selectedCoordinates, moveCoordinates)) {
                        newKick = false;
                        isComputerRound = false;
                        selectedCoordinates = null;
                    } else {
                        newKick = true;
                        selectedCoordinates = moveCoordinates;
                        computerMove();
                    }
                } else {
                    movePawn(selectedCoordinates, moveCoordinates);

                    isComputerRound = false;
                    selectedCoordinates = null;
                }

            }
        });

        isComputerRound = true;
        computer.getGameData();

        if (!newKick) {
            selectedCoordinates = computer.choosePawn();
        }

        lightSelect(selectedCoordinates);

        new Thread(computerSleep).start();
    }

    private boolean isPossiblePawn(Coordinates coordinates, PawnColor color) {
        Set<Coordinates> possiblePawn = new HashSet<>();

        for (Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            if (entry.getValue().getColor() == color) {
                PawnMoves pawnMoves = new PawnMoves(entry.getKey(), entry.getValue());

                if (pawnMoves.getPossibleKick().size() > 0) {
                    possiblePawn.add(entry.getKey());
                }
            }
        }

        if (possiblePawn.size() == 0 || possiblePawn.contains(coordinates)) {
            return true;
        }

        return false;
    }

    private void movePawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if (possiblePromote.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        Design.removePawn(oldCoordinates);
        Design.removePawn(newCoordinates);
        Design.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.put(newCoordinates, pawn);
    }

    private boolean kickPawn(Coordinates oldCoordinates, Coordinates newCoordinates) {
        PawnClass pawn = getPawn(oldCoordinates);

        if (possiblePromote.contains(newCoordinates)) {
            pawn = new PawnClass(Pawn.QUEEN, pawn.getColor());
        }

        Coordinates enemyCoordinates = getEnemyCoordinates(newCoordinates);

        Design.removePawn(oldCoordinates);
        Design.removePawn(enemyCoordinates);
        Design.addPawn(newCoordinates, pawn);

        board.remove(oldCoordinates);
        board.remove(enemyCoordinates);
        board.put(newCoordinates, pawn);

        PawnMoves pawnMoves = new PawnMoves(newCoordinates, pawn);

        if (pawnMoves.getPossibleKick().size() > 0) {
            lightNewKick(newCoordinates);
            return true;
        }

        return false;
    }

    private Coordinates getEnemyCoordinates(Coordinates coordinates) {
        Coordinates checkUpLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() - 1);

        if (possibleKick.contains(checkUpLeft)) {
            return checkUpLeft;
        }

        Coordinates checkUpRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() - 1);

        if (possibleKick.contains(checkUpRight)) {
            return checkUpRight;
        }

        Coordinates checkBottomLeft = new Coordinates(coordinates.getX() - 1, coordinates.getY() + 1);

        if (possibleKick.contains(checkBottomLeft)) {
            return checkBottomLeft;
        }

        Coordinates checkBottomRight = new Coordinates(coordinates.getX() + 1, coordinates.getY() + 1);

        if (possibleKick.contains(checkBottomRight)) {
            return checkBottomRight;
        }

        return null;
    }

    private void lightSelect(Coordinates coordinates) {
        PawnMoves pawnMoves = new PawnMoves(coordinates, getPawn(coordinates));

        possibleMoves = pawnMoves.getPossibleMoves();
        possibleKick = pawnMoves.getPossibleKick();
        possiblePromote = pawnMoves.getPossiblePromote();

        if (possibleKick.size() > 0) {
            possibleMoves.clear();
        }

        possibleMoves.forEach(this::lightMove);
        possibleKick.forEach(this::lightKick);

        lightPawn(coordinates);
    }

    private void lightNewKick(Coordinates coordinates) {
        PawnMoves pawnMoves = new PawnMoves(coordinates, getPawn(coordinates));

        possibleMoves.clear();
        possibleKick = pawnMoves.getPossibleKick();
        possiblePromote = pawnMoves.getPossiblePromote();

        possibleKick.forEach(this::lightKick);

        lightPawn(coordinates);
    }

    private void lightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addLightPawn(coordinates, pawn);
    }

    private void lightMove(Coordinates coordinates) {
        Design.addLightMove(coordinates);
    }

    private void lightKick(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if (pawn == null) {
            lightMove(coordinates);
        }
    }

    private void unLightSelect(Coordinates coordinates) {
        possibleMoves.forEach(this::unLightMove);
        possibleKick.forEach(this::unLightKick);

        unLightPawn(coordinates);
    }

    private void unLightPawn(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);
        Design.removePawn(coordinates);
        Design.addPawn(coordinates, pawn);
    }

    private void unLightMove(Coordinates coordinates) {
        Design.removePawn(coordinates);
    }

    private void unLightKick(Coordinates coordinates) {
        PawnClass pawn = getPawn(coordinates);

        if (pawn != null) {
            unLightPawn(coordinates);
        } else {
            unLightMove(coordinates);
        }
    }

    public void checkGameEnd() {
        Set<Coordinates> possibleMovesWhite = new HashSet<>();
        Set<Coordinates> possibleMovesBlack = new HashSet<>();
        int pawnWhiteCount = 0;
        int pawnBlackCount = 0;

        for (Map.Entry<Coordinates, PawnClass> entry : board.entrySet()) {
            PawnMoves moves = new PawnMoves(entry.getKey(), entry.getValue());

            if (entry.getValue().getColor().isBlack()) {
                pawnBlackCount++;
                possibleMovesBlack.addAll(moves.getPossibleKick());
                possibleMovesBlack.addAll(moves.getPossibleMoves());
            } else {
                pawnWhiteCount++;
                possibleMovesWhite.addAll(moves.getPossibleKick());
                possibleMovesWhite.addAll(moves.getPossibleMoves());
            }
        }

        if (roundWithoutKick == 12) {
            isGameEnd = true;
            new EndGame("Draw. Maybe you try again?");
        } else if (possibleMovesWhite.size() == 0 || pawnWhiteCount <= 1) {
            isGameEnd = true;
            new EndGame("You loss. Maybe you try again?");
        } else if (possibleMovesBlack.size() == 0 || pawnBlackCount <= 1) {
            isGameEnd = true;
            new EndGame("You win! Congratulations! :)");
        }
    }

    public static boolean isFieldNotNull(Coordinates coordinates) {
        return getPawn(coordinates) != null;
    }

    public static boolean isThisSameColor(Coordinates coordinates, PawnColor color) {
        return getPawn(coordinates).getColor() == color;
    }

    public static void setBoard(HashMap<Coordinates, PawnClass> board) {
        Board.board = board;
    }

    public static PawnClass getPawn(Coordinates coordinates) {
        return board.get(coordinates);
    }

    public void setMenuActive(boolean menuActive) {
        isMenuActive = menuActive;
    }

    public boolean isMenuActive() {
        return isMenuActive;
    }

    public ImageIcon getImageIconForFileName(String name) {
        String fileString = this.getClass().getClassLoader().getResource(name).getFile();
        return new ImageIcon(fileString);
    }

    public class Menu extends JFrame implements ActionListener {
        public static final int WIDTH = 500;
        public static final int HEIGHT = 500;
        private JButton resumeButton = new JButton(Commands.RESUME.name());
        private JButton newGameButton = new JButton(Commands.NEW_GAME.name());
        private JButton quitButton = new JButton(Commands.EXIT.name());

        public Menu() {
            super("Menu");
            setSize(WIDTH, HEIGHT);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setUndecorated(true);
            this.setBackground(new Color(102, 51, 0, 200));
            this.add(resumeButton);
            this.add(newGameButton);
            this.add(quitButton);
            quitButton.setIcon(getImageIconForFileName("quit-game.png"));
            resumeButton.setIcon(getImageIconForFileName("resume.png"));
            newGameButton.setIcon(getImageIconForFileName("new-game.png"));
            resumeButton.setBounds(150, 50, 180, 50);
            newGameButton.setBounds(150, 200, 180, 50);
            quitButton.setBounds(150, 350, 180, 50);
            resumeButton.addActionListener(this);
            newGameButton.addActionListener(this);
            quitButton.addActionListener(this);
            setLocationRelativeTo(null);
            setLayout(null);
            setVisible(true);
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    isMenuActive = false;
                }
            });
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String command = actionEvent.getActionCommand();
            if (command.equalsIgnoreCase(Commands.RESUME.name())) {
                isMenuActive = false;
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            } else if (command.equalsIgnoreCase(Commands.NEW_GAME.name())) {
                RestartGame.restartGame();
            } else if (command.equalsIgnoreCase(Commands.EXIT.name())) {
                System.exit(0);
            }
        }
    }

    public enum Commands {
        // Add your Command names here
        // For use them as string call Commands.EXIT.name() or for another class
        // Board.Commands.Resume.toString()
        RESUME, EXIT, NEW_GAME, START;
    }
}