package view;

import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JFrame;

import model.ReadOnlyReversiModel;

import javax.swing.WindowConstants;
import javax.swing.JOptionPane;

/**
 * A window to hold the canvas of the game and receive inputs.
 */
public class HexReversiGUI extends JFrame implements TileClickedListener, IReversiGUI {
  protected ReadOnlyReversiModel model;
  protected HintDecoratedCanvas canvas;

  /**
   * Constructor for making a new ReversiWindow, takes in a model to
   * view and run on.
   *
   * @param model the ReadOnlyReversiModel that represents this game
   */
  public HexReversiGUI(ReadOnlyReversiModel model) {
    super();
    this.model = Objects.requireNonNull(model);
    setPreferredSize(new Dimension(750, 750));
    this.canvas = new HintDecoratedCanvas(model, 750);

    canvas.addTileClickedListener(this);
    this.addKeyListener(canvas);

    add(canvas);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
    setResizable(true);

    pack();
  }

  /**
   * Opens a small notification window to alert messages and errors.
   *
   * @param message message to add with the alert
   */
  @Override
  public void notifyPlayer(String message) {
    JOptionPane.showMessageDialog(null,
            message,
            "Invalid Move Error",
            JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void updateBoard() {
    this.canvas.updateBoard();
  }

  @Override
  public void clickedAt(int q, int r) {
    System.out.println("q: " + q + ", r: " + r);
  }

  @Override
  public void placeTileAttempt(int q, int r) {
    System.out.println("Attempted move at axial coords: " + q + " " + r);
  }

  @Override
  public void addTileClickedListener(TileClickedListener e) {
    this.canvas.addTileClickedListener(Objects.requireNonNull(e));
  }

  @Override
  public void setLabel(String message) {
    canvas.setLabel(message);
  }

  @Override
  public void passAttempt() {
    System.out.println("Attempted pass attempt.");
  }
}
