package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;

import model.ReadOnlyReversiModel;

/**
 * Represents a view that decorates the original HexReversiCanvas with the ability
 * to display hints. Turn this feature on or off using '3'.
 */
public class HintDecoratedCanvas extends HexReversiCanvas implements KeyListener, MouseListener {
  private boolean hintsHuh;

  /**
   * Constructor for a HintDecoratedCanvas that takes in a non-mutable model and
   * the size of the canvas.
   * @param model the ReadOnlyModel to get tiles from.
   * @param size an integer representing the size of the canvas.
   */
  public HintDecoratedCanvas(ReadOnlyReversiModel model, int size) {
    super(model, size);
    hintsHuh = false;
    addKeyListener(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D ourG = (Graphics2D)g.create();
    ourG.setTransform(transformLogicalToPhysical());
    if (this.hintsHuh && this.selected.getX() != model.getRadius()) {
      int pixelX = 0;
      int pixelY = 0;
      for (ITile tile : tiles) {
        if (tile.equalsPoint(selected)) {
          pixelX = tile.getPixelX();
          pixelY = tile.getPixelY();
        }
      }
      int countFlips = model.countFlipsForMove((int) selected.getX(),
              (int) selected.getY(), (int) (-selected.getX() - selected.getY()));
      if (countFlips != 0) {
        countFlips--;
      }
      ourG.setColor(Color.black);
      ourG.setStroke(new BasicStroke(20));

      ourG.drawString(Integer.toString(countFlips), pixelX + 17, pixelY - 10);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    super.keyTyped(e);
    if (e.getKeyChar() == '3') {
      System.out.println("3 pressed");
      hintsHuh = !hintsHuh;
    }
    repaint();
  }


  @Override
  public void keyPressed(KeyEvent e) {
    // keyTyped handles all neccessary functionality.
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // keyTyped handles all neccessary functionality.
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);
    this.repaint();
  }
}
