import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * Handles WASD movement using the left hand
 */
public class MovementListener extends Listener {

	public Robot robot;
	
	private boolean moveForwardFlag;
	private boolean moveBackwardFlag;
	private boolean moveLeftFlag;
	private boolean moveRightFlag;

	private double leftHandZAxisMidPoint = .5;
	private double leftHandXAxisMidPoint = .25;
	private double leftHandXZAxisPadding = .18;

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
		InteractionBox interactionBox = frame.interactionBox();

		handleForward(frame, interactionBox);
		handleBackward(frame, interactionBox);
		handleRight(frame, interactionBox);
		handleLeft(frame, interactionBox);

	}

	/**
	 * detects if the palm is farther to you relative to the interaction box
	 *
	 * @param frame
	 * @param interactionBox
	 * @return
	 */
	private boolean forwardMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		double zMin = leftHandZAxisMidPoint - leftHandXZAxisPadding;

		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
					//System.out.println(palmPosition);

					if (palmPosition.getZ() <= zMin) {
						//System.out.println(" move forward maybe");
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * detects if the palm is closeish to you relative to the interaction box
	 *
	 * @param frame
	 * @param interactionBox
	 * @return
	 */
	private boolean backwardMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		double zMax = leftHandZAxisMidPoint + leftHandXZAxisPadding;

		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());

					if (palmPosition.getZ() >= zMax) {
						//System.out.println(" move backward maybe");
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * detects if the left hand is over the right side of the left side (because that makes sense)
	 *
	 * @param frame
	 * @param interactionBox
	 * @return
	 */
	private boolean rightMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		double xBoundary = leftHandXAxisMidPoint + leftHandXZAxisPadding;

		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
					if (palmPosition.getX() >= xBoundary) {
						//System.out.println("move right maybe");
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * detects if the left hand is over in the leftmost range
	 *
	 * @param frame
	 * @param interactionBox
	 * @return
	 */
	private boolean leftMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		double xBoundary = leftHandXAxisMidPoint - leftHandXZAxisPadding;

		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
					//System.out.println(palmPosition.getX());
					if (palmPosition.getX() <= xBoundary) {
						//System.out.println("move left maybe");
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * pushes the w key if forwardMovementHoverDetected is true
	 *
	 * @param frame
	 * @param interactionBox
	 */
	private void handleForward(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag = false;

		if (forwardMovementHoverDetected(frame, interactionBox)) {
			if (!moveForwardFlag) {
				gestureFlag = true;
			}
			moveForwardFlag = true;
		} else {
			if (moveForwardFlag) {
				gestureFlag = true;
			}
			moveForwardFlag = false;
		}

		if (gestureFlag) {
			System.out.println("moveForwardFlag raised " + moveForwardFlag);
			if (moveForwardFlag) {
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_W);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					robot = new Robot();
					robot.keyRelease(KeyEvent.VK_W);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * pushes the s button if backwardMovementHoverDetected is true
	 *
	 * @param frame
	 * @param interactionBox
	 */
	private void handleBackward(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag = false;

		if (backwardMovementHoverDetected(frame, interactionBox)) {
			if (!moveBackwardFlag) {
				gestureFlag = true;
			}
			moveBackwardFlag = true;
		} else {
			if (moveBackwardFlag) {
				gestureFlag = true;
			}
			moveBackwardFlag = false;
		}

		if (gestureFlag) {
			System.out.println("moveBackwardFlag raised " + moveBackwardFlag);
			if (moveBackwardFlag) {
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_S);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					robot = new Robot();
					robot.keyRelease(KeyEvent.VK_S);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * pushes the a key if leftMovementHoverDetected is true
	 *
	 * @param frame
	 * @param interactionBox
	 */
	private void handleLeft(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag = false;

		if (leftMovementHoverDetected(frame, interactionBox)) {
			if (!moveLeftFlag) {
				gestureFlag = true;
			}
			moveLeftFlag = true;
		} else {
			if (moveLeftFlag) {
				gestureFlag = true;
			}
			moveLeftFlag = false;
		}

		if (gestureFlag) {
			System.out.println("moveLeftFlag raised " + moveLeftFlag);
			if (moveLeftFlag) {
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_A);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					robot = new Robot();
					robot.keyRelease(KeyEvent.VK_A);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * pushes the d key if rightMovementHoverDetected is true
	 *
	 * @param frame
	 * @param interactionBox
	 */
	private void handleRight(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		if (rightMovementHoverDetected(frame, interactionBox)) {
			if (!moveRightFlag) {
				gestureFlag = true;
			}
			moveRightFlag = true;
		} else {
			if (moveRightFlag) {
				gestureFlag = true;
			}
			moveRightFlag = false;
		}

		if (gestureFlag) {
			System.out.println("moveRightFlag raised " + moveRightFlag);
			if (moveRightFlag) {
				try {
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_D);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					robot = new Robot();
					robot.keyRelease(KeyEvent.VK_D);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}

	}


}
