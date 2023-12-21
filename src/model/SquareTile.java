package model;

/**
 * Assignment 9 class representing a single square tile. The coordinates
 * of this system are in x and y with x replacing q, and y replacing r
 * when called on a method built for axial coordinates (s = 0).
 */
public class SquareTile extends ATile {
  // Integers representing the middle of this tile.
  int x;
  int y;

  /**
   * Constructor for a SqTile, takes in the middle of this square.
   * @param x the x coordinate of the middle of this square.
   * @param y the y coordinate of the middle of this square.
   */
  public SquareTile(int x, int y) {
    this.x = x;
    this.y = y;
    this.ft = FillType.EMPTY;
  }

  @Override
  public boolean hasCoords(int q, int r, int s) throws IllegalArgumentException {
    return (this.x == q && this.y == r);
  }

  @Override
  public Tile getClone() {
    SquareTile toReturn = new SquareTile(this.x, this.y);
    toReturn.setFillType(this.ft);
    return toReturn;
  }

  @Override
  public int getQorX() {
    return this.x;
  }

  @Override
  public int getRorY() {
    return this.y;
  }
}
