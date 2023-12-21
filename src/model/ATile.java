package model;

/**
 * A class representing the shared methods of all Tiles in the model.
 * Regardless of coordinate system.
 */
public abstract class ATile implements Tile {
  FillType ft;

  @Override
  public void flipTile() {
    switch (this.ft) {
      case BLACK:
        this.ft = FillType.WHITE;
        break;
      case WHITE:
        this.ft = FillType.BLACK;
        break;
      default:
        this.ft = FillType.EMPTY;
        break;
    }
  }

  @Override
  public void setFillType(FillType ft) {
    this.ft = ft;
  }

  @Override
  public boolean hasCoords(int q, int r, int s) throws IllegalArgumentException {
    return false;
  }

  @Override
  public FillType getFT() {
    return this.ft;
  }

  @Override
  public Tile getClone() {
    return null;
  }

  @Override
  public int getQorX() {
    return 0;
  }

  @Override
  public int getRorY() {
    return 0;
  }

  @Override
  public int getS() {
    return 0;
  }

  @Override
  public String toString() {
    return this.ft.toString();
  }
}
