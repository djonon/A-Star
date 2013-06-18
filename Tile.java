/*    
 * A* algorithm implementation.
 * Copyright (C) 2013 Hippolyte Djonon <hdjonon@gmail.com>

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
                       
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>.
 */
import java.util.HashSet;
import java.util.Set;

public class Tile
{

	private int x;
	private int y;
	private boolean start;
	private boolean end;

	private double localCost; // cost of getting from this tile to goal
	private double parentCost; // cost of getting from parent tile to this node
	private double passThroughCost;// cost of getting from the start to the goal
	// through this tile

	private UserMap userMap;
	private Set<Tile> adjacencies = new HashSet<Tile>();

	private Tile parent;

	//constructor
	public Tile(int x, int y, UserMap userMap)
	{

		this.x = x;
		this.y = y;
		this.userMap = userMap;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	//Could be used to manually set the starting point
	//rather than using the input file 
	public boolean isStart()
	{
		return start;
	}

	public void setStart(boolean start)
	{
		this.start = start;
	}

	public boolean isEnd()
	{
		return end;
	}

	public void setEnd(boolean end)
	{
		this.end = end;
	}

	public Set<Tile> getAdjacencies()
	{

		return adjacencies;
	}

	public void setAdjacencies(Set<Tile> adjacencies)
	{
		this.adjacencies = adjacencies;
	}

	public Tile getParent()
	{
		return parent;
	}

	public void setParent(Tile parent)
	{
		this.parent = parent;
	}
/***************************************************************
 * In this method, each tile then randomly calculates if it has an 
 * adjacent tile to its' bottom and right. 
 * If an adjacency is found, then the tile adds 
 * a references to the adjacent tile to itself 
 * and adds itself as an adjacent tile to it's neighbor
*****************************************************************/
	public void calculateAdjacencies()
	{
		int top = x - 1;
		int bottom = x + 1;
		int left = y - 1;
		int right = y + 1;
		
		if (bottom < userMap.getRows())
		{
			if (isAdjacent())
			{
				userMap.getTile(bottom, y).addAdjacency(this);
				this.addAdjacency(userMap.getTile(bottom, y));
			}
		}

		if (right < userMap.getColumns())
		{
			if (isAdjacent())
			{
				userMap.getTile(x, right).addAdjacency(this);
				this.addAdjacency(userMap.getTile(x, right));
			}
		}
	}

	public void addAdjacency(Tile tile)
	{
		adjacencies.add(tile); //add adjacency
	}
	
	public void removeAdjacency(Tile tile)
	{
		adjacencies.remove(tile); //remove adjacency
	}
	
/********************************************
* Calculates the cost of getting from the 
* starting Tile to the target via this Tile
* Simply put: passThroughCost = localCost + parentCost
*********************************************/

	public double getPassThrough(Tile goal)
	{
		if (isStart())
		{
			return 0.0;
		}
		return getLocalCost(goal) + getParentCost();
	}

/********************************************
* Calculates the cost of getting from the 
* current Tile to the target
*********************************************/

	public double getLocalCost(Tile goal)
	{

		if (isStart()) {
			return 0.0;
		}
		/* Use the Manhattan distance heuristic */
		localCost = 1.0 * (Math.abs(x - goal.getX()) + Math.abs(y - goal.getY()));
		return localCost;
	}

/********************************************
* Calculates the cost of getting from the 
* parent Tile to the current one
*********************************************/
	public double getParentCost()
	{
		if (isStart())
		{
			return 0.0;
		}

		if (parentCost == 0.0)
		{
			/*The local cost defines the cost of moving along one axis, which we'll define as 1.0*/
			parentCost = 1.0 + .5 * (parent.getParentCost() - 1.0);
		}
		return parentCost;
	}
	
	public boolean isAdjacent()
	{

		if (Math.random() > .5)
		{
			return true;
		}
		return false;
	}
}