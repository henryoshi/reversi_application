package view;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Objects;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JLabel;

import model.ReadOnlyReversiModel;

/**
 * A canvas to handle view and user input of a now Square reversi game.
 */
public class SquareReversiCanvas extends JPanel implements MouseListener, KeyListener {
  private ArrayList<SquareTileView> tiles;
  private final ArrayList<TileClickedListener> listeners;
  private final ReadOnlyReversiModel model;
  private final int size;
  private Point selected;
  private JLabel label;

  /**
   * Constructor for a ReversiCanvas that creates
   * a new Canvas to display the model at the given parameters.
   *
   * @param model the model to display
   * @param size  the size of the canvas
   */
  SquareReversiCanvas(ReadOnlyReversiModel model, int size) {
    super();
    setPreferredSize(new Dimension(size, size));
    setBackground(Color.PINK);
    addMouseListener(this);
    this.addKeyListener(this);
    this.listeners = new ArrayList<>();
    this.tiles = new ArrayList<>();
    this.model = Objects.requireNonNull(model);
    this.size = size;
    makeTiles();
    this.selected = new Point(-1, -1);
    this.label = new JLabel("");
    this.label.setFont(new Font("Serif", Font.BOLD, 20));
    add(label);
  }

  private void makeTiles() {
    int size = this.size / (model.getRadius() * 4);
    for (int x = 0; x < model.getRadius() * 2; x++) {
      for (int y = 0; y < model.getRadius() * 2; y++) {
        int pixelX = (int) (size * x);
        int pixelY = (int) (size * y);
        tiles.add(new SquareTileView(pixelX, pixelY, x, y, size, model.getTileAt(x, y, 0).getFT()));
      }
    }
  }

  protected void addTileClickedListener(TileClickedListener e) {
    this.listeners.add(Objects.requireNonNull(e));
  }

  protected void setLabel(String message) {
    if (!label.getText().contains("<html><center>")) {
      this.label.setText(message);
    }
    repaint();
  }

  protected void updateBoard() {
    this.tiles = new ArrayList<>();
    makeTiles();
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (SquareTileView t : this.tiles) {
      // draw tile either normal, highlighted without hints, or highlighted with hints
      t.drawTile(g, transformLogicalToPhysical(), t.equalsPoint(selected));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Point clickedPoint = e.getPoint();
    Point realClicked = transformPhysicalToLogical(clickedPoint);
    boolean clickedHuh = false;
    for (SquareTileView tile : this.tiles) {
      if (tile.contains(realClicked)) {
        clickedHuh = true;
        if (tile.equalsPoint(selected)) {
          this.selected = new Point(-1, -1);
        } else {
          this.selected = new Point(tile.getX(), tile.getY());
          emitTileClickedEvent(tile.getX(), tile.getY());
        }
        repaint();
      }
    }
    if (!clickedHuh) {
      this.selected = new Point(-1, -1);
      repaint();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // From mouse listener
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // From mouse listener
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // From mouse listener
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // From mouse listener
  }

  private void emitTileClickedEvent(int x, int y) {
    for (TileClickedListener listener : listeners) {
      listener.clickedAt(x, y);
    }
  }

  private void emitPlaceTileEvent(int x, int y) {
    for (TileClickedListener listener : listeners) {
      listener.placeTileAttempt(x, y);
    }
  }

  private void emitPassTileEvent() {
    for (TileClickedListener listener : listeners) {
      listener.passAttempt();
    }
  }

  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    ret.scale(1.25, 1.25);
    ret.translate(this.getWidth() / 3.8, this.getHeight() / 3.8);

    return ret;
  }

  private Point transformPhysicalToLogical(Point p) {
    Point p2 = new Point((int) p.getX(), (int) p.getY());
    p2.translate((int) (-this.getWidth() / 3.8), (int) (-this.getHeight() / 3.8));

    return p2;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (e.getKeyChar() == '1') {
      if (selected.getX() != -1) {
        emitPlaceTileEvent((int) this.selected.getX(), (int) this.selected.getY());
      }
    } else if (e.getKeyChar() == '2') {
      emitPassTileEvent();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // From key listener
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // From key listener
  }
}
