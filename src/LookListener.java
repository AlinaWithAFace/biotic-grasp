import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

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
		Frame frame = controller.frame();

		handleLookLeft(frame);
		handleLookRight(frame);
		//handleLookDown(frame);
		//handleLookUp(frame);
	}

	private int mouseMovementAddition = 25;
	private double zBound = -.8;

	private void handleLookUp(Frame frame) {
		if (lookUpDetected(frame)) {
			System.out.println("lookUpDetected");
			try {
				Robot robot = new Robot();
				Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(currentMouseLocation.x, currentMouseLocation.y + mouseMovementAddition);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleLookDown(Frame frame) {
		if (lookDownDetected(frame)) {
			System.out.println("lookDownDetected");
			try {
				Robot robot = new Robot();
				Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(currentMouseLocation.x, currentMouseLocation.y - mouseMovementAddition);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleLookRight(Frame frame) {
		if (lookRightDetected(frame)) {
			System.out.println("lookRightDetected");
			try {
				Robot robot = new Robot();
				Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(currentMouseLocation.x + mouseMovementAddition, currentMouseLocation.y);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleLookLeft(Frame frame) {
		if (lookLeftDetected(frame)) {
			System.out.println("lookLeftDetected");
			try {
				Robot robot = new Robot();
				Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(currentMouseLocation.x - mouseMovementAddition, currentMouseLocation.y);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean lookLeftDetected(Frame frame) {
		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isRight()) {
					//System.out.println(hand.direction());
					if (hand.direction().getZ() >= zBound) {
						if (hand.direction().getX() < 0) {
							//TODO: it looks left sometimes when you kinda wanna look up, so that's broken, maybe figure out a way to more solidly identify desired vectors and use the hand's direction's difference from there?
							//if (.25 < hand.direction().getY() && hand.direction().getY() < .5) {
							//System.out.println("look left?");
							//System.out.println(hand.direction());
							return true;
							//}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean lookRightDetected(Frame frame) {
		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isRight()) {
					if (hand.direction().getZ() <= zBound) {
						if (hand.direction().getX() > 0) {
							//System.out.println("look right?");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean lookUpDetected(Frame frame) {
		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isRight()) {
					//System.out.println(hand.direction().getY());
					if (hand.direction().getY() > 0) {
						System.out.println("look up?");
						//todo
					}
				}
			}
		}
		return false;
	}

	private boolean lookDownDetected(Frame frame) {
		//todo fix up first then clone it here
		return false;
	}

}
