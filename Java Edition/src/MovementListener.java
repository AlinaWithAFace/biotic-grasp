import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.event.KeyEvent;

/**
 * Handles WASD movement using the left hand
 */
public class MovementListener extends Listener {
	
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
		boolean gestureOccurring = forwardMovementHoverDetected(hand, interactionBox);
		boolean gestureChange = Utilities.detectGestureChange(gestureOccurring, ActionFlag.MOVE_FORWARD);
		if (gestureChange) {
			Utilities.tryToPressAButton(ActionFlag.MOVE_FORWARD, KeyEvent.VK_W);
		}
	}
	
	/**
	 * pushes the s button if backwardMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleBackward(Hand hand, InteractionBox interactionBox) {
		boolean gestureOccurring = backwardMovementHoverDetected(hand, interactionBox);
		boolean gestureFlag = Utilities.detectGestureChange(gestureOccurring, ActionFlag.MOVE_BACKWARD);
		if (gestureFlag) {
			Utilities.tryToPressAButton(ActionFlag.MOVE_BACKWARD, KeyEvent.VK_S);
		}
	}
	
	/**
	 * pushes the a key if leftMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleLeft(Hand hand, InteractionBox interactionBox) {
		boolean gestureOccurring = leftMovementHoverDetected(hand, interactionBox);
		boolean gestureFlag = Utilities.detectGestureChange(gestureOccurring, ActionFlag.MOVE_LEFT);
		if (gestureFlag) {
			Utilities.tryToPressAButton(ActionFlag.MOVE_LEFT, KeyEvent.VK_A);
		}
	}
	
	/**
	 * pushes the d key if rightMovementHoverDetected is true
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleRight(Hand hand, InteractionBox interactionBox) {
		boolean gestureOccurring = rightMovementHoverDetected(hand, interactionBox);
		boolean gestureFlag = Utilities.detectGestureChange(gestureOccurring, ActionFlag.MOVE_RIGHT);
		if (gestureFlag) {
			Utilities.tryToPressAButton(ActionFlag.MOVE_RIGHT, KeyEvent.VK_D);
		}
	}
	
	/**
	 * @param hand
	 * @param interactionBox
	 */
	private void handleCrouch(Hand hand, InteractionBox interactionBox) {
		boolean gestureOccurring = crouchMovementHoverDetected(hand, interactionBox);
		boolean gestureFlag = Utilities.detectGestureChange(gestureOccurring, ActionFlag.CROUCH);
		if (gestureFlag) {
			Utilities.tryToPressAButton(ActionFlag.CROUCH, KeyEvent.CTRL_DOWN_MASK);
		}
	}
	
	/**
	 * Hit the space key
	 *
	 * @param hand
	 * @param interactionBox
	 */
	private void handleJump(Hand hand, InteractionBox interactionBox) {
		boolean gestureOccurring = jumpMovementHoverDetected(hand, interactionBox);
		boolean gestureFlag = Utilities.detectGestureChange(gestureOccurring, ActionFlag.JUMP);
		if (gestureFlag) {
			Utilities.tryToPressAButton(ActionFlag.JUMP, KeyEvent.VK_SPACE);
		}
	}
	
	//---------------------------------------------------------------------------------------
	//endregion
	
}