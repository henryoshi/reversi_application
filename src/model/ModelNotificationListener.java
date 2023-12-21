package model;

/**
 * A features interface for a Reversi Model, which update any classes
 * implementing this interface as listeners.
 */
public interface ModelNotificationListener {
  /**
   * Notifies the object extending this interface as a listener that
   * it is its turn to play Reversi.
   */
  void turnFor(PlayerTurn pt);
}
