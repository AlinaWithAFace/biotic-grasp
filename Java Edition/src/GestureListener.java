import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * Handles various gestures using one or more hands
 */
public class GestureListener extends Listener {
	
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
		HandList hands = frame.hands();
		
		if (hands.count() >= 1) {
			handleBioticOrbGesture(hands);
			for (Hand hand : hands) {
				if (hand.isLeft()) {
					handleLeftBioticGraspGesture(hand);
					handleCoalescenceGesture(hand);
					handleFadeGesture(hand);
				} else if (hand.isRight()) {
					handleRightBioticGraspGesture(hand);
				}
			}
		}
	}
	
	private void handleMeleeGesture(Hand hand) {
		boolean gestureFlag = false;
		
		if (fistGestureDetected(hand)) {
			if (!movementFlags.meleeFlag) {
				gestureFlag = true;
			}
			movementFlags.meleeFlag = true;
		} else {
			if (movementFlags.meleeFlag) {
				gestureFlag = true;
			}
			movementFlags.meleeFlag = false;
		}
		
		if (gestureFlag) {
			Utilities.tryToTapAButton(movementFlags.meleeFlag, KeyEvent.VK_V);
		}
	}
	
	/**
	 * pressed q if coalescenceGesture is detected
	 *
	 * @param hand
	 */
	private void handleCoalescenceGesture(Hand hand) {
		boolean gestureFlag = false;
		
		if (coalescenceGestureDetected(hand)) {
			if (!movementFlags.coalescenceFlag) {
				gestureFlag = true;
			}
			movementFlags.coalescenceFlag = true;
		} else {
			if (movementFlags.coalescenceFlag) {
				gestureFlag = true;
			}
			movementFlags.coalescenceFlag = false;
		}
		
		if (gestureFlag) {
			Utilities.tryToTapAButton(movementFlags.coalescenceFlag, KeyEvent.VK_Q);
		}
	}
	
	private void handleFadeGesture(Hand hand) {
		boolean gestureFlag = false;
		
		if (fistGestureDetected(hand)) {
			if (!movementFlags.fadeFlag) {
				gestureFlag = true;
			}
			movementFlags.fadeFlag = true;
		} else {
			if (movementFlags.fadeFlag) {
				gestureFlag = true;
			}
			movementFlags.fadeFlag = false;
		}
		
		if (gestureFlag) {
			Utilities.tryToTapAButton(movementFlags.fadeFlag, KeyEvent.VK_SHIFT);
		}
	}
	
	/**
	 * presses the E button if BioticOrbGesture is detected
	 *
	 * @param hands
	 */
	private void handleBioticOrbGesture(HandList hands) {
		boolean gestureFlag = false;
		
		if (bioticOrbGestureDetected(hands)) {
			if (!movementFlags.bioticOrbFlag) {
				gestureFlag = true;
			}
			movementFlags.bioticOrbFlag = true;
		} else {
			if (movementFlags.bioticOrbFlag) {
				gestureFlag = true;
			}
			movementFlags.bioticOrbFlag = false;
		}
		
		if (gestureFlag) {
			Utilities.tryToTapAButton(movementFlags.bioticOrbFlag, KeyEvent.VK_E);
		}
	}
	
	/**
	 * Hits the left mouse button if the gesture is detected
	 *
	 * @param hand
	 */
	private void handleLeftBioticGraspGesture(Hand hand) {
		boolean gestureFlag = false;
		
		if (bioticGraspGestureDetected(hand)) {
			if (!movementFlags.leftBioticGraspFlag) {
				gestureFlag = true;
			}
			movementFlags.leftBioticGraspFlag = true;
		} else {
			if (movementFlags.leftBioticGraspFlag) {
				gestureFlag = true;
			}
			movementFlags.leftBioticGraspFlag = false;
		}
		
		if (gestureFlag) {
			Utilities.tryToPressAButton(movementFlags.leftBioticGraspFlag, InputEvent.BUTTON1_DOWN_MASK);
		}
	}
	
	/**
	 * Hits the right mouse button if the gesture is detected
	 *
	 * @param hand
	 */
	private void handleRightBioticGraspGesture(Hand hand) {
		boolean gestureFlag = false;
		
		if (bioticGraspGestureDetected(hand)) {
			if (!movementFlags.rightBioticGraspFlag) {
				gestureFlag = true;
			}
			movementFlags.rightBioticGraspFlag = true;
		} else {
			if (movementFlags.rightBioticGraspFlag) {
				gestureFlag = true;
			}
			movementFlags.rightBioticGraspFlag = false;
		}
		
		if (gestureFlag) {
			Utilities.tryToPressAButton(movementFlags.rightBioticGraspFlag, InputEvent.BUTTON3_MASK);
		}
	}
	
	/**
	 * detects if 5 fingers on the given hand are pointing up
	 *
	 * @param hand
	 * @return
	 */
	private boolean bioticGraspGestureDetected(Hand hand) {
		int fingerUpCount = 0;
		for (Finger finger : hand.fingers()) {
			Vector pointDirection = finger.direction();
			if (pointDirection.getY() > fingerPointingUpNum) {
				fingerUpCount++;
			}
		}
		return fingerUpCount >= fingerNum;
	}
	
	/**
	 * detects whether all fingers of both hands are forward (think a T-Rex)
	 *
	 * @param hands
	 * @return
	 */
	private boolean bioticOrbGestureDetected(HandList hands) {
		double bioticOrbFingerDirection = -.8;
		int bioticOrbFingerCount = 8;
		
		if (hands.count() >= 2) {
			int fingerUpCount = 0;
			for (Hand hand : hands) {
				for (Finger finger : hand.fingers()) {
					if (finger.type() != Finger.Type.TYPE_THUMB) {
						if (finger.direction().getZ() <= bioticOrbFingerDirection) {
							fingerUpCount++;
						}
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
	 * detects whether the left hand's 4 fingers are up and the thumb is to the right
	 *
	 * @param hand
	 * @return
	 */
	private boolean coalescenceGestureDetected(Hand hand) {
		double coalescenceFingerDirection = .8;
		int coalescenceFingerCount = 4;
		boolean thumbPass = false;
		
		int fingerUpCount = 0;
		for (Finger finger : hand.fingers()) {
			Vector pointDirection = finger.direction();
			
			if (finger.type() != Finger.Type.TYPE_THUMB) {
				if (pointDirection.getY() > coalescenceFingerDirection) {
					fingerUpCount++;
				}
			} else if (finger.type() == Finger.Type.TYPE_THUMB) {
				//System.out.println(finger.type() + " pointDirection" + pointDirection.toString());
				if (pointDirection.getX() > coalescenceFingerDirection) {
					thumbPass = true;
				}
			}
		}
		
		return fingerUpCount >= coalescenceFingerCount && thumbPass;
	}
	
	/**
	 * Returns true if the given hand has a "strong grip", i.e., is balled into a fist
	 *
	 * @param hand
	 * @return
	 */
	private boolean fistGestureDetected(Hand hand) {
		double gripThreshold = 1;
		return hand.grabStrength() >= gripThreshold;
	}
}

