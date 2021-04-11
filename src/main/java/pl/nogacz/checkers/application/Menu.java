package pl.nogacz.checkers.application;

import pl.nogacz.checkers.board.Board;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class Menu extends JFrame implements ActionListener {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private Board startBoard;
    private JButton startGameButton = new JButton(Board.Commands.START.name());
    private JButton quitButton = new JButton(Board.Commands.EXIT.name());
    private boolean menuOpen;

    public JButton getStartGameButton() {
        return startGameButton;
    }

    public void setStartGameButton(JButton startGameButton) {
        this.startGameButton = startGameButton;
    }

    public Board getStartBoard() {
        return startBoard;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void setStartBoard(Board startBoard) {
        this.startBoard = startBoard;
    }

    public JButton getquitButton() {
        return quitButton;
    }

    public void setQuitButton(JButton quitButton) {
        this.quitButton = quitButton;
    }

    public Menu(Board board) {
        super("Menu");
        startBoard = board;
        startBoard.setMenuActive(true);
        menuOpen = true;
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setUndecorated(true);
        this.setBackground(new Color(102, 51, 0, 200));
        this.add(startGameButton);
        this.add(quitButton);
        startGameButton.setIcon(board.getImageIconForFileName("start-game.png"));
        quitButton.setIcon(board.getImageIconForFileName("quit-game.png"));
        startGameButton.setBounds(150, 125, 190, 50);
        quitButton.setBounds(150, 300, 180, 50);
        startGameButton.addActionListener(this);
        quitButton.addActionListener(this);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();
        if (command.equalsIgnoreCase(Board.Commands.START.name())) {
            startBoard.setMenuActive(false);
            menuOpen = false;
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (command.equalsIgnoreCase(Board.Commands.EXIT.name())) {
            System.exit(7777);
        }
    }
}