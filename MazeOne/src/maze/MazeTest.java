package maze;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class MazeTest {

	@Test
	public void testGetRoom() throws FileNotFoundException {
		Maze maze = new Maze();
		assertEquals(maze.getCurrentRoom(),maze.getRoom(0));
		String[] args = {"data/new.large.maze"};
		maze = new Maze(args);
		assertEquals(maze.getCurrentRoom(),maze.getRoom(0));
		
	}

	@Test
	public void testGetNumberOfRooms() throws FileNotFoundException {
		String[] args = {"data/new.large.maze"};
		Maze maze = new Maze(args);
		assertEquals(25,maze.getNumberOfRooms());
		String[] args2 = {"data/new.small.maze"};
		maze = new Maze(args2);
		assertEquals(3,maze.getNumberOfRooms());
	}


}
