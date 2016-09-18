package com.Horunkan.Draughts.Views;

import com.Horunkan.Draughts.Draughts;
import com.Horunkan.Draughts.Game.GUI.DrawCell;
import com.Horunkan.Draughts.Game.GUI.DrawPawn;
import com.Horunkan.Draughts.Game.GUI.PlayerInfo;
import com.Horunkan.Draughts.Game.Logic.Board;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameScreen extends AbstractScreen {
	private Skin skin;
	private Table boardCellContainer;
	private Board board;
	private DrawCell[][] boardCells;
	private DrawPawn[] pawnsBright, pawnsDark;
	private PlayerInfo playerDark, playerBright;
	
	

	public GameScreen(Draughts game) {
		super(game);
		skin = new Skin();
		loadTextures();
	}
	
	@Override public void render(float delta) {
		//Clear view
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.setScreen(Draughts.ScreenMode.MAIN_MENU);
		
		stage.draw();
		if(Draughts.debug)board.renderDebug();
	}
	
	@Override public void show() {
		super.show();
		newGame();
	}
	
	private void newGame() {
		System.out.println("\nNew Game\n");
		for(Actor act : stage.getActors()) act.remove();
		
		boardCellContainer = new Table();
		boardCellContainer.setFillParent(true);
		board = new Board(this);
		loadBoard();
		
		pawnsBright = new DrawPawn[board.countPawns(2)];
		pawnsDark = new DrawPawn[board.countPawns(3)];
		
		stage.addActor(boardCellContainer);
		boardCellContainer.validate();
		
		loadPawnsGroups();
		
		if(Draughts.debug) board.debug(this, board, boardCells, pawnsBright, pawnsDark);
		
		playerDark = new PlayerInfo(skin, "Player A");
		playerDark.setPosition(60, 100);
		stage.addActor(playerDark);
		
		playerBright = new PlayerInfo(skin, "Player B");
		playerBright.setPosition(720, 500);
		stage.addActor(playerBright);	
	}
	
	public void removePawn(int x, int y) {
		for(DrawPawn pawn : pawnsBright) {
			if(pawn.getBoardPosition().isEqual(x, y)) {
				pawn.remove();
				pawn.setBoardPosition(900, 900);
				break;
			}
		}
		for(DrawPawn pawn : pawnsDark) {
			if(pawn.getBoardPosition().isEqual(x, y)) {
				pawn.remove();
				pawn.setBoardPosition(900, 900);
				break;
			}
		}
	}
	
	private void loadTextures() {
		skin.add("boardBright", new Texture(Gdx.files.internal("Textures/boardCell_bright.png")));
		skin.add("boardDark", new Texture(Gdx.files.internal("Textures/boardCell_dark.png")));
		skin.add("pawnBright", new Texture(Gdx.files.internal("Textures/pawn_bright.png")));
		skin.add("pawnDark", new Texture(Gdx.files.internal("Textures/pawn_dark.png")));
	}
	
	private void loadBoard() {
		int width = board.getWidth();
		int height = board.getHeight();
		boardCells = new DrawCell[width][height];
		
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				boardCells[x][y] = new DrawCell(skin, board, board.getValue(x, y), x, y);
				boardCellContainer.add(boardCells[x][y]).size(65);
			}
			boardCellContainer.row();
		}
	}
	
	private void loadPawnsGroups() {
		for(int y = 0, i = 0, j = 0; y < board.getHeight(); ++y) {
			for(int x = 0; x < board.getWidth(); ++x) {
				if(board.getValue(x, y) == 2) {
					pawnsBright[i] = new DrawPawn(skin, board, 2, x, y);
					pawnsBright[i].setPosition(boardCells[x][y].getPosition().x, boardCells[x][y].getPosition().y);
					stage.addActor(pawnsBright[i]);
					++i;
				}
				else if(board.getValue(x, y) == 3) {
					pawnsDark[j] = new DrawPawn(skin, board, 3, x, y);
					pawnsDark[j].setPosition(boardCells[x][y].getPosition().x, boardCells[x][y].getPosition().y);
					stage.addActor(pawnsDark[j]);
					++j;
				}
			}
		}
	}
}
