package model;

/**
 * Enumeration of the two players in a game of Reversi,
 * the black tiles, or the white tiles.
 */
public enum PlayerTurn {
  WHITE, BLACK, OVER;

  /**
   * Changes whose turn it is in the current Reversi game.
   *
   * @return the opposite PlayerTurn enum
   */
  public PlayerTurn flip() {
    if (this.equals(PlayerTurn.WHITE)) {
      return PlayerTurn.BLACK;
    } else if (this.equals(PlayerTurn.BLACK)) {
      return PlayerTurn.WHITE;
    }
    throw new IllegalStateException("flip was attempted with PlayerTurn OVER");
  }
}
