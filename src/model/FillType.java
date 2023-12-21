package model;

/**
 * An enumeration representing the three different ways a tile can be filled
 * in this implementation of Reversi. Empty, when noone has placed a piece on
 * this tile, white or black depending on which player flipped/placed on this
 * tile.
 */
public enum FillType {
  EMPTY("_"), WHITE("X"), BLACK("O");

  private final String name;

  /**
   * Sets the name of this Enum to the passed string.
   *
   * @param name the String representation of the Enum
   */
  FillType(String name) {
    this.name = name;
  }

  /**
   * Returns the string representation of this enum.
   *
   * @return a string representing this enum
   */
  @Override
  public String toString() {
    return this.name;
  }
}
