import com.leapmotion.leap.Controller;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		// Create a sample gestureListener and controller
		GestureListener gestureListener = new GestureListener();
		MovementListener movementListener = new MovementListener();
		LookListener lookListener = new LookListener();
		Controller controller = new Controller();

		// Have the listener receive events from the controller
		controller.addListener(gestureListener);
		controller.addListener(movementListener);
		controller.addListener(lookListener);

		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample gestureListener when done
		controller.removeListener(gestureListener);
		controller.removeListener(movementListener);
		controller.removeListener(lookListener);
	}
}
