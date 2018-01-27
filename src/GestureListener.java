import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GestureListener extends Listener {
	private boolean rightHandOpen;

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
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();
//		System.out.println("Frame id: " + frame.id()
//				+ ", timestamp: " + frame.timestamp()
//				+ ", hands: " + frame.hands().count()
//				+ ", fingers: " + frame.fingers().count());

		boolean rightHandChanged = false;

		if (tapGestureDetected(frame)) {
			if (!rightHandOpen) {
				rightHandChanged = true;
			}
			rightHandOpen = true;

			//System.out.println("Found gesture!");
		} else {
			if (rightHandOpen) {
				rightHandChanged = true;
			}
			rightHandOpen = false;
		}


		if (rightHandChanged) {
			System.out.println("flag raised: " + rightHandOpen);

			if (rightHandOpen) {
				try {
					Robot robot = new Robot();

					//robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.keyPress(KeyEvent.VK_Q);
				} catch (AWTException e) {
					e.printStackTrace();
				}
			}

			rightHandChanged = false;
		}


		//Get hands
		for (Hand hand : frame.hands()) {
			String handType = hand.isLeft() ? "Left hand" : "Right hand";
//			System.out.println("  " + handType + ", id: " + hand.id()
//					+ ", palm position: " + hand.palmPosition());

			// Get the hand's normal vector and direction
			Vector normal = hand.palmNormal();
			Vector direction = hand.direction();

			// Calculate the hand's pitch, roll, and yaw angles
//			System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
//					+ "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
//					+ "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

			// Get arm bone
			Arm arm = hand.arm();
//			System.out.println("  Arm direction: " + arm.direction()
//					+ ", wrist position: " + arm.wristPosition()
//					+ ", elbow position: " + arm.elbowPosition());

			// Get fingers
			for (Finger finger : hand.fingers()) {
//				System.out.println("    " + finger.type() + ", id: " + finger.id()
//						+ ", length: " + finger.length()
//						+ "mm, width: " + finger.width() + "mm");

				//Get Bones
				for (Bone.Type boneType : Bone.Type.values()) {
					Bone bone = finger.bone(boneType);
//					System.out.println("      " + bone.type()
//							+ " bone, start: " + bone.prevJoint()
//							+ ", end: " + bone.nextJoint()
//							+ ", direction: " + bone.direction());
				}
			}
		}

		if (!frame.hands().isEmpty()) {
			//System.out.println();
		}
	}

	private boolean tapGestureDetected(Frame frame) {

		if ((frame.hands().count() > 0)) {
			for (Hand hand : frame.hands()) {

				int fingerCount = 0;
				for (Finger finger : frame.fingers()) {
					Vector pointingToward = finger.direction();
					if (pointingToward.getY() > .5) {
						//System.out.println("finger pointing up?");
						fingerCount++;
					}

				}
				if (hand.isLeft()) {
					if (fingerCount >= 5) {
						//System.out.println("All 5 fingers pointing up : left hand");
					} else {
						//System.out.println(" ");
					}
				}
				if (hand.isRight()) {
					if (fingerCount >= 5) {
						//System.out.println("All 5 fingers pointing up : right hand");
						return true;
					} else {
						//System.out.println(" ");
					}
				}


			}
		} else {
			return false;
		}
		return false;


	}


}

