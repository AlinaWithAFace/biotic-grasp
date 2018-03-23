import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;

/*
todo: make it not incremental
at the moment no matter how fast you swipe to a side you only look that direction at a set rate,
make it work with the velocity of a turn or something
 */

/**
 * Handles the right hand's direction to move the mouse
 */
public class LookListener extends Listener {
	
	public Robot robot;
	
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}
	
	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}
	
	public void onDisconnect(Controller controller) {
		//Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}
	
	public void onExit(Controller controller) {
		System.out.println("Exited");
	}
	
	public void onFrame(Controller controller) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		Frame frame = controller.frame();
		InteractionBox interactionBox = frame.interactionBox();
		
		for (Hand f : frame.hands()) {
			if (f.isRight()) {
				Vector fingerPos = f.stabilizedPalmPosition();
				Vector boxfingerPos = interactionBox.normalizePoint(fingerPos);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				robot.mouseMove((int) (screen.width * boxfingerPos.getX()), (int) (screen.height - screen.height * boxfingerPos.getY()));
			}
		}
	}
}
