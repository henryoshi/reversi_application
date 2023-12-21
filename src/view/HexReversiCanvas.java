package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Objects;

import model.ReadOnlyReversiModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;


import static java.lang.Math.sqrt;

/**
 * A canvas for a Hexagonal game of Reversi, implements all required
 * functionality of IReversiCanvas.
 */
public class HexReversiCanvas extends JPanel implements MouseListener, KeyListener {
  protected ArrayList<HexTileView> tiles;
  private final ArrayList<TileClickedListener> listeners;
  protected final ReadOnlyReversiModel model;
  private final int size;
  protected Point selected;
  private JLabel label;

  /**
   * Constructor for a ReversiCanvas that creates
   * a new Canvas to display the model at the given parameters.
   *
   * @param model the model to display
   * @param size  the size of the canvas
   */
  HexReversiCanvas(ReadOnlyReversiModel model, int size) {
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
    this.selected = new Point(model.getRadius(), model.getRadius());
    this.label = new JLabel("");
    this.label.setFont(new Font("Serif", Font.BOLD, 20));
    add(label);
  }

  private void makeTiles() {
    int size = this.size / (model.getRadius() * 4);
    for (int x = -model.getRadius() + 1; x < model.getRadius(); x++) {
      for (int y = -model.getRadius() + 1; y < model.getRadius(); y++) {
        int pixelX = (int) (size * (sqrt(3) * x + sqrt(3) / 2 * y));
        int pixelY = (int) (size * (3.0 / 2 * y));
        if (Math.abs(-x - y) < model.getRadius()) {
          tiles.add(new HexTileView(pixelX, pixelY, x, y, size,
                  model.getTileAt(x, y, -x - y).getFT()));
        }
      }
    }
  }

  protected void addTileClickedListener(TileClickedListener e) {
    this.listeners.add(Objects.requireNonNull(e));
  }

  protected void setLabel(String message) {
    this.label.setText(message);
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
    for (HexTileView t : this.tiles) {
      t.drawTile(g, transformLogicalToPhysical(), t.equalsPoint(selected));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Point clickedPoint = e.getPoint();
    Point realClicked = transformPhysicalToLogical(clickedPoint);
    boolean clickedHuh = false;
    for (HexTileView tile : this.tiles) {
      if (tile.contains(realClicked)) {
        clickedHuh = true;
        if (tile.equalsPoint(selected)) {
          this.selected = new Point(model.getRadius(), model.getRadius());
        } else {
          this.selected = new Point(tile.getX(), tile.getY());
          emitTileClickedEvent(tile.getX(), tile.getY());
        }
        repaint();
      }
    }
    if (!clickedHuh) {
      this.selected = new Point(model.getRadius(), model.getRadius());
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

  private void emitTileClickedEvent(int q, int r) {
    for (TileClickedListener listener : listeners) {
      listener.clickedAt(q, r);
    }
  }

  private void emitPlaceTileEvent(int q, int r) {
    for (TileClickedListener listener : listeners) {
      listener.placeTileAttempt(q, r);
    }
  }

  private void emitPassTileEvent() {
    for (TileClickedListener listener : listeners) {
      listener.passAttempt();
    }
  }

  protected AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    ret.scale(1.25, 1.25);
    ret.translate(this.getWidth() / 2.0, this.getHeight() / 2.0);

    return ret;
  }

  protected Point transformPhysicalToLogical(Point p) {
    Point p2 = new Point((int) p.getX(), (int) p.getY());
    p2.translate((int) (-this.getWidth() / 2), (int) (-this.getHeight() / 2));

    return p2;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (e.getKeyChar() == '1') {
      if (selected.getX() != model.getRadius()) {
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
