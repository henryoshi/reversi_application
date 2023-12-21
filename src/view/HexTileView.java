package view;

import java.awt.Polygon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import model.FillType;

import static java.lang.Math.sqrt;

/**
 * A class representing a single tile on the board of a ReversiGame.
 */
public class HexTileView implements ITile {
  protected final int x;
  protected final int y;
  private final int logicalX;
  private final int logicalY;
  private final int size;
  private final Polygon shape;
  private final FillType ft;

  /**
   * A simple tile constructor that creates Hexagon about the passed
   * x and y.
   *
   * @param x        Pixel coordinates of the center of this tile
   * @param y        Pixel coordinates of the center of this tile
   * @param logicalX Logical coordinates of this tile
   * @param logicalY Logical coordinates of this tile
   * @param size     The size that all tiles are based off of
   * @param ft       The type of tile placed on this tile (white, black, none)
   */
  HexTileView(int x, int y, int logicalX, int logicalY, int size, FillType ft) {
    this.x = x;
    this.y = y;
    this.logicalX = logicalX;
    this.logicalY = logicalY;
    this.size = size;
    this.shape = makePolygon();
    this.ft = Objects.requireNonNull(ft);
  }

  private Polygon makePolygon() {
    double w = (sqrt(3) * this.size);
    double h = 2 * this.size;

    int[] xCoords = {(int) x, (int) (x + .5 * w), (int) (x + .5 * w), (int) (x),
        (int) (x - .5 * w), (int) (x - .5 * w)};
    int[] yCoords = {(int) (y - .5 * h), (int) (y - .25 * h), (int) (y + .25 * h),
        (int) (y + .5 * h), (int) (y + .25 * h), (int) (y - .25 * h)};

    return new Polygon(xCoords, yCoords, 6);
  }

  private void drawPerson(Graphics ourG) {
    int circlescale = (int) (this.size * .7);

    if (this.ft.equals(FillType.BLACK)) {
      ourG.setColor(Color.BLACK);
      ourG.fillOval(this.x - circlescale / 2, this.y - circlescale / 2, circlescale, circlescale);
      ((Graphics2D) ourG).setStroke(new BasicStroke((float) (this.size * .065)));
      ourG.setColor(Color.WHITE);
      ourG.drawOval(this.x - circlescale / 2, this.y - circlescale / 2, circlescale, circlescale);
    } else if (this.ft.equals(FillType.WHITE)) {
      ourG.setColor(Color.WHITE);
      ourG.fillOval(this.x - circlescale / 2, this.y - circlescale / 2, circlescale, circlescale);
      ((Graphics2D) ourG).setStroke(new BasicStroke((float) (this.size * .065)));
      ourG.setColor(Color.BLACK);
      ourG.drawOval(this.x - circlescale / 2, this.y - circlescale / 2, circlescale, circlescale);
    }
  }

  @Override
  public void drawTile(Graphics g, AffineTransform at, boolean highlighted) {
    Graphics2D ourG = (Graphics2D) g.create();
    ourG.setTransform(at);
    if (highlighted) {
      ourG.setColor(new Color(93, 157, 220));
    }
    else {
      ourG.setColor(Color.GRAY);
    }
    ourG.fill(shape);
    ourG.setColor(Color.BLACK);
    ourG.setStroke(new BasicStroke((float) (this.size * 0.1)));
    ourG.drawPolygon(shape);
    drawPerson(ourG);
  }

  @Override
  public boolean contains(Point p) {
    return shape.contains(p);
  }

  @Override
  public boolean equalsPoint(Point p) {
    return (p.getX() == this.logicalX && p.getY() == this.logicalY);
  }

  @Override
  public int getX() {
    return this.logicalX;
  }

  @Override
  public int getY() {
    return this.logicalY;
  }

  @Override
  public int getPixelX() {
    return this.x;
  }

  @Override
  public int getPixelY() {
    return this.y;
  }


}
