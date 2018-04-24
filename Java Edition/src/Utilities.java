import java.awt.*;

public class Utilities {
	/**
	 * Given a boolean, try to push a button based on whether said boolean is true or false.
	 * If the bool is true, try to push the button, if it's false, try to release the button
	 *
	 * @param movementFlag
	 * @param keyEventCode
	 */
	static void tryToPressAButton(boolean movementFlag, int keyEventCode) {
		Robot robot;
		try {
			robot = new Robot();
			if (movementFlag) {
				robot.keyPress(keyEventCode);
			} else {
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
	 * @param movementFlag
	 * @param keyEventCode
	 */
	static void tryToTapAButton(boolean movementFlag, int keyEventCode) {
		Robot robot;
		try {
			robot = new Robot();
			if (movementFlag) {
				robot.keyPress(keyEventCode);
				robot.keyRelease(keyEventCode);
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
