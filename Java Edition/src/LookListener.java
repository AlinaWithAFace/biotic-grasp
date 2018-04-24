import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;

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
	
	// The screen size, we only map the hand's direction to a portion of the screen, because you really only need some of it for a FPS
	private Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	private int xScreenMid = screen.width / 2;
	private int xBuffer = screen.width / 12;
	private int xScreenMin = xScreenMid - xBuffer;
	private int xScreenMax = xScreenMid + xBuffer;
	private int xScreenDiff = xScreenMax - xScreenMin;
	
	private int yScreenMid = screen.height / 2;
	private int yBuffer = screen.height / 12;
	private int yScreenMin = yScreenMid - yBuffer;
	private int yScreenMax = yScreenMid + yBuffer;
	private int yScreenDiff = yScreenMax - yScreenMin;
	
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
		
		for (Hand hand : frame.hands()) {
			if (hand.isRight()) {
				handleHand(hand, interactionBox);
			}
		}
	}
	
	/**
	 * Takes in the right hand and maps its direction to the mouse on screen
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleHand(Hand hand, InteractionBox interactionBox) {
		//TODO: this is still hyper-sensitive, but I'm not sure how to make it better
		Vector rawHandPos = hand.stabilizedPalmPosition();
		Vector boxHandPos = interactionBox.normalizePoint(rawHandPos);
		double newX = MouseInfo.getPointerInfo().getLocation().getX();
		double newY = MouseInfo.getPointerInfo().getLocation().getY();
		
		boolean xInRange = xRightRangeStart < boxHandPos.getX() & boxHandPos.getX() < xRightRangeEnd;
		boolean yInRange = yRightRangeStart < boxHandPos.getY() & boxHandPos.getY() < yRightRangeEnd;
		
		// Is the hand in the overall range?
		if (xInRange) {
			/// Fancy Mathematics to map the right hand's range to the full range, thanks stack overflow
			double newXPos = ((boxHandPos.getX() - xRightRangeStart) * rangeRatio + boxRangeStart);
			newX = (int) ((newXPos * xScreenDiff) + xScreenMin);
		}
		
		if (yInRange) {
			double newYPos = boxHandPos.getY();
			newY = (int) (screen.height - (yScreenDiff * newYPos + yScreenMin));
		}
		//System.out.printf("%d < %f > %d | %d < %f > %d\n", xScreenMin, newX, xScreenMax, yScreenMin, newY, yScreenMax);
		robot.mouseMove((int) newX, (int) newY);
	}
	
}
