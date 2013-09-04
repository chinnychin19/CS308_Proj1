Game, Part 1: Project Plan (updated gameplay after implementation)
By Chinmay Patwardhan (chinnychin19)


Name of the Game: Stairway to Heaven
Origin: I first thought of popular song titles and tried to design games that fit to the same title.
I think this title is self explanatory if you read the goal and mechanics below.

Genre: 2-D side scrolling platformer

Goal: Hell is chasing you! Run to the base of heaven before it catches you!
Once at the base, the user builds a stairway to heaven with the items he/she picked up along the way.
The user wins if they reach heaven, and loses otherwise -- whether by falling, being captured by hell, or being hit by a fireball.

Mechanics: The user will be able to move right, move left, and jump (gravity is present).
Gravity only affects the user if he/she jumps. If he/she walk off a platform, he/she will fall but not at an accelerating rate.
The user may jump exactly once at a time. To regain the jump, the user must touch a platform.
(Note that this means, that a user that walks off a platform may still jump off of thin air. This helps prevent quick deaths.)
A final physics note: the user may jump through a platform from beneath it. If this happens he/she will snap to the upper surface of the platform.

The games is broken into two stages as follows:
	Stage 1:
		The user starts at the center of the screen and "darkness" starts at the far left.
		Darkness slowly moves toward the right and the user should move right to keep ahead of the darkness.
		The user may move more quickly than the darkness, but if the user gets too far ahead, darkness will hug the left side of the screen.
		If darkness catches the user, he/she will lose a life.
		The user also has to get around obstacles:
			- Jumping from platform to platform (the heights vary, and falling through the bottom of the screen will result in a lost life)
			- Fireballs falling from above (collision will result in a lost life)
		The user must also attempt to pick up as many "items" as possible along the way.
		When the user reaches the far right of stage 1, he/she will get to use all the items he/she picked up in the current life.
	Stage 2:
		The second stage is not a side scroller. It is composed of two phases: a build phase and an action phase.
		During the build phase, the user gets to turn their items into platforms.
		The user starts off in the bottom left and heaven is in the top right. The goal is to reach heaven.
		With 15-20 items, the user should easily be able to build a traversible stairway.
		After the build phase, the user jumps from platform to platform to reach heaven.
		This time, the platforms begin to fall as soon as the user steps on them!
		If the user does not reach heaven they lose. (It doesn't matter if they had extra lives left over from stage 1.)
		If the user reaches heaven, they win.

Cheat Codes (only applicable in Stage 1):
	1) Shift + B: This sends the user straight to the build phase of stage 2 with 20 items.
	2) Shift + F: This turns off fireballs.
	3) Shift + D: This eliminates darkness.
		
End of file.