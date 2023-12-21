import org.junit.Test;

import controller.HumanPlayer;
import controller.SimpleAI;
import controller.IReversiPlayer;
import controller.ReversiController;
import model.SquareReversiModel;
import view.HexReversiGUI;
import model.IReversiModel;
import model.PlayerTurn;
import model.HexReversiModel;
import model.MockModel;
import view.SquareReversiGUI;

import static org.junit.Assert.assertEquals;


/**
 * Class to test the string interpolation of args in main
 * Also includes method used for transcript generation for SimpleAI.
 */
public class TestMain {

  @Test (expected = IllegalArgumentException.class)
  public void testBadInput() {
    String[] args = {"human", "dog"};
    Reversi.main(args);
  }

  @Test
  public void simpleAITranscript() {
    HexReversiModel real = new HexReversiModel(5);
    IReversiModel mock = new MockModel(real);
    HexReversiGUI view1 = new HexReversiGUI(mock);
    HexReversiGUI view2 = new HexReversiGUI(mock);

    IReversiPlayer aiPlayer = new SimpleAI(mock, PlayerTurn.WHITE);
    IReversiPlayer humanPlayer = new HumanPlayer(PlayerTurn.BLACK);

    ReversiController controller1 = new ReversiController(mock, aiPlayer, view1);
    ReversiController controller2 = new ReversiController(mock, humanPlayer, view2);
    System.out.println("starting mock");
    assertEquals(mock.getScore(PlayerTurn.BLACK), 3);
    mock.startGame();
  }

  @Test
  public void simpleAISquareTranscript() {
    SquareReversiModel real = new SquareReversiModel(4);
    IReversiModel mock = new MockModel(real);
    SquareReversiGUI view1 = new SquareReversiGUI(mock);
    SquareReversiGUI view2 = new SquareReversiGUI(mock);

    IReversiPlayer aiPlayer = new SimpleAI(mock, PlayerTurn.WHITE);
    IReversiPlayer humanPlayer = new HumanPlayer(PlayerTurn.BLACK);

    ReversiController controller1 = new ReversiController(mock, aiPlayer, view1);
    ReversiController controller2 = new ReversiController(mock, humanPlayer, view2);
    System.out.println("starting mock");
    assertEquals(mock.getScore(PlayerTurn.BLACK), 2);
    mock.startGame();
  }
}
