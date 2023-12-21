package model;

/**
 * An implementation of ITile that represents a hexagonal tile
 * in a Reversi game. The HexTile works on a Cubic coordinate system
 * and thus has q, r, and s to determine its position. There is an
 * invariant that q + r + s must equal 0 at all times.
 */
public class HexTile extends ATile {
  int q;
  int r;
  int s;

  /**
   * Constructor to create a Hexagonal Tile with the given
   * cubic coordinates. By default, sets the fill to Empty.
   *
   * @param q the q coordinate of a cubic coordinate system.
   * @param r the r coordinate of a cubic coordinate system.
   * @param s the s coordinate of a cubic coordinate system.
   */
  public HexTile(int q, int r, int s) {
    checkValidQRS(q, r, s);
    this.q = q;
    this.r = r;
    this.s = s;
    this.ft = FillType.EMPTY;
  }

  /**
   * Private constructor used when creating deep copy of HexTile.
   *
   * @param q the q coordinate of a cubic coordinate system.
   * @param r the r coordinate of a cubic coordinate system.
   * @param s the s coordinate of a cubic coordinate system.
   */
  private HexTile(int q, int r, int s, FillType ft) {
    checkValidQRS(q, r, s);
    this.q = q;
    this.r = r;
    this.s = s;
    this.ft = ft;
  }

  @Override
  public boolean hasCoords(int q, int r, int s) throws IllegalArgumentException {
    checkValidQRS(q, r, s);
    return (this.q == q && this.r == r && this.s == s);
  }

  @Override
  public Tile getClone() {
    return new HexTile(this.getQorX(), this.getRorY(), this.getS(), this.ft);
  }

  @Override
  public int getQorX() {
    return this.q;
  }

  @Override
  public int getRorY() {
    return this.r;
  }

  @Override
  public int getS() {
    return this.s;
  }

  /**
   * Validates QRS input by throwing error if invalid.
   *
   * @param q the q coordinate of the tile in a cube coordinate system
   * @param r the r coordinate of the tile in a cube coordinate system
   * @param s the s coordinate of the tile in a cube coordinate system
   * @throws IllegalArgumentException if q + r + s != 0, or if any of the inputs are out of bounds.
   */
  private void checkValidQRS(int q, int r, int s) throws IllegalArgumentException {
    if (q + r + s != 0) {
      throw new IllegalArgumentException("Invalid q, r, and s coordinates passed.");
    }
  }
}
