package controller;

import java.util.Objects;
import java.util.Optional;

import model.ProviderModelToReversiModelAdapter;
import provider.cs3500.reversi.player.Player;
import provider.cs3500.reversi.strategy.Move;
import provider.cs3500.reversi.strategy.infallible.InfallibleReversiStrategy;

/**
 * A class that converts from the provider's Player interface to work
 * as an IReversiPlayer for this source code.
 */
public class ProviderInfalliblePlayer implements Player {
  private InfallibleReversiStrategy strategy;
  private ProviderModelToReversiModelAdapter model;

  /**
   * Constructor that takes in what color this player represents.
   *
   * @param strategy a strategy to decide the moves to execute
   * @param model a model to execute the strategy on
   */
  public ProviderInfalliblePlayer(InfallibleReversiStrategy strategy,
                                  ProviderModelToReversiModelAdapter model) {
    this.strategy = Objects.requireNonNull(strategy);
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public Optional<Move> selectMove() {
    return Optional.of(strategy.chooseMove(model));
  }
}
