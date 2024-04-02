package com.dev.factories;

import com.dev.entities.Position;

public class PositionFactory {

	public static Position createPosition() {
		Position position = new Position(1L, "UX Designer", 4250.00);
		return position;
	}
	
	public static Position createPosition(String newPosition) {
		Position position = createPosition();
		position.setPosition(newPosition);
		return position;
	}
}
