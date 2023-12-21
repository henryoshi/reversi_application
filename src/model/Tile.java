package model;

/**
 * public-facing interface for a tile of a board.
 */
public interface Tile {
  /**
   * Flips this Tile's FillType from White to Black,
   * Black to White, or if Empty, remains Empty.
   */
  void flipTile();

  /**
   * Sets this Tile's FillType to the passed FillType.
   *
   * @param ft the requested new FillType for this Tile.
   */
  void setFillType(FillType ft);

  /**
   * Returns whether this ITile has the passed cubic coordinates.
   *
   * @param q the q coordinate of a cubic coordinate system
   * @param r the q coordinate of a cubic coordinate system
   * @param s the q coordinate of a cubic coordinate system
   * @return a boolean representing the equality of the given coordinates with
   *         this ITile's coordinates.
   * @throws IllegalArgumentException if q + r + s != 0 as expected
   */
  boolean hasCoords(int q, int r, int s) throws IllegalArgumentException;

  /**
   * Returns this ITile's FillType.
   *
   * @return a FillType of this ITile.
   */
  FillType getFT();

  /**
   * Create and return a new instance of the ITile.
   *
   * @return a deep copy of the ITile.
   */
  Tile getClone();

  /**
   * Returns this ITile's q coordinate.
   *
   * @return an integer q, coordinate.
   */
  int getQorX();

  /**
   * Returns this ITile's q coordinate.
   *
   * @return an integer q, coordinate.
   */
  int getRorY();

  /**
   * Returns this ITile's q coordinate.
   *
   * @return an integer q, coordinate.
   */
  int getS();

  /**
   * Returns a render of the tile.
   *
   * @return string rendering of the tile ('X', 'O', or ' ')
   */
  String toString();
}
