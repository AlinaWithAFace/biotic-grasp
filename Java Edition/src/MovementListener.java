import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;
import java.awt.event.KeyEvent;

// TODO: Add crouching and jumping behaviors, probably using the y axis on the left hand

/**
 * Handles WASD movement using the left hand
 */
public class MovementListener extends Listener {
	
	static boolean moveForwardFlag;
	static boolean moveBackwardFlag;
	static boolean moveLeftFlag;
	static boolean moveRightFlag;
	static boolean jumpingFlag;
	static boolean crouchFlag;
	public Robot robot;
	
	private double zMid = .5;
	private double zPadding = .20;
	
	private double yMid = .5;
	private double yPadding = .20;
	
	private double xMid = .25;
	private double xPadding = .20;
	
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
		
		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					handleForward(hand, interactionBox);
					handleLeft(hand, interactionBox);
					handleBackward(hand, interactionBox);
					handleRight(hand, interactionBox);
					handleJump(hand, interactionBox);
					handleCrouch(hand, interactionBox);
				}
			}
		}
	}
	
	//region Detectors, they look at a given hand to see if the user is trying to perform an action
	//---------------------------------------------------------------------------------------
	
	/**
	 * detects if the palm is farther to you relative to the interaction box
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean forwardMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double zMin = zMid - zPadding;
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		return palmPosition.getZ() <= zMin;
	}
	
	/**
	 * detects if the palm is closeish to you relative to the interaction box
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean backwardMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double zMax = zMid + zPadding;
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		return palmPosition.getZ() >= zMax;
	}
	
	/**
	 * detects if the left hand is over in the leftmost range
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean leftMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double xMin = xMid - xPadding;
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		return palmPosition.getX() <= xMin;
	}
	
	/**
	 * detects if the left hand is over the right side of the left side (because that makes sense)
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean rightMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double xMax = xMid + xPadding;
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		return palmPosition.getX() >= xMax;
	}
	
	/**
	 * Check if a given hand is hovering relatively low in order to trigger crouching
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean crouchMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double yMin = yMid - yPadding;
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		return palmPosition.getY() <= yMin;
	}
	
	/**
	 * Check if the given hand is hovering relatively high in order to trigger a jump
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean jumpMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double yMax = yMid + yPadding;
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		return palmPosition.getY() >= yMax;
		
	}

//	/**
//	 * TODO
//	 * This is kind of broken, but I want it to ideally return whether or not the gesture has changed from before or not
//	 * and modify a given boolean that gets read for movement later on,
//	 * but turns out java doesn't like anything other than references
//	 *
//	 * @param gestureDetected
//	 * @param actionOccurring
//	 * @return
//	 */
//	private boolean detectGestureChange(boolean gestureDetected, boolean actionOccurring) {
//		boolean gestureChangedFlag = false;
//
//		if (gestureDetected) {
//			if (!actionOccurring) {
//				gestureChangedFlag = true;
//			}
//			actionOccurring = true;
//		} else {
//			if (actionOccurring) {
//				gestureChangedFlag = true;
//			}
//			actionOccurring = false;
//		}
//		return gestureChangedFlag;
//	}
	//---------------------------------------------------------------------------------------
	//endregion
	
	//region Handlers, they take the checks from the detectors and perform the corresponding operation based on it
	//---------------------------------------------------------------------------------------
	
	/**
	 * pushes the w key if forwardMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleForward(Hand hand, InteractionBox interactionBox) {
		boolean gestureFlag;
		//gestureFlag = detectGestureChange(forwardMovementHoverDetected(frame, interactionBox), MovementFlags.moveForwardFlag);
		
		gestureFlag = false;
		if (forwardMovementHoverDetected(hand, interactionBox)) {
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
			tryToPressAButton(moveForwardFlag, KeyEvent.VK_W);
		}
		
	}
	
	/**
	 * pushes the s button if backwardMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleBackward(Hand hand, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		
		if (backwardMovementHoverDetected(hand, interactionBox)) {
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
			tryToPressAButton(moveBackwardFlag, KeyEvent.VK_S);
		}
	}
	
	/**
	 * pushes the a key if leftMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleLeft(Hand hand, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		
		if (leftMovementHoverDetected(hand, interactionBox)) {
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
			tryToPressAButton(moveLeftFlag, KeyEvent.VK_A);
		}
	}
	
	/**
	 * pushes the d key if rightMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleRight(Hand hand, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		if (rightMovementHoverDetected(hand, interactionBox)) {
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
			tryToPressAButton(moveRightFlag, KeyEvent.VK_D);
		}
		
	}
	
	/**
	 * @param hand
	 * @param interactionBox
	 */
	private void handleCrouch(Hand hand, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		
		if (crouchMovementHoverDetected(hand, interactionBox)) {
			if (!crouchFlag) {
				gestureFlag = true;
			}
			crouchFlag = true;
		} else {
			if (crouchFlag) {
				gestureFlag = true;
			}
			crouchFlag = false;
		}
		
		if (gestureFlag) {
			tryToPressAButton(crouchFlag, KeyEvent.CTRL_DOWN_MASK);
		}
	}
	
	/**
	 * Hit the space key
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleJump(Hand hand, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		
		if (jumpMovementHoverDetected(hand, interactionBox)) {
			if (!jumpingFlag) {
				gestureFlag = true;
			}
			jumpingFlag = true;
		} else {
			if (jumpingFlag) {
				gestureFlag = true;
			}
			jumpingFlag = false;
		}
		
		if (gestureFlag) {
			tryToPressAButton(jumpingFlag, KeyEvent.VK_SPACE);
		}
	}
	
	/**
	 * Given booleans to read, try to push a button based on whether or not it's true or false.
	 * For example, if the gesture has changed from before and the related action key is true press the key,
	 * but if it's false, release the key
	 *
	 * @param performMovementFlag
	 * @param keyEventCode
	 */
	private void tryToPressAButton(boolean performMovementFlag, int keyEventCode) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		if (performMovementFlag) {
			robot.keyPress(keyEventCode);
		} else {
			robot.keyRelease(keyEventCode);
		}
	}
	//---------------------------------------------------------------------------------------
	//endregion
	
}