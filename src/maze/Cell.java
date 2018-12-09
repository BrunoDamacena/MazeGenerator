package maze;
import java.awt.Color;


public class Cell {
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private int x;
	private int y;
	
	private Color col;
	
	public Cell(int setx, int sety) {
		x = setx;
		y = sety;
		col = Color.black;
	}
	
	public Color getColor() {return col;}
	public void setColor(Color newCol) {col = newCol;}
        public boolean isWalkable(){
            if(col == Color.black){
                return false;
            }
            else return true;
        }

	public int getx() {return x;}
	public int gety() {return y;}
}
