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
	
	private Robot robot;
	
	private double rightHandXYAxisMidPoint = .75;
	private double rightHandXAxisPadding = .035;
	
	private double rightHandYAxisMidPoint = .5;
	private double rightHandYAxisPadding = .035;
	
	private double xBoundaryHigh = rightHandXYAxisMidPoint + rightHandXAxisPadding;
	private double xBoundaryLow = rightHandXYAxisMidPoint - rightHandXAxisPadding;
	private double yBoundaryHigh = rightHandYAxisMidPoint + rightHandYAxisPadding;
	private double yBoundaryLow = rightHandYAxisMidPoint - rightHandYAxisPadding;
	
	// This range is the interaction box's beginning and end.
	// In other words, normalizing the hand's point direction will give you a value between 0 and 1
	// This is that range
	private double rangeAStart = 0;
	private double rangeAEnd = 1;
	
	// This is the right half of the interaction box.
	// We want to keep the right hand in the right half of the box to give the left it's own space,
	// but we still want full mouse movement, so we map this half to the whole box
	private double rangeBStart = .5;
	private double rangeBEnd = 1;
	
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
		
		for (Hand hand : frame.hands()) {
			if (hand.isRight()) {
				Vector rawHandPos = hand.stabilizedPalmPosition();
				Vector boxHandPos = interactionBox.normalizePoint(rawHandPos);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				MouseInfo.getPointerInfo().getLocation().getX();
				
				if (xBoundaryLow > boxHandPos.getX() | boxHandPos.getX() > xBoundaryHigh) {
					System.out.print("move the mouse maybe X: ");
					System.out.println(xBoundaryLow + " | " + boxHandPos.getX() + " | " + xBoundaryHigh);
					
					/// Fancy Mathematics to map the right range to the full range, thanks stack overflow
					double rangeRatio = ((rangeAEnd - rangeAStart) / (rangeBEnd - rangeBStart));
					double newXPos = ((boxHandPos.getX() - rangeBStart) * rangeRatio + rangeAStart);
					
					int screenX = (int) (screen.width * newXPos);
					robot.mouseMove(screenX, (int) MouseInfo.getPointerInfo().getLocation().getY());
				}
				if (yBoundaryLow > boxHandPos.getY() | boxHandPos.getY() > yBoundaryHigh) {
					System.out.print("move the mouse maybe Y: ");
					System.out.println(yBoundaryLow + " | " + boxHandPos.getY() + " | " + yBoundaryHigh);
					
					double newYPos = boxHandPos.getY();
					
					int screenY = (int) (screen.height - screen.height * newYPos);
					robot.mouseMove((int) MouseInfo.getPointerInfo().getLocation().getX(), screenY);
				}
			}
		}
	}
}
