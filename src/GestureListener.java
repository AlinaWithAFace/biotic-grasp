import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class GestureListener extends Listener {
	private boolean rightBioticGraspFlag;
	private boolean leftBioticGraspFlag;
	private boolean bioticOrbFlag;
	private boolean coalescenceFlag;

	private double fingerPointingUpNum = .5;
	private int fingerNum = 5;

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
		handleCoalescenceGesture(frame);
		handleBioticOrbGesture(frame);
	}

	/**
	 * pressed q if coalescenceGesture is detected
	 *
	 * @param frame
	 */
	private void handleCoalescenceGesture(Frame frame) {
		boolean gestureFlag = false;

		if (coalescenceGestureDetected(frame)) {
			if (!coalescenceFlag) {
				gestureFlag = true;
			}
			coalescenceFlag = true;
		} else {
			if (coalescenceFlag) {
				gestureFlag = true;
			}
			coalescenceFlag = false;
		}

		if (gestureFlag) {
			System.out.println("coalescenceFlag raised " + coalescenceFlag);
			if (coalescenceFlag) {
				try {
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_Q);
					robot.keyRelease(KeyEvent.VK_Q);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * presses the E button if BioticOrbGesture is detected
	 *
	 * @param frame
	 */
	private void handleBioticOrbGesture(Frame frame) {
		boolean gestureFlag = false;

		if (bioticOrbGestureDetected(frame)) {
			if (!bioticOrbFlag) {
				gestureFlag = true;
			}
			bioticOrbFlag = true;
		} else {
			if (bioticOrbFlag) {
				gestureFlag = true;
			}
			bioticOrbFlag = false;
		}

		if (gestureFlag) {
			System.out.println("bioticOrbFlag raised " + bioticOrbFlag);
			if (bioticOrbFlag) {
				try {
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_E);
					robot.keyRelease(KeyEvent.VK_E);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Hits the left mouse button if the gesture is detected
	 *
	 * @param frame
	 */
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
	 * Hits the right mouse button if the gesture is detected
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


	/**
	 * //todo
	 *
	 * @param frame
	 * @return
	 */
	private boolean bioticOrbGestureDetected(Frame frame) {
		double bioticOrbFingerDirection = -.8;
		int bioticOrbFingerCount = 10;

		if (frame.hands().count() >= 2) {
			int fingerUpCount = 0;
			for (Hand hand : frame.hands()) {
				//System.out.println("hand");
				for (Finger finger : hand.fingers()) {
					Vector pointDirection = finger.direction();
					//System.out.println("bioticOrbGestureDetected " + pointDirection.getZ());

					if (pointDirection.getZ() <= bioticOrbFingerDirection) {
						fingerUpCount++;
					}
				}
			}
			if (fingerUpCount >= bioticOrbFingerCount) {
				//System.out.println("All 10 fingers facing forward?");
				return true;
			}
		}
		return false;
	}

	/**
	 * //todo
	 *
	 * @param frame
	 * @return
	 */
	private boolean coalescenceGestureDetected(Frame frame) {
		return false;
	}

}

