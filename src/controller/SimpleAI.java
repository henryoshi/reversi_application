package controller;


import java.util.ArrayList;
import java.util.Objects;

import model.IReversiModel;
import model.PlayerTurn;

/**
 * Represents an AI that uses a basic strategy to play Reversi.
 * Only tries to maximize the possible points gained in the current turn,
 * and prioritizes top-most, and then left-most to break ties. (lowest R and highest S for axial,
 * lowest x and lowest y for xy plane).
 */
public class SimpleAI implements IReversiPlayer {
  private IReversiModel model;
  private PlayerTurn pt;
  private ArrayList<PlayerListener> listeners;

  /**
   * Default constructor for a SimpleAI.
   *
   * @param model an IReversiModel for this player to execute the respective
   *              commands on.
   */
  public SimpleAI(IReversiModel model, PlayerTurn pt) {
    this.model = Objects.requireNonNull(model);
    this.pt = Objects.requireNonNull(pt);
    this.listeners = new ArrayList<>();
  }

  @Override
  public void playNextMove() {
    if (!model.isGameOver()) {
      int maxTileGain = 0;
      int maxQ = this.model.getRadius();
      int maxR = this.model.getRadius(); // invalid position, so will pass if nothing updates this
      for (int q = -model.getRadius() + 1; q < 2 * model.getRadius(); q++) {
        for (int r = -model.getRadius() + 1; r < 2 * model.getRadius(); r++) {
          if (model.doCoordsExist(q, r, - q - r)) {
            int scoreDiff = this.model.countFlipsForMove(q, r, -q - r);
            if (scoreDiff > maxTileGain
                    || (scoreDiff == maxTileGain && (r < maxR || (maxR == r && q < maxQ)))) {
              maxTileGain = scoreDiff;
              maxQ = q;
              maxR = r;
            }
          }
        }
      }
      if (maxTileGain == 0) {
        model.pass();
        emitMoved();
      } else {
        model.placeTile(maxQ, maxR, -maxQ - maxR);
        emitMoved();
      }
    }
  }

  @Override
  public PlayerTurn getPlayerTurn() {
    return this.pt;
  }

  @Override
  public void addPlayerListener(PlayerListener pl) {
    this.listeners.add(pl);
  }

  private void emitMoved() {
    for (PlayerListener pl : this.listeners) {
      pl.justMoved();
    }
  }
}
