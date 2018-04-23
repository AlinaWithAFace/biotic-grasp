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
		
		handleForward(frame, interactionBox);
		handleBackward(frame, interactionBox);
		handleRight(frame, interactionBox);
		handleLeft(frame, interactionBox);
	}
	
	/**
	 * Given booleans to read, try to push a button based on whether or not it's true or false.
	 * For example, if the gesture has changed from before and the related action key is true press the key,
	 * but if it's false, release the key
	 *
	 * @param gestureChangedFlag
	 * @param performMovementFlag
	 * @param keyEventCode
	 */
	private void tryToPressAButton(boolean gestureChangedFlag, boolean performMovementFlag, int keyEventCode) {
		if (gestureChangedFlag) {
			if (performMovementFlag) {
				robot.keyPress(keyEventCode);
			} else {
				robot.keyRelease(keyEventCode);
			}
		}
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
	
	private boolean jumpMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		//todo
		return false;
	}
	
	private boolean crouchMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		//todo
		return false;
	}
	
	
	/**
	 * pushes the w key if forwardMovementHoverDetected is true
	 *
	 * @param frame
	 * @param interactionBox
	 */
	private void handleForward(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag;
		//gestureFlag = detectGestureChange(forwardMovementHoverDetected(frame, interactionBox), MovementFlags.moveForwardFlag);
		
		gestureFlag = false;
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
		
		tryToPressAButton(gestureFlag, moveForwardFlag, KeyEvent.VK_W);
	}
	
	/**
	 *
	 * @param frame
	 * @param interactionBox
	 */
	private void handleCrouch(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		
		if (crouchMovementHoverDetected(frame, interactionBox)) {
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
		
		tryToPressAButton(gestureFlag, crouchFlag, KeyEvent.CTRL_DOWN_MASK);
		//TODO: test
	}
	
	private void handleJump(Frame frame, InteractionBox interactionBox) {
		boolean gestureFlag = false;
		
		if (jumpMovementHoverDetected(frame, interactionBox)) {
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
		
		tryToPressAButton(gestureFlag, jumpingFlag, KeyEvent.VK_SPACE);
		//TODO: test
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
		
		tryToPressAButton(gestureFlag, moveBackwardFlag, KeyEvent.VK_S);
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
		
		tryToPressAButton(gestureFlag, moveLeftFlag, KeyEvent.VK_A);
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
		
		tryToPressAButton(gestureFlag, moveRightFlag, KeyEvent.VK_D);
	}
}


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
//
//	}