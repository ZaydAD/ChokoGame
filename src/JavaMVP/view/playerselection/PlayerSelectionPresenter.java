package JavaMVP.view.playerselection;

import JavaMVP.model.ComputerPlayer;
import JavaMVP.model.GameSession;
import JavaMVP.model.MVPModel;
import JavaMVP.view.UISettings;
import JavaMVP.view.board.BoardScreenPresenter;
import JavaMVP.view.board.BoardScreenView;
import enums.PieceColor;
import javafx.stage.Stage;

public class PlayerSelectionPresenter {

    private final PlayerSelectionView view;
    private final MVPModel model;
    private final UISettings uiSettings = new UISettings();
    private final Stage primaryStage;
    private boolean isPlayingBlack;
    private boolean dropInitiative;

    public PlayerSelectionPresenter(PlayerSelectionView view, MVPModel model, Stage primaryStage) {
        this.view = view;
        this.model = model;
        this.primaryStage = primaryStage;

        attachEventHandlers();
    }

    /**
     * Attaches event handlers to the buttons and checkbox within the player selection view.
     * - Handles selecting a player color (black or white).
     * - Starts a new game session when the "Start Game" button is pressed.
     */
    private void attachEventHandlers() {
        view.getBlackButton().setOnAction(e -> selectColor(true));
        view.getWhiteButton().setOnAction(e -> selectColor(false));

        view.getStartGameButton().setOnAction(e -> {
            model.setGameSession(new GameSession(model));

            ComputerPlayer computer = new ComputerPlayer();
            computer.setColor(isPlayingBlack ? PieceColor.WHITE : PieceColor.BLACK);
            model.getGameSession().setComputerAi(computer);
            model.getGameSession().setUserIsBlack(isPlayingBlack);

            dropInitiative = view.getDropInitiativeCheckbox().isSelected();
            model.getGameSession().setDropInitiativeEnabled(true);

            PieceColor currentTurnColor;
            if (!dropInitiative) {
                currentTurnColor = isPlayingBlack ? PieceColor.WHITE : PieceColor.BLACK;
            } else {
                currentTurnColor = isPlayingBlack ? PieceColor.BLACK : PieceColor.WHITE;
            }

            model.getGameSession().setCurrentTurn(currentTurnColor);
            model.getGameSession().setDropInitiativeHolder(currentTurnColor);

            model.getGameSession().play();
            switchToBoardScreen();
        });
    }

    private void selectColor(boolean playAsBlack) {
        isPlayingBlack = playAsBlack;
    }

    /**
     * Transitions from the player selection screen to the board screen.
     * Sets up the board screen view and presenter, and updates the scene root for the transition.
     */
    private void switchToBoardScreen() {
        if (model.getGameSession().getBoard() == null) {
            model.setGameSession(new GameSession(model));
        }

        double boardSize = uiSettings.getLowestRes() / 1.8;
        BoardScreenView boardScreenView = new BoardScreenView(model.getGameSession().getBoard(), boardSize);
        BoardScreenPresenter boardScreenPresenter = new BoardScreenPresenter(
                boardScreenView,
                model,
                model.getGameSession(),
                uiSettings,
                (Stage) view.getScene().getWindow()
        );

        view.getScene().setRoot(boardScreenView);
    }
}