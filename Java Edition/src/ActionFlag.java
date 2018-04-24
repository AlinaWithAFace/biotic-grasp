/**
 * The various actions that can be performed,
 * along with a flag to tell whether or not it should be happening right now
 */
public enum ActionFlag {
	MOVE_FORWARD,
	MOVE_BACKWARD,
	MOVE_LEFT,
	MOVE_RIGHT,
	JUMP,
	CROUCH,
	RIGHT_BIOTIC_GRASP,
	LEFT_BIOTIC_GRASP,
	BIOTIC_ORB,
	COALESCENCE,
	FADE,
	MELEE,
	GREETINGS,
	THANKS;
	boolean flag;
}
