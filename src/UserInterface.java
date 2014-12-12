//Mitchell Kendrioski Chess Tutorial chess.com

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

@SuppressWarnings("serial")
public class UserInterface extends JPanel implements MouseListener,
		MouseMotionListener {
	private int mouseX, mouseY, newMouseX, newMouseY;
	private int squareSize = 64;
	private AlphaBetaChess game;

	public UserInterface(AlphaBetaChess game) {
		this.game = game;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.yellow);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		for (int i = 0; i < 64; i += 2) {
			g.setColor(new Color(255, 200, 100));
			g.fillRect((i % 8 + (i / 8) % 2) * squareSize,
					(i / 8) * squareSize, squareSize, squareSize);
			g.setColor(new Color(150, 50, 30));
			g.fillRect(((i + 1) % 8 - ((i + 1) / 8) % 2) * squareSize,
					((i + 1) / 8) * squareSize, squareSize, squareSize);
		}
		Image chessPiecesImages;
		chessPiecesImages = new ImageIcon("chess-figures.png").getImage();
		//g.drawImage(chessPiecesImages, x, 0, x + 100, 100, x, 0, x + 100, 100,this);
		for (int i = 0; i < 64; i++) {
			int j = -1, k = -1;
			switch (game.getChessBoard()[i / 8][i % 8]) {
			case "P":
				j = 5;
				k = 0;
				break;
			case "p":
				j = 5;
				k = 1;
				break;
			case "R":
				j = 0;
				k = 0;
				break;
			case "r":
				j = 0;
				k = 1;
				break;
			case "N":
				j = 1;
				k = 0;
				break;
			case "n":
				j = 1;
				k = 1;
				break;
			case "B":
				j = 2;
				k = 0;
				break;
			case "b":
				j = 2;
				k = 1;
				break;
			case "Q":
				j = 3;
				k = 0;
				break;
			case "q":
				j = 3;
				k = 1;
				break;
			case "K":
				j = 4;
				k = 0;
				break;
			case "k":
				j = 4;
				k = 1;
				break;

			}
			if(game.getHumanAsWhite()==0){
				if(k == 0){
					k = 1;
				}else{
					k = 0;
				}
				
			}
			if (j != -1 && k != -1) {
				g.drawImage(chessPiecesImages, (i % 8) * squareSize, (i / 8)
						* squareSize, ((i % 8) + 1) * squareSize, ((i / 8) + 1)
						* squareSize, j * 64, k * 64, (j + 1) * 64,
						(k + 1) * 64, this);
			}
		}

		//g.setColor(new Color(190, 50, 245));
		//g.fillRect(200, 200, 200, 200);
		//g.setColor(Color.red);
		//g.fillRect(x - 20, y - 20, 20, 20);
		//g.drawString("Mitchell", x, y);	
		//chessPiecesImages = new ImageIcon("chess-figures1.png").getImage();
		//g.drawImage(chessPiecesImages, x, 0, x + 100, 100, x, 0, x + 100, 100,this);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getX()<8*squareSize&&e.getY()<8*squareSize){
			//if inside board
			mouseX = e.getX();
			mouseY = e.getY();
			repaint();
			//System.out.println(mouseX/squareSize+","+mouseY/squareSize);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getX()<8*squareSize&&e.getY()<8*squareSize){
			//if inside board
			newMouseX = e.getX();
			newMouseY = e.getY();
			if(e.getButton()==MouseEvent.BUTTON1){
				String dragMove;
				if(newMouseY/squareSize ==0 && mouseY/squareSize ==1 &&"P".equals(game.getChessBoard()[mouseY/squareSize][mouseX/squareSize])){
					//pawn promotion
					dragMove=""+mouseX/squareSize+newMouseX/squareSize+game.getChessBoard()[newMouseY/squareSize][newMouseX/squareSize]+"QP";
				}else{
					//regular move
					dragMove=""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+game.getChessBoard()[newMouseY/squareSize][newMouseX/squareSize];
				}
				String userPossibilities= game.possibleMoves();
				if(userPossibilities.replaceAll(dragMove, "").length()<userPossibilities.length()){
					//if valid move
					game.makeMove(dragMove);
					game.flipboard();
					game.makeMove(game.alphaBeta("", 0));
					game.flipboard();
					repaint();
				}
				
			}
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
