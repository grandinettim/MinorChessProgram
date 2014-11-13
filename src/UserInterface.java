//Mitchell Kendrioski Chess Tutorial chess.com

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
		static int x = 0,y = 0;
		@Override
		public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.yellow);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		g.setColor(new Color(190, 50,245));
		g.fillRect(200,200, 200, 200);
		g.setColor(Color.red);
		g.fillRect(x - 20,y - 20, 20, 20);
		g.drawString("Mitchell", x, y);
		Image chessPiecesImages;
		chessPiecesImages = new ImageIcon("chess-figures1.png").getImage();
		g.drawImage(chessPiecesImages, x, 0, x+100, 100, x, 0, x+100, 100, this);
		
	}
	@Override
	public void mouseMoved(MouseEvent e){
		x = e.getX();
		y = e.getY();
		repaint();
		
	}
	@Override
	public void mousePressed(MouseEvent e){
		
	}
	@Override
	public void mouseReleased(MouseEvent e){
		
	}
	@Override
	public void mouseClicked(MouseEvent e){
		
	}
	@Override
	public void mouseDragged(MouseEvent e){
		
	}
	@Override
	public void mouseEntered(MouseEvent e){
	
	}
	@Override
	public void mouseExited(MouseEvent e){
	
	}
	
}
