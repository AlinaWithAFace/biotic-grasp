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
	
	private double leftHandZAxisMidPoint = .5;
	private double leftHandXAxisMidPoint = .25;
	private double leftHandXZAxisPadding = .20;
	
	public void onInit(Controller controller) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
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
		double zMin = leftHandZAxisMidPoint - leftHandXZAxisPadding;
		
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		//System.out.println(palmPosition);
		
		if (palmPosition.getZ() <= zMin) {
			//System.out.println(" move forward maybe");
			return true;
		}
		
		return false;
	}
	
	/**
	 * detects if the palm is closeish to you relative to the interaction box
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean backwardMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double zMax = leftHandZAxisMidPoint + leftHandXZAxisPadding;
		
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		if (palmPosition.getZ() >= zMax) {
			//System.out.println(" move backward maybe");
			return true;
		}
		return false;
	}
	
	/**
	 * detects if the left hand is over in the leftmost range
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean leftMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double xBoundary = leftHandXAxisMidPoint - leftHandXZAxisPadding;
		
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		//System.out.println(palmPosition.getX());
		if (palmPosition.getX() <= xBoundary) {
			//System.out.println("move left maybe");
			return true;
		}
		return false;
	}
	
	/**
	 * detects if the left hand is over the right side of the left side (because that makes sense)
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean rightMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		double xBoundary = leftHandXAxisMidPoint + leftHandXZAxisPadding;
		
		Vector palmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
		if (palmPosition.getX() >= xBoundary) {
			//System.out.println("move right maybe");
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the given hand is hovering relatively high in order to trigger a jump
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean jumpMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		//todo
		return false;
	}
	
	/**
	 * Check if a given hand is hovering relatively low in order to trigger crouching
	 *
	 * @param hand
	 * @param interactionBox
	 * @return
	 */
	private boolean crouchMovementHoverDetected(Hand hand, InteractionBox interactionBox) {
		//todo
		return false;
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
		
		tryToPressAButton(gestureFlag && moveForwardFlag, KeyEvent.VK_W);
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
		
		tryToPressAButton(gestureFlag && moveBackwardFlag, KeyEvent.VK_S);
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
		
		tryToPressAButton(gestureFlag && moveLeftFlag, KeyEvent.VK_A);
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
		
		tryToPressAButton(gestureFlag && moveRightFlag, KeyEvent.VK_D);
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
		
		tryToPressAButton(gestureFlag && jumpingFlag, KeyEvent.VK_SPACE);
		//TODO: test
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
		
		tryToPressAButton(gestureFlag && crouchFlag, KeyEvent.CTRL_DOWN_MASK);
		//TODO: test
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
		if (performMovementFlag) {
			robot.keyPress(keyEventCode);
		} else {
			robot.keyRelease(keyEventCode);
		}
	}
	//---------------------------------------------------------------------------------------
	//endregion
	
}