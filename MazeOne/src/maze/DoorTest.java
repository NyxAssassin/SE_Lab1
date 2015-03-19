package maze;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class DoorTest {
	private Room room1 = new Room(0);
	private Room room2 = new Room(1);
	private Door door = new Door(room1,room2);
	@Test
	public void testGetColor() {
		org.junit.Assert.assertEquals(Color.LIGHT_GRAY, door.getColor());
		door.setColor("ff0000");
		org.junit.Assert.assertEquals(Color.RED,door.getColor());
		door = new Door(room1,room2);
		org.junit.Assert.assertEquals(Color.LIGHT_GRAY, door.getColor());
	}

	@Test
	public void testIsOpen() {
		Door door2 = new Door(room1,room2);
		org.junit.Assert.assertEquals(false, door2.isOpen());
		door2.setOpen(false);
		org.junit.Assert.assertEquals(false, door2.isOpen());
		
	}

	@Test
	public void testGetOtherSide() {
		Door door3 = new Door(room1,room2);
		org.junit.Assert.assertEquals(door3.getOtherSide(room1),room2);
		org.junit.Assert.assertEquals(door3.getOtherSide(room2),room1);
	}

}
