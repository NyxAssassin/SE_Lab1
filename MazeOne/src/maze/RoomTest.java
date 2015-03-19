package maze;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class RoomTest {

	@Test
	public void testGetColor() {
		Room room = new Room(1);
		assertEquals(Color.WHITE,room.getColor());
		room.setColor("00ff00");
		assertEquals(Color.GREEN,room.getColor());
	}

	@Test
	public void testGetSide() {
		Room room2 = new Room(18);
		Wall d = new Wall();
		room2.setside(0, d);
		room2.setside(1, d);
		room2.setside(2, d);
		room2.setside(3, d);
		assertEquals(d,room2.getSide(Direction.North));
		
	}

	@Test
	public void testGetNumber() {
		Room room3 = new Room(1);
		assertEquals(1,room3.getNumber());
	}

}
