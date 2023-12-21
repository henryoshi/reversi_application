package controller;

import model.ModelNotificationListener;
import view.TileClickedListener;

/**
 * Interface representing the public-facing methods of a Reversi Controller.
 */
public interface IReversiController extends PlayerListener, ModelNotificationListener,
        TileClickedListener {
  // Feature interface is composed of PlayerListener, ModelNotificationListener,
  // and TileClickedListener interface methods.
}
