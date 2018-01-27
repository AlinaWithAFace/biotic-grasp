import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;
import jdk.internal.util.xml.impl.Input;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class GestureListener extends Listener {
	private boolean rightBioticGraspFlag;
	private boolean leftBioticGraspFlag;

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

		handleRightBioticGraspGesture(frame);
		handleLeftBioticGraspGesture(frame);

	}

	private void handleLeftBioticGraspGesture(Frame frame) {
		boolean gestureFlag = false;

		if (leftBioticGraspGestureDetected(frame)) {
			if (!leftBioticGraspFlag) {
				gestureFlag = true;
			}
			leftBioticGraspFlag = true;
		} else {
			if (leftBioticGraspFlag) {
				gestureFlag = true;
			}
			leftBioticGraspFlag = false;
		}

		if (gestureFlag) {
			System.out.println("leftBioticGraspFlag raised " + leftBioticGraspFlag);
			if (leftBioticGraspFlag) {
				try {
					Robot robot = new Robot();
					robot.mousePress(InputEvent.BUTTON1_MASK);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Robot robot = new Robot();
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Presses a button on call depending on whether or not the gesture is happening
	 */
	private void handleRightBioticGraspGesture(Frame frame) {
		boolean gestureFlag = false;

		if (rightBioticGraspGestureDetected(frame)) {
			if (!rightBioticGraspFlag) {
				gestureFlag = true;
			}
			rightBioticGraspFlag = true;
		} else {
			if (rightBioticGraspFlag) {
				gestureFlag = true;
			}
			rightBioticGraspFlag = false;
		}

		if (gestureFlag) {
			System.out.println("rightBioticGraspFlag raised: " + rightBioticGraspFlag);
			if (rightBioticGraspFlag) {
				try {
					Robot robot = new Robot();
					robot.mousePress(InputEvent.BUTTON3_MASK);
					//robot.keyPress(KeyEvent.VK_Q);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Robot robot = new Robot();
					robot.mouseRelease(InputEvent.BUTTON3_MASK);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private double fingerPointingUpNum = .5;
	private int fingerNum = 5;

	/**
	 * detects if 5 fingers on the right hand are pointing up
	 *
	 * @param frame
	 * @return
	 */
	private boolean rightBioticGraspGestureDetected(Frame frame) {
		if ((frame.hands().count() > 0)) {
			for (Hand hand : frame.hands()) {
				int fingerUpCount = 0;
				for (Finger finger : frame.fingers()) {
					Vector pointDirection = finger.direction();
					if (pointDirection.getY() > fingerPointingUpNum) {
						//System.out.println("finger pointing up?");
						fingerUpCount++;
					}
				}
				if (hand.isRight()) {
					if (fingerUpCount >= fingerNum) {
						//System.out.println("All 5 fingers pointing up : right hand");
						return true;
					}
				}


			}
		} else {
			return false;
		}
		return false;
	}


	/**
	 * detects if 5 fingers on the left hand are pointing up
	 *
	 * @param frame
	 * @return
	 */
	private boolean leftBioticGraspGestureDetected(Frame frame) {
		if ((frame.hands().count() > 0)) {
			for (Hand hand : frame.hands()) {
				int fingerUpCount = 0;
				for (Finger finger : hand.fingers()) {
					Vector pointDirection = finger.direction();
					if (pointDirection.getY() > fingerPointingUpNum) {
						fingerUpCount++;
					}
				}
				if (hand.isLeft()) {
					if (fingerUpCount >= fingerNum) {
						//System.out.println("All 5 fingers pointing up : left hand");
						return true;
					}
				}
			}
		}
		return false;
	}


}

