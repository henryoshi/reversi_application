package controller;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import model.PlayerTurn;
import provider.cs3500.reversi.model.ROReversiModel;
import provider.cs3500.reversi.strategy.Move;
import provider.cs3500.reversi.strategy.fallible.FallibleReversiStrategy;

/**
 * A class representing a human player for a game of Reversi.
 */
public class HumanPlayer implements IReversiPlayer, FallibleReversiStrategy {
  private PlayerTurn pt;
  private ArrayList<PlayerListener> listeners;

  /**
   * Constructor that takes in what color this player represents.
   *
   * @param pt the color of this player
   */
  public HumanPlayer(PlayerTurn pt) {
    this.pt = Objects.requireNonNull(pt);
    this.listeners = new ArrayList<>();
  }

  @Override
  public void playNextMove() {
    // simply waits until player inputs and view triggers listeners.
  }

  @Override
  public PlayerTurn getPlayerTurn() {
    return this.pt;
  }

  @Override
  public void addPlayerListener(PlayerListener pl) {
    this.listeners.add(pl);
  }

  @Override
  public Optional<Move> chooseMove(ROReversiModel model) throws IllegalArgumentException,
          IllegalStateException {
    // simply waits until player inputs and view triggers listeners.
    try {
      wait();
    } catch (java.lang.InterruptedException e) {
      System.out.println(e.getMessage());
    }
    return Optional.empty();
  }
}
