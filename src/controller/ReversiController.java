package controller;

import java.util.Objects;

import model.IReversiModel;
import model.PlayerTurn;
import view.IReversiGUI;

/**
 * A class representing a ReversiController with features as dictated by IReversiController.
 */
public class ReversiController implements IReversiController {
  /**
   * model for this ReversiController.
   */
  IReversiModel model;
  /**
   * player for this ReversiController.
   */
  IReversiPlayer player;
  /**
   * window for this ReversiController.
   */
  IReversiGUI view;
  /**
   * tracks whether it is this ReversiController's turn.
   */
  private boolean thisTurn;

  /**
   * Constructor for a ReversiController, including adding itslf as a listener to all,
   * the model, the player, and the view.
   *
   * @param model  an IReversiModel
   * @param player an IReversiPlayer
   * @param view   a ReversiWindow
   */
  public ReversiController(IReversiModel model, IReversiPlayer player, IReversiGUI view) {
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.player = Objects.requireNonNull(player);
    this.model.addModelListener(this);
    this.view.addTileClickedListener(this);
    this.player.addPlayerListener(this);
    this.thisTurn = false;
  }

  @Override
  public void turnFor(PlayerTurn pt) {
    if (pt.equals(PlayerTurn.OVER)) {
      runGameOver();
    } else {
      if (pt.equals(player.getPlayerTurn()) && !model.isGameOver()) {
        this.thisTurn = true;
        this.view.setLabel("Your turn!");
        this.view.updateBoard();
        this.player.playNextMove();
      } else if (!model.isGameOver()) {
        this.view.setLabel("Not your turn...");
      }
    }
  }

  private void runGameOver() {
    int whiteScore = this.model.getScore(PlayerTurn.WHITE);
    int blackScore = this.model.getScore(PlayerTurn.BLACK);
    thisTurn = false;
    String endMessage;
    if (whiteScore > blackScore) {
      endMessage = "WHITE WINS!";
    } else if (whiteScore == blackScore) {
      endMessage = "IT'S A TIE!";
    } else {
      endMessage = "BLACK WINS!";
    }
    this.view.setLabel("<html><center>" + endMessage + "<br>White Score: " + whiteScore
           + "<br>Black Score: " + blackScore + "</html>");
    this.view.updateBoard();
  }

  @Override
  public void clickedAt(int q, int r) {
    // Clicking is dealt with highlighting tiles delegated to the Tile class.
  }

  @Override
  public void placeTileAttempt(int q, int r) {
    if (thisTurn) {
      boolean attemptedMove = model.isMoveValid(q, r, -q - r);
      if (attemptedMove) {
        this.thisTurn = false;
        this.view.setLabel("Not your turn...");
        model.placeTile(q, r, -q - r);
      } else {
        view.notifyPlayer("Illogical move.");
      }
      view.updateBoard();
    } else {
      notifyInvalid();
    }
  }

  @Override
  public void passAttempt() {
    if (thisTurn) {
      this.thisTurn = false;
      this.view.setLabel("Not your turn...");
      model.pass();
      view.updateBoard();
    } else {
      notifyInvalid();
    }
  }

  private void notifyInvalid() {
    view.notifyPlayer("Not your turn!");
  }

  @Override
  public void justMoved() {
    this.thisTurn = false;
    view.updateBoard();
    this.view.setLabel("Not your turn...");
  }
}
