/*
 * Maze.java
 * Copyright (c) 2008, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class Maze implements Iterable<Room>
{
	/*
	 * Well,since we may have non-numeric names for each room,now we have to change this DS to accommodate for the change.
	 */
	public final Map<Integer, Room> rooms = new HashMap<Integer, Room>();
	public final Map<String, Door> doors = new HashMap<String, Door>();
	/*
	 * The following two DSs are set in order to map a room/door name back to an ID.
	 * I didn't even mean to use this stupid method until I found out that...
	 * The MazeViewer class invokes methods in Maze class implicitly making we HAVE TO write still use a mapping from an
	 * integer to a room and makes some f*cking remedies like below.
	 * Screw Sunny for what he/she has created.
	 */
	public final Map<String, Integer> roomname2id = new HashMap<String, Integer>();
	public Room current;
	
	public Maze()
	{
		
		Room r1 = new Room(0);
		Room r2 = new Room(1);
		Door theDoor = new Door(r1, r2);
		/*
		 * Oh if we change the visibility of class variables we must replace theses statementes
		 * with getter and setter.
		 */
		this.rooms.put(r1.getNumber(), r1);
		this.rooms.put(r2.getNumber(), r2);
	
		r1.setside(Direction.North.ordinal(), new Wall());
		r1.setside(Direction.East.ordinal(), theDoor);
		r1.setside(Direction.South.ordinal(), new Wall());
		r1.setside(Direction.West.ordinal(), new Wall());
		r2.setside(Direction.North.ordinal(), new Wall());
		r2.setside(Direction.East.ordinal(), new Wall());
		r2.setside(Direction.South.ordinal(), new Wall());
		r2.setside(Direction.West.ordinal(), theDoor);
		this.current = this.rooms.get(0);
		
	}
    public Maze(String[] args) throws FileNotFoundException{
		/*
		 * Since now we have new arguments(optional),we must handle them.
		 */
    	String wallArgs = "",doorArgs = "",roomArgs = "";
    	Scanner scanner = null;
    	if (args.length==1){
    		scanner = new Scanner(new File(args[0]));
    	}
    	else {
    		int len = args.length;
    		for (int ind = 0;ind < len;ind++){
    			if (args[ind].indexOf("wall")>=0) wallArgs = args[ind].substring(args[ind].lastIndexOf('=')+1);
    			if (args[ind].indexOf("door")>=0) doorArgs = args[ind].substring(args[ind].lastIndexOf('=')+1);
    			if (args[ind].indexOf("room")>=0) roomArgs = args[ind].substring(args[ind].lastIndexOf('=')+1);
    			if (args[ind].endsWith("maze")) scanner = new Scanner(new File(args[ind]));
    		}
    	}
		String line;
		LinkedList<String[]> roomConfs = new LinkedList<>();

		int roomCount = 0; 
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] variables = line.split(" ");
			if (variables[0].equals("room")) {
				roomConfs.add(variables);
				/*
				 *  I finally added a constructor in which color is specified.
				 */
				Room room = roomArgs.equals("")? 
						new Room(roomCount)
						:
						new Room(roomCount,roomArgs);
				roomCount++;
				
				this.rooms.put(room.getNumber(), room);
				/*
				 * I once omitted this line.And then...I got stuck for a whole day.
				 */
				this.roomname2id.put(variables[1], room.getNumber());

			}else if (variables[0].equals("door")) {
				Door door = doorArgs.equals("")? 
						new Door(rooms.get(this.roomname2id.get(variables[2])), rooms.get(this.roomname2id.get(variables[3])))
						:
						new Door(rooms.get(this.roomname2id.get(variables[2])), rooms.get(this.roomname2id.get(variables[3])),doorArgs);
				door.setOpen(variables[4].equals("open"));
				doors.put(variables[1], door);
			}
		}
		int number = rooms.size();
		Direction[] directions = {Direction.North, Direction.South, Direction.East, 
				Direction.West};
		for (int roomId = 0; roomId < number; roomId++) {
			Room room = rooms.get(roomId);
			String[] roomConf = roomConfs.get(roomId);
			/*
			 *  Is it really OK writing like confId = 2;confId < 6...or so? 
			 */
			for(int confId = 2; confId < 6; confId ++){
				String conf = roomConf[confId];
				MapSite mapSite = null;
				/*
				 * Oh shit.If we allow map designers to name their rooms and doors freely the original judgment
				 * like conf.startsWith("d") will not work any more!
				 * WTF is this shit.
				 * Furthermore how can this crap be handled if we use the same name for a door and a room?
				 * This is gonna be a WTF moment in our SE labs.
				 */
				if(conf.equals("wall")){
					mapSite = wallArgs.equals("")? new Wall():new Wall(wallArgs);
				}else if (doors.get(conf)!=null) {
					mapSite = doors.get(conf);
				}else {
					mapSite = rooms.get(this.roomname2id.get(conf));
				}
				/*
				 * Oh BTW,I haven't dealt with the case in which we designate a room/door name that never exists.
				 */
				room.setside(directions[confId - 2].ordinal(),mapSite);
			}
		}
		scanner.close();
		this.current = this.rooms.get(0);
    
    }
	public final Room getRoom(int number)
	{
		return rooms.get(number);
	}
	
	@Override
	public Iterator<Room> iterator()
	{
		return rooms.values().iterator();
	}

	public int getNumberOfRooms()
	{
		return rooms.size();
	}

	public final Room getCurrentRoom()
	{
		return current;
	}

	public final void setCurrentRoom(final Room room)
	{
		current = room;
	}
}
