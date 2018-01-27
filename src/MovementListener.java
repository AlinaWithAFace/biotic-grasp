import com.leapmotion.leap.Frame;

public class MovementListener {

	private boolean moveForwardFlag;
	private boolean moveBackwardFlag;
	private boolean moveLeftFlag;
	private boolean moveRightFlag;


	private boolean movementGestureDetected(Frame frame) {
		//todo
		return false;
	}

	/**
	 * @param frame
	 */
	private void handleForward(Frame frame) {
		boolean gestureFlag = false;

		if (movementGestureDetected(frame)) {
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
		}
	}

}
