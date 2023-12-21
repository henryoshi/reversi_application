package view;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * An interface including the neccessary methods of any ITile.
 */
public interface ITile {
  /**
   * Draws the current tile on the passed Graphics object.
   *
   * @param g  the Graphics object to display on
   * @param at an AffineTransform to manipulate the display
   * @param highlighted whether to draw the tile as highlighted or not
   */
  void drawTile(Graphics g, AffineTransform at, boolean highlighted);

  /**
   * Returns whether the passed PHYSICAL x y point is contained within the shape
   * of this ITile.
   *
   * @param p the point in question
   * @return boolean representing whether the point is in this shape
   */
  boolean contains(Point p);

  /**
   * Returns whether the passed LOGICAL x y point is the same as
   * this ITile's logical coordinates.
   *
   * @param p the point in question
   * @return boolean representing whether the point is the same as the
   *         logical point of this ITile
   */
  boolean equalsPoint(Point p);

  /**
   * Returns the logical X coordinate of this ITile.
   *
   * @return an integer representing the logical X of this ITile
   */
  int getX();

  /**
   * Returns the logical Y coordinate of this ITile.
   *
   * @return an integer representing the logical Y of this ITile
   */
  int getY();

  int getPixelX();

  int getPixelY();
}
