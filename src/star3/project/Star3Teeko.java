package star3.project;

import java.io.IOException;
import java.util.HashMap;

import star3.project.service.JavaService;
import star3.project.service.PrologService;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class TemporaryPawn {
	public int x;
	public int y;
	Field tmpOwner = null;

	TemporaryPawn() {
		reset();
	}

	void reset() {
		x = -1;
		y = -1;
		tmpOwner = Field.EMPTY;
	}

	boolean isInClosestNeighbourhood(int x, int y) {
		boolean state = false;

		// horizontal axis
		if (this.y == y && (this.x - 1 == x || this.x + 1 == x)) {
			state = true;
		} // vertical axis
		else if (this.x == x && (this.y - 1 == y || this.y + 1 == y)) {
			state = true;
		}
		// vertical left
		else if (this.x + 1 == x && (this.y - 1 == y || this.y + 1 == y)) {
			state = true;
		}
		// vertical right
		else if (this.x - 1 == x && (this.y - 1 == y || this.y + 1 == y)) {
			state = true;
		}

		return state;
	}

	boolean isTemporary(int x, int y) {
		if (this.equals(x) && this.equals(y))
			return true;
		else
			return false;
	}

	boolean isAvailable() {

		if (tmpOwner.equals(Field.EMPTY))
			return true;
		else
			return false;
	}

	Field getOwner() {
		return tmpOwner;
	}
}

public class Star3Teeko extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private OrthographicCamera camera;
	private TemporaryPawn tmpPawn = new TemporaryPawn();
	private Texture red, black, texT, gameDeck, win, lose;
	private int countPlayerAPawns = 0;
	private int countPlayerBPawns = 0;
	private GameStatus gameStatus;
	private Field fields[][] = new Field[5][5];
	JavaService js = new JavaService();		
	PrologService ps = new PrologService();
	HashMap<Integer, String> playboard = new HashMap<Integer, String>();
	private boolean stateOfGame=false;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		win = new Texture("Win.jpg");
		lose = new Texture("Lose.jpg");
		red = new Texture("A.png");
		black = new Texture("B.png");
		texT = new Texture("T.png");
		gameDeck = new Texture("GameDeck.png");
		newGame();
	}

	private void newGame() {
		ps.start();
		for(int i=0;i<25;i++)
		{
			playboard.put(i+1, "");
		}
		emptyFields();
		gameStatus = GameStatus.BLACKTURN;

	}

	public boolean checkWinningPositions(int x, int y, Field player) {
		int leftLowerIncline = 0;
		int rightLowerIncline = 0;
		int square = 0;
		int downStroke = 0;
		int rightStroke = 0;

		// left lower incline
		if (x - 3 >= 0 && y + 3 <= 4) {
			for (int i = 0; i < 4; i++) {

				if (fields[x - i][y + i].equals(player))
					leftLowerIncline++;
			}
			if (leftLowerIncline == 4)
				return true;
		}

		// right lower incline
		if (x + 3 <= 4 && y + 3 <= 4) {
			for (int i = 0; i < 4; i++) {

				if (fields[x + i][y + i].equals(player))
					rightLowerIncline++;
			}
			if (rightLowerIncline == 4)
				return true;
		}

		// downStroke
		if (y + 3 <= 4) {
			for (int i = 0; i < 4; i++) {

				if (fields[x][y + i].equals(player))
					downStroke++;
			}
			if (downStroke == 4)
				return true;
		}

		// rightStroke
		if (x + 3 <= 4) {
			for (int i = 0; i < 4; i++) {

				if (fields[x + i][y].equals(player))
					rightStroke++;
			}
			if (rightStroke == 4)
				return true;
		}

		// square
		if (x + 1 <= 4 && y + 1 <= 4) {
			if (fields[x + 1][y].equals(player)
					&& fields[x + 1][y + 1].equals(player)
					&& fields[x][y + 1].equals(player)
					&& fields[x][y].equals(player)) {
				return true;
			}

		}

		return false;
	}

	public void emptyFields() {
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 5; y++)
				fields[x][y] = Field.EMPTY;
		countPlayerAPawns = 0;
		countPlayerBPawns = 0;
		tmpPawn.reset();
	}

	public boolean checkGameStatus(Field playah) {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				if (checkWinningPositions(x, y, playah))
					return true;

			}

		}
		return false;
	}

	@Override
	public void render() {

		try
		{
		Update();
		}
		catch(IOException e){}

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (gameStatus == GameStatus.BLACKTURN
				|| gameStatus == GameStatus.REDTURN) {
			batch.draw(gameDeck, 0, 0);
			for (int x = 0; x < 5; x++)
				for (int y = 0; y < 5; y++) {
					if (fields[x][y] == Field.X) {
						batch.draw(red, 102 * 5 - x * 102
								- (red.getWidth() / 2 - 102 / 2) - 102, 102 * 5
								- y * 102 - (red.getWidth() / 2 - 102 / 2)
								- 102);

					}
					if (fields[x][y] == Field.O) {
						batch.draw(black, 102 * 5 - x * 102
								- (red.getWidth() / 2 - 102 / 2) - 102, 102 * 5
								- y * 102 - (red.getWidth() / 2 - 102 / 2)
								- 102);
					}
					if (fields[x][y] == Field.T) {
						batch.draw(texT, 102 * 5 - x * 102
								- (red.getWidth() / 2 - 102 / 2) - 102, 102 * 5
								- y * 102 - (texT.getWidth() / 2 - 102 / 2)
								- 102);
					}
				}
		} else if (gameStatus == GameStatus.REDWIN) {
			batch.draw(lose, 0, 0);
		} else if (gameStatus == GameStatus.BLACKWIN) {
			batch.draw(win, 0, 0);
		}
		batch.end();
	}

	public void dispose() {
		batch.dispose();
		black.dispose();
		red.dispose();
		gameDeck.dispose();

	}

	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}

	private void Update() throws IOException{

		// game start - empty game deck
		if (Gdx.input.justTouched()
				&& (gameStatus != GameStatus.REDTURN && gameStatus != GameStatus.BLACKTURN))
		{
			dispose();
			System.exit(0);
		}
		else
			

		// red player turn
		if (gameStatus == GameStatus.REDTURN) {
			// if red player clicked empty field and there is less than 4 pawns
			// on
			// game deck
			int fieldX = 0, fieldY = 0;
			if (countPlayerBPawns < 4) {
				countPlayerBPawns++;
				int xy = js.putPawnAI(playboard, ps, countPlayerBPawns);
				System.out.println(xy);
				if(xy%5 != 0)
				{
					fieldX = (xy%5)-1;
					fieldY = (xy/5+1)-1;
				}
				else
				{
					fieldX = ((xy-1)%5);
					fieldY = (xy/5)-1;
				}
				if(fieldX == 0)
				{
					fieldX=4;
				}
				else if(fieldX == 1)
				{
					fieldX=3;
				}
				else if(fieldX == 3)
				{
					fieldX=1;
				}
				else if(fieldX == 4)
				{
					fieldX=0;
				}
				System.out.println(fieldX + " | " + fieldY);
				fields[fieldX][fieldY] = Field.X;
				gameStatus = GameStatus.BLACKTURN;

			} else

			// if red player clicked on his own red pawn and there is at least 4
			// pawns on game deck
			if (countPlayerBPawns >= 4) {
				int[] xy = new int[2];
				xy = js.movePawnAI(playboard, ps);
				tmpPawn.tmpOwner = Field.X;
				if(xy[0]%5 != 0)
				{
					tmpPawn.x = (xy[0]%5)-1;
					tmpPawn.y = (xy[0]/5+1)-1;
				}
				else
				{
					tmpPawn.x = ((xy[0]-1)%5);
					tmpPawn.y = (xy[0]/5)-1;
				}
				if(tmpPawn.x == 0)
				{
					tmpPawn.x=4;
				}
				else if(tmpPawn.x == 1)
				{
					tmpPawn.x=3;
				}
				else if(tmpPawn.x == 3)
				{
					tmpPawn.x=1;
				}
				else if(tmpPawn.x == 4)
				{
					tmpPawn.x=0;
				}
				fields[tmpPawn.x][tmpPawn.y] = Field.T;
				
				fields[tmpPawn.x][tmpPawn.y] = Field.EMPTY;
				tmpPawn.reset();
				if(xy[1]%5 != 0)
				{
					fieldX = (xy[1]%5)-1;
					fieldY = (xy[1]/5+1)-1;
				}
				else
				{
					fieldX = ((xy[1]-1)%5);
					fieldY = (xy[1]/5)-1;
				}
				if(fieldX == 0)
				{
					fieldX=4;
				}
				else if(fieldX == 1)
				{
					fieldX=3;
				}
				else if(fieldX == 3)
				{
					fieldX=1;
				}
				else if(fieldX == 4)
				{
					fieldX=0;
				}
				fields[fieldX][fieldY] = Field.X;
				gameStatus = GameStatus.BLACKTURN;

			}

			// if red player sets winning position
			if (checkGameStatus(Field.X))
			{
				js.endOfGame(playboard, "R");
				gameStatus = GameStatus.REDWIN;
			}
				

		}

		// black player turn
		else if (Gdx.input.justTouched() && gameStatus == GameStatus.BLACKTURN) {
			float posX = -((float) Gdx.input.getX() / Gdx.graphics.getWidth() * 5) + 5;
			float posY = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * 5;

			int fieldX = 0, fieldY = 0;
			if (posX >= 0 && posX <= 1)
				fieldX = 0;
			if (posX > 1 && posX <= 2)
				fieldX = 1;
			if (posX > 2 && posX <= 3)
				fieldX = 2;
			if (posX > 3 && posX <= 4)
				fieldX = 3;
			if (posX > 4 && posX <= 5)
				fieldX = 4;

			if (posY >= 0 && posY <= 1)
				fieldY = 0;
			if (posY > 1 && posY <= 2)
				fieldY = 1;
			if (posY > 2 && posY <= 3)
				fieldY = 2;
			if (posY > 3 && posY <= 4)
				fieldY = 3;
			if (posY > 4 && posY <= 5)
				fieldY = 4;

			// if black player clicked empty field and there is less than 4
			// pawns
			// on
			// game deck
			if (fields[fieldX][fieldY] == Field.EMPTY && countPlayerAPawns < 4) {
				countPlayerAPawns++;
				fields[fieldX][fieldY] = Field.O;
				if(fieldX == 0)
				{
					fieldX=4;
				}
				else if(fieldX == 1)
				{
					fieldX=3;
				}
				else if(fieldX == 3)
				{
					fieldX=1;
				}
				else if(fieldX == 4)
				{
					fieldX=0;
				}
				int xy = Integer.parseInt(Integer.toString(fieldX+1) + Integer.toString(fieldY+1));
				js.putPawnHuman(playboard, ps, countPlayerAPawns, xy);
				gameStatus = GameStatus.REDTURN;

			} else

			// if black player clicked on his own black pawn and there is at
			// least 4
			// pawns on game deck
			if (fields[fieldX][fieldY] == Field.O && countPlayerAPawns >= 4
					&& tmpPawn.isAvailable()) {
				tmpPawn.tmpOwner = Field.O;
				tmpPawn.x = fieldX;
				tmpPawn.y = fieldY;
				fields[fieldX][fieldY] = Field.T;

			} else

			// if black player clicked on green temporary field and there is at
			// least 4 black player pawns
			if (fields[fieldX][fieldY] == Field.T && countPlayerAPawns >= 4
					&& !tmpPawn.isAvailable()) {
				tmpPawn.reset();
				fields[fieldX][fieldY] = Field.O;

			} else

			// if Black player clicked on empty field, temporary pawn (pawn that
			// player wants to move) is selected and clicked empty field is in
			// closest neighbourhood
			if (fields[fieldX][fieldY] == Field.EMPTY && countPlayerBPawns >= 4
					&& !tmpPawn.isAvailable()
					&& tmpPawn.isInClosestNeighbourhood(fieldX, fieldY)) {
				fields[tmpPawn.x][tmpPawn.y] = Field.EMPTY;
				fields[fieldX][fieldY] = Field.O;
				
				if(fieldX == 0)
				{
					fieldX=4;
				}
				else if(fieldX == 1)
				{
					fieldX=3;
				}
				else if(fieldX == 3)
				{
					fieldX=1;
				}
				else if(fieldX == 4)
				{
					fieldX=0;
				}
				
				if(tmpPawn.x == 0)
				{
					tmpPawn.x=4;
				}
				else if(tmpPawn.x == 1)
				{
					tmpPawn.x=3;
				}
				else if(tmpPawn.x == 3)
				{
					tmpPawn.x=1;
				}
				else if(tmpPawn.x == 4)
				{
					tmpPawn.x=0;
				}
				
				int xyold = Integer.parseInt(Integer.toString(tmpPawn.x+1) + Integer.toString(tmpPawn.y+1));
				int xynew = Integer.parseInt(Integer.toString(fieldX+1) + Integer.toString(fieldY+1));
				
				System.out.println(xyold + " | " + xynew);
				
				js.movePawnHuman(playboard, ps, xyold, xynew);
				tmpPawn.reset();
				gameStatus = GameStatus.REDTURN;
			}
			// if red player sets winning position
			if (checkGameStatus(Field.O))
			{
				js.endOfGame(playboard, "B");
				gameStatus = GameStatus.BLACKWIN;
			}

		}

	}

}
