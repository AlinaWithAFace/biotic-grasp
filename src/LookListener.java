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
	
	
	private double yAxisMidPoint = .5;
	private double yAxisPadding = .035;
	
	private double yBoundaryHigh = yAxisMidPoint + yAxisPadding;
	private double yBoundaryLow = yAxisMidPoint - yAxisPadding;
	
	// This range is the interaction box's beginning and end.
	// In other words, normalizing the hand's point direction will give you a value between 0 and 1
	// This is that range
	private double boxRangeAStart = 0;
	private double boxRangeAEnd = .5;
	
	private double boxRangeBStart = .5;
	private double boxRangeBEnd = 1;
	
	
	// This is the right half of the interaction box.
	// We want to keep the right hand in the right half of the box to give the left it's own space,
	// but we still want full mouse movement, so we map this half to the whole box
	// It's divided by a middle safe area, because hands are naturally going to be very shaky, but we want the user
	// to be able to hold their hand mostly in place in order to keep the mouse in the same place
	private double xAxisMidPoint = .75;
	private double xAxisPadding = .05;
	
	private double xRangeAStart = .5;
	private double xRangeAEnd = xAxisMidPoint - xAxisPadding;
	
	private double xRangeBStart = xAxisMidPoint + xAxisPadding;
	private double xRangeBEnd = 1;
	
	
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
		
		//TODO: So, it's kind of odd, it skips over the safe zone area, so maybe try having it "skip" into it using the difference?
		
		for (Hand hand : frame.hands()) {
			if (hand.isRight()) {
				Vector rawHandPos = hand.stabilizedPalmPosition();
				Vector boxHandPos = interactionBox.normalizePoint(rawHandPos);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				double mouseX = MouseInfo.getPointerInfo().getLocation().getX();
				double mouseY = MouseInfo.getPointerInfo().getLocation().getY();
				
				boolean inRangeA = xRangeAStart < boxHandPos.getX() & boxHandPos.getX() < xRangeAEnd;
				boolean inRangeB = xRangeBStart < boxHandPos.getX() & boxHandPos.getX() < xRangeBEnd;
				
				System.out.println(boxHandPos.getX() + " | " + xRangeAStart + " - " + xRangeAEnd + " | " + inRangeA + " || " + xRangeBStart + " - " + xRangeBEnd + " | " + inRangeB);
				if (inRangeA) {
					double rangeRatio = ((boxRangeAEnd - boxRangeAStart) / (xRangeAEnd - xRangeAStart));
					double newXPos = ((boxHandPos.getX() - xRangeAStart) * rangeRatio + boxRangeAStart);
					
					int screenX = (int) (screen.width * newXPos);
					robot.mouseMove(screenX, (int) mouseY);
					
				} else if (inRangeB) {
					double rangeRatio = ((boxRangeBEnd - boxRangeBStart) / (xRangeBEnd - xRangeBStart));
					double newXPos = ((boxHandPos.getX() - xRangeBStart) * rangeRatio + boxRangeBStart);
					
					int screenX = (int) (screen.width * newXPos);
					robot.mouseMove(screenX, (int) mouseY);
				}
				
				
				// Is the hand in the overall range?
//				if (xRangeAEnd > boxHandPos.getX() | boxHandPos.getX() > xRangeBStart) {
////					System.out.print("move the mouse maybe X: ");
////					System.out.println(xRangeAEnd + " | " + boxHandPos.getX() + " | " + xRangeBStart);
//
//					/// Fancy Mathematics to map the right hand's range to the full range, thanks stack overflow
//
//				}

//				if (yBoundaryLow > boxHandPos.getY() | boxHandPos.getY() > yBoundaryHigh) {
//					System.out.print("move the mouse maybe Y: ");
//					System.out.println(yBoundaryLow + " | " + boxHandPos.getY() + " | " + yBoundaryHigh);
//
//					double newYPos = boxHandPos.getY();
//
//					int screenY = (int) (screen.height - screen.height * newYPos);
//					robot.mouseMove((int) mouseX, screenY);
//				}
			}
		}
	}
}
