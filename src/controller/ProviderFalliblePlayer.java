package controller;

import java.util.Objects;
import java.util.Optional;

import model.ProviderModelToReversiModelAdapter;
import provider.cs3500.reversi.player.Player;
import provider.cs3500.reversi.strategy.Move;
import provider.cs3500.reversi.strategy.fallible.FallibleReversiStrategy;

/**
 * A class that converts from the provider's Player interface to work
 * as an IReversiPlayer for this source code.
 */
public class ProviderFalliblePlayer implements Player {
  private FallibleReversiStrategy strategy;
  private ProviderModelToReversiModelAdapter model;

  /**
   * Constructor that takes in what color this player represents.
   *
   */
  public ProviderFalliblePlayer(FallibleReversiStrategy strategy,
                                ProviderModelToReversiModelAdapter model) {
    this.strategy = Objects.requireNonNull(strategy);
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public Optional<Move> selectMove() {
    return strategy.chooseMove(model);
  }
}
