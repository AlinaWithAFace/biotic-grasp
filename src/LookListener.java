import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;

import java.awt.*;

public class LookListener extends Listener {

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

		handleLookLeft(frame);
		handleLookRight(frame);
	}

	private int mouseMovementAddition = 1;

	private void handleLookRight(Frame frame) {
		if (lookRightDetected(frame)) {
			System.out.println("lookRightDetected");
			try {
				Robot robot = new Robot();
				Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(currentMouseLocation.x + mouseMovementAddition, currentMouseLocation.y);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleLookLeft(Frame frame) {
		if (lookLeftDetected(frame)) {
			System.out.println("lookLeftDetected");
			try {
				Robot robot = new Robot();
				Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
				robot.mouseMove(currentMouseLocation.x - mouseMovementAddition, currentMouseLocation.y);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	private double zBound = -.8;

	private boolean lookLeftDetected(Frame frame) {
		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isRight()) {
					if (hand.direction().getZ() >= zBound) {
						if (hand.direction().getX() < 0) {
							//System.out.println("look left?");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean lookRightDetected(Frame frame) {
		if (frame.hands().count() >= 1) {
			for (Hand hand : frame.hands()) {
				if (hand.isRight()) {
					if (hand.direction().getZ() <= zBound) {
						if (hand.direction().getX() > 0) {
							//System.out.println("look right?");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
