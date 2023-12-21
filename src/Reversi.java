import controller.HumanPlayer;
import controller.IReversiPlayer;
import controller.ProviderFalliblePlayer;
import controller.ProviderInfalliblePlayer;
import controller.ReversiController;
import controller.SimpleAI;
import model.IReversiModel;
import model.PlayerTurn;
import model.ProviderModelToReversiModelAdapter;
import model.ReadOnlyReversiImpl;
import model.ReadOnlyReversiModel;
import model.HexReversiModel;
import model.SquareReversiModel;
import provider.cs3500.reversi.strategy.infallible.AvoidCornerAdjacencyMaxScore;
import provider.cs3500.reversi.strategy.infallible.CaptureMostPieces;
import provider.cs3500.reversi.strategy.infallible.CherryPickerCMSOptimizer;
import provider.cs3500.reversi.strategy.infallible.OptimizeCornerStratMaxScore;
import provider.cs3500.reversi.strategy.infallible.PlayCornersMaxScore;
import provider.cs3500.reversi.player.Player;
import view.HexReversiGUI;
import view.IReversiGUI;
import view.SquareReversiGUI;

/**
 * Main class to run the Reversi game.
 */
public final class Reversi {
  /**
   * Provided code modified to start and view a new Reversi game.
   * args as follows: modelType, playerType, playerType, OPTIONAL radius
   * modelType: hex, square (defaults to square)
   * playerType: human, simpleAI (defaults to human)
   * radius: integer greater than 1 (by default set to 5)
   *
   * @param args the string arguments to run the game
   */
  public static void main(String[] args) {
    IReversiModel mutableModel;
    if (args.length < 3 || args.length > 4) {
      throw new IllegalArgumentException("Please provide 3 or 4 valid parameters as described.");
    } else if (args.length == 4) {
      mutableModel = parseModel(args[0], Integer.parseInt(args[3]));
    } else {
      mutableModel = parseModel(args[0], 5);
    }

    ReadOnlyReversiModel model = new ReadOnlyReversiImpl(mutableModel);
    //ProviderModelToReversiModelAdapter model2 = new ProviderModelToReversiModelAdapter(realModel);


    //GraphicalFrameView view2 = new JFrameView(model2);

    IReversiPlayer player1 = makePlayer1(args[1], mutableModel, PlayerTurn.WHITE);
    IReversiPlayer player2 = makePlayer1(args[2], mutableModel, PlayerTurn.BLACK);
    //Player player2 = makePlayer2(args[1], model2);

    IReversiGUI view1;
    IReversiGUI view2;

    if (args[0].equals("hex")) {
      view1 = new HexReversiGUI(model);
      view2 = new HexReversiGUI(model);
    } else {
      view1 = new SquareReversiGUI(model);
      view2 = new SquareReversiGUI(model);
    }

    ReversiController controller1 = new ReversiController(mutableModel, player1, view1);
    ReversiController controller2 = new ReversiController(mutableModel, player2, view2);

    //ProviderController controller2 = new ProviderController(model2, view2, player2);

    System.out.println("new reversi main");

    mutableModel.startGame();
  }

  private static IReversiModel parseModel(String arg, int radius) {
    IReversiModel model;
    if (arg.equals("hex")) {
      model = new HexReversiModel(radius);
    } else {
      model = new SquareReversiModel(radius);
    }
    return model;
  }

  private static IReversiPlayer makePlayer1(String arg, IReversiModel model, PlayerTurn pt) {
    if (arg.equals("simpleAI")) {
      return new SimpleAI(model, pt);
    }
    return new HumanPlayer(pt);
  }

  /**
   * Used for provider's players and views.
   *
   * @param arg   type of player to create
   * @param model an adapted model to convert between root logic and provided views/players
   * @return a new Player for use in game
   */
  private static Player makePlayer2(String arg, ProviderModelToReversiModelAdapter model) {
    switch (arg) {
      case "Human":
        return new ProviderFalliblePlayer(new HumanPlayer(PlayerTurn.BLACK), model);
      case "AvoidCornerAdjacencyMaxScore":
        return new ProviderInfalliblePlayer(new AvoidCornerAdjacencyMaxScore(), model);
      case "CaptureMostPieces":
        return new ProviderInfalliblePlayer(new CaptureMostPieces(), model);
      case "CherryPickerCMSOptimizer":
        return new ProviderInfalliblePlayer(new CherryPickerCMSOptimizer(), model);
      case "OptimizeCornerStratMaxScore":
        return new ProviderInfalliblePlayer(new OptimizeCornerStratMaxScore(), model);
      default:
        return new ProviderInfalliblePlayer(new PlayCornersMaxScore(), model);
    }
  }
}