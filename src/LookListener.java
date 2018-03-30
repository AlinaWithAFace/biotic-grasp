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
	
	// This range is the interaction box's beginning and end.
	// In other words, normalizing the hand's point direction will give you a value between 0 and 1
	// This is that range
	private double boxRangeStart = 0;
	private double boxRangeEnd = 1;
	
	// This is the right half of the interaction box.
	// We want to keep the right hand in the right half of the box to give the left it's own space,
	// but we still want full mouse movement, so we map this half to the whole box
	private double xRightRangeStart = .5;
	private double xRightRangeEnd = 1;
	
	private double yRightRangeStart = 0;
	private double yRightRangeEnd = 1;
	
	// The ratio between the ranges, i.e. the thing used for mapping from on range to the other
	private double rangeRatio = ((boxRangeEnd - boxRangeStart) / (xRightRangeEnd - xRightRangeStart));
	
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
		
		// TODO: I'm not really sure how to make this smooth out more, currently it maps 1:1 (ish) from your hand's
		// position to the mouse, but because hands are super shaky because human, this causes a lot of unnecessary
		// micro-corrections in game leading to hyper sensitivity, especially when Overwatch moves your mouse back to
		// the center of the screen. Maybe try some sort of acceleration based mouse movement? Figure out how you'd
		// actually like it to work, safe zones don't work here
		
		for (Hand hand : frame.hands()) {
			if (hand.isRight()) {
				Vector rawHandPos = hand.stabilizedPalmPosition();
				Vector boxHandPos = interactionBox.normalizePoint(rawHandPos);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				double newX = MouseInfo.getPointerInfo().getLocation().getX();
				double newY = MouseInfo.getPointerInfo().getLocation().getY();
				
				boolean xInRange = xRightRangeStart < boxHandPos.getX() & boxHandPos.getX() < xRightRangeEnd;
				boolean yInRange = yRightRangeStart < boxHandPos.getY() & boxHandPos.getY() < yRightRangeEnd;
				
				// Is the hand in the overall range?
				if (xInRange) {
					/// Fancy Mathematics to map the right hand's range to the full range, thanks stack overflow
					double newXPos = ((boxHandPos.getX() - xRightRangeStart) * rangeRatio + boxRangeStart);
					newX = (int) (screen.width * newXPos);
				}
				
				if (yInRange) {
					double newYPos = boxHandPos.getY();
					newY = (int) (screen.height - screen.height * newYPos);
				}
				robot.mouseMove((int) newX, (int) newY);
			}
		}
	}
}
