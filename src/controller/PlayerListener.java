package controller;

/**
 * Represents a listener for a player.
 * Used by the AI after a tile move to update the board view and controller turn
 */
public interface PlayerListener {
  /**
   * does what needs to be done after a move,
   * updating the board and telling controller it can't move.
   */
  void justMoved();
}
