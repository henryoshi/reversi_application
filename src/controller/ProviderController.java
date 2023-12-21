package controller;

import java.util.Objects;
import java.util.Optional;

import model.ModelNotificationListener;
import model.PlayerTurn;
import model.ProviderModelToReversiModelAdapter;
import provider.cs3500.reversi.controller.IController;
import provider.cs3500.reversi.model.Disc;
import provider.cs3500.reversi.model.Hexagon;
import provider.cs3500.reversi.player.Player;
import provider.cs3500.reversi.strategy.Move;
import provider.cs3500.reversi.view.graphical.GraphicalFrameView;

/**
 * A Controller working with the provided "provider view" and strategy implementations.
 */
public class ProviderController implements IController, ModelNotificationListener {
  private ProviderModelToReversiModelAdapter adaptee;
  private GraphicalFrameView view;
  private Player player;

  /**
   * Constructor that takes in a ProviderModel, ProviderView, and ProviderPlayer to execute
   * on.
   * @param adaptee a ProviderModelToReversiModelAdapter to run the game on
   * @param view a GraphicalFramView to display reversi on
   * @param player a Provider given player to compare with
   */
  public ProviderController(ProviderModelToReversiModelAdapter adaptee, GraphicalFrameView view,
                            Player player) {
    this.adaptee = Objects.requireNonNull(adaptee);
    this.view = Objects.requireNonNull(view);
    this.player = Objects.requireNonNull(player);
    this.adaptee.addListener(this);
  }

  @Override
  public void notifyGameStarted() {
    this.view.displayMessage("Game started!");
    this.view.updateView();
  }

  @Override
  public void notifyModelStateUpdated() {
    this.view.updateView();
  }

  @Override
  public void notifyTurnUpdated() {
    this.view.displayMessage("Turn changed!");
  }

  @Override
  public void makeMove(Hexagon hexagon) {
    adaptee.placeDisc(hexagon);
  }

  @Override
  public void pass() {
    adaptee.passTurn();
  }

  @Override
  public void turnFor(PlayerTurn pt) {
    if (pt.equals(PlayerTurn.OVER)) {
      System.out.println("GAME OVER");
      runGameOver();
    } else if (pt.equals(PlayerTurn.BLACK) && !adaptee.gameOver()) {
      System.out.println("turn for " + pt.toString());
      System.out.println("AI provider turn.");
      Optional<Move> nextMove = player.selectMove();
      if (nextMove.isPresent()) {
        Move realMove = nextMove.get();
        if (realMove.getPass()) {
          adaptee.passTurn();
        } else if (realMove.getHexagon().isPresent()) {
          System.out.println("attempting to place hexagon.");
          Hexagon turn = realMove.getHexagon().get();
          adaptee.placeDisc(turn);
        }
      } else {
        adaptee.passTurn();
      }
      view.updateView();
    }
  }

  private void runGameOver() {
    int whiteScore = this.adaptee.getScore(new Disc(Disc.DiscColor.WHITE));
    int blackScore = this.adaptee.getScore(new Disc(Disc.DiscColor.BLACK));
    String endMessage = "";
    if (whiteScore > blackScore) {
      endMessage = "WHITE WINS!";
    } else if (whiteScore == blackScore) {
      endMessage = "IT'S A TIE!";
    } else {
      endMessage = "BLACK WINS!";
    }
    this.view.displayMessage("hello");
    this.view.displayMessage("<html><center>GAME OVER! " + endMessage + "<br>White Score: "
            + whiteScore + "<br>Black Score: " + blackScore + "<html>");
    this.view.updateView();
  }
}