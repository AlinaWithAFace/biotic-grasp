import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MovementListener extends Listener {

	private boolean moveForwardFlag;
	private boolean moveBackwardFlag;
	private boolean moveLeftFlag;
	private boolean moveRightFlag;

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

	}

	/**
	 * detects if the palm is farther to you relative to the interaction box
	 *
	 * @param frame
	 * @param interactionBox
	 * @return
	 */
	private boolean forwardMovementHoverDetected(Frame frame, InteractionBox interactionBox) {
		double zMin = .25;

		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					Vector normalizedPalmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());
					//System.out.println(normalizedPalmPosition);

					if (normalizedPalmPosition.getZ() <= zMin) {
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
		double zMax = .75;

		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isLeft()) {
					Vector normalizedPalmPosition = interactionBox.normalizePoint(hand.stabilizedPalmPosition());

					if (normalizedPalmPosition.getZ() >= zMax) {
						//System.out.println(" move backward maybe");
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
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_W);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Robot robot = new Robot();
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
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_S);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Robot robot = new Robot();
					robot.keyRelease(KeyEvent.VK_S);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
