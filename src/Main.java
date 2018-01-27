import com.leapmotion.leap.Controller;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		// Create a sample listener and controller
		GestureListener listener = new GestureListener();
		Controller controller = new Controller();

		// Have the sample listener receive events from the controller
		controller.addListener(listener);

		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		controller.removeListener(listener);
	}
}
