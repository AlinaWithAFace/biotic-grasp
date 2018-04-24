import java.awt.*;
import java.awt.event.KeyEvent;

public class Utilities {
	/**
	 * Returns whether or not the gesture has changed from the last time it was looked at,
	 * and changes the given ActionFlag's flag accordingly.
	 * <p>
	 * For example, if the gesture for walk forward is detected, and the moveForward flag is false,
	 * you're not already walking forward, so change the flag to true and change the fact you changed the flag to true,
	 * but if the gesture for walk forward is detected and moveForward is true, you're already moving forward,
	 * so you can pretty much leave everything else as-is
	 *
	 * @param gestureDetected
	 * @param actionFlag
	 * @return
	 */
	static boolean detectGestureChange(boolean gestureDetected, ActionFlag actionFlag) {
		boolean gestureChangedFlag = false;
		
		if (gestureDetected) {
			if (!actionFlag.flag) {
				gestureChangedFlag = true;
			}
			actionFlag.flag = true;
		} else {
			if (actionFlag.flag) {
				gestureChangedFlag = true;
			}
			actionFlag.flag = false;
		}
		return gestureChangedFlag;
	}
	
	/**
	 * Given a boolean, try to push a button based on whether said boolean is true or false.
	 * If the bool is true, try to push the button, if it's false, try to release the button
	 *
	 * @param actionFlag
	 * @param keyEventCode
	 */
	static void tryToPressAButton(ActionFlag actionFlag, int keyEventCode) {
		Robot robot;
		try {
			robot = new Robot();
			if (actionFlag.flag) {
				System.out.println("Press Key " + keyEventCode);
				robot.keyPress(keyEventCode);
			} else {
				System.out.println("Release Key " + keyEventCode);
				robot.keyRelease(keyEventCode);
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Similar to tryToPressAButton, but rather than potentially pressing or releasing it,
	 * this simply presses and releases the key if the related boolean is positive and does nothing if it's negative
	 *
	 * @param actionFlag
	 * @param keyEventCode
	 */
	static void tryToTapAButton(ActionFlag actionFlag, int keyEventCode) {
		Robot robot;
		try {
			robot = new Robot();
			if (actionFlag.flag) {
				System.out.println("Tap " + keyEventCode);
				robot.keyPress(keyEventCode);
				robot.keyRelease(keyEventCode);
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	static void tryToMouse(ActionFlag actionFlag, int mouseEventCode) {
		Robot robot;
		try {
			robot = new Robot();
			if (actionFlag.flag) {
				robot.mousePress(mouseEventCode);
			} else {
				robot.mouseRelease(mouseEventCode);
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
