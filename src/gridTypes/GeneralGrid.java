package gridTypes;

import javafx.scene.shape.Shape;

public abstract class GeneralGrid {
	
	public abstract int[] calculateXDelta(int x, int y);
	
	public abstract int[] calculateYDelta(int x, int y);
	
	public abstract Shape calculateShape();
	
}
