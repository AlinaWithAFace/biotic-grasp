import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * Handles various gestures using one or more hands
 */
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
		HandList hands = frame.hands();
		
		if (hands.count() >= 1) {
			handleBioticOrbGesture(hands);
			for (Hand hand : hands) {
				if (hand.isLeft()) {
					handleLeftBioticGraspGesture(hand);
					handleCoalescenceGesture(hand);
				} else if (hand.isRight()) {
					handleRightBioticGraspGesture(hand);
				}
			}
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
			Utilities.tryToTapAButton(coalescenceFlag, KeyEvent.VK_Q);
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
			Utilities.tryToTapAButton(bioticOrbFlag, KeyEvent.VK_E);
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
			Utilities.tryToPressAButton(leftBioticGraspFlag, InputEvent.BUTTON1_DOWN_MASK);
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
			Utilities.tryToPressAButton(rightBioticGraspFlag, InputEvent.BUTTON3_MASK);
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
				//System.out.println("hand");
				for (Finger finger : hand.fingers()) {
					if (finger.type() != Finger.Type.TYPE_THUMB) {
						Vector pointDirection = finger.direction();
						//System.out.println("bioticOrbGestureDetected " + pointDirection.getZ());
						
						if (pointDirection.getZ() <= bioticOrbFingerDirection) {
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
	
	private boolean fadeGestureDetected(Frame frame) {
		//todo, use grab_strength
		return false;
	}
}

