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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class UserMap
{
	private int rows;
	private int columns;
	private Tile[][] elements;
	private Tile target;
	private String map[][];
		
	private static final String SPACE = "  ";
	private static final String PATH = "# ";
	private static final String TARGET = "X ";
	
	private List<Tile> opened = new ArrayList<Tile>();
	private List<Tile> closed = new ArrayList<Tile>();
	private List<Tile> bestList = new ArrayList<Tile>();
	
	public UserMap(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		elements = new Tile[rows][columns];
		
		try
		{
			ReadMapFile mapFile = new ReadMapFile("large_map.txt");
			try
			{
				map = mapFile.readMap();
			} 
			catch(Exception e)
			{
				System.out.println("Caught Exception in main");
			}
			finally
			{
				mapFile.dispose();//we are finished with the input map file
			}
		}
		catch(Exception e)
		{
			System.out.println("InputFile construction failed");
		}
		init();
	}
/*
 *This create a Tile object representing 
 *an x and y coordinates for each position
 *in the map
 *
 */
	private void init()
	{
		createTiles();
		setStartAndTarget();
		generateAdjacenies();//build map adjacency information
	}

	public int getRows()
	{
		return rows; //map rows
	}

	public int getColumns()
	{
		return columns; // map columns
	}

	private void setStartAndTarget()
	{
		elements[0][0].setStart(true);
		target = elements[rows - 1][columns - 1];
		target.setEnd(true);
	}

	private void generateAdjacenies()
	{
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				elements[i][j].calculateAdjacencies();
			}
		}
	}

	private void createTiles()
	{

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				elements[i][j] = new Tile(i, j, this);				
			}
		}
	}

	public Tile getTile(int x, int y)
	{
		return elements[x][y];
	}

	public void setTile(Tile tile)
	{
		elements[tile.getX()][tile.getY()] = tile;
	}

	public void draw()
	{
		System.out.println("Drawing userMap");
		drawContents();
	}

	private void drawContents()
	{
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				Tile tile = elements[i][j];
			}
			System.out.println("");

			for (int j = 0; j < columns; j++)
			{
				Tile tile = elements[i][j];
				drawMap(tile);
			}
			System.out.println("");
		}
	}
		
	private void drawMap(Tile tile)
	{
		int x = tile.getX();
		int y = tile.getY();
    	System.out.print(map[x][y] + " ");  
 		
		for (Tile neighbor : tile.getAdjacencies())
		{
			if (neighbor.getX() == x && neighbor.getY() == y - 1)
			{
				if (tile.isEnd())
				{
					System.out.print(TARGET);
					return;
				}
				if (bestList.contains(tile))
				{
					System.out.print(PATH);
					return;
				}
				System.out.print(SPACE);
				return;
			}
		}

		if (tile.isEnd())
		{
			System.out.print(TARGET);
			return;
		}

		if (bestList.contains(tile))
		{
			System.out.print(PATH);
			return;
		}
		System.out.print(SPACE);

	}

		public void findBestPath()
		{
		System.out.println("Calculating best path...");
		
		Set<Tile> adjacencies = elements[0][0].getAdjacencies();
		
		for (Tile adjacency : adjacencies)
		{
			adjacency.setParent(elements[0][0]);
			if (adjacency.isStart() == false)
			{
				opened.add(adjacency);				
			}
		}
		
		while (opened.size() > 0)
		{
			Tile best = findBestPassThrough();
			opened.remove(best);
			closed.add(best);
			if (best.isEnd())
			{
				System.out.println("Found TARGET!");
				populateBestList(target);
				draw();
				return;
			}
			
			else
			{
				Set<Tile> neighbors = best.getAdjacencies();
				for (Tile neighbor : neighbors)
				{
					if (opened.contains(neighbor))
					{
						Tile tmpTile = new Tile(neighbor.getX(),neighbor.getY(), this);
						tmpTile.setParent(best);
						if (tmpTile.getPassThrough(target) >= neighbor.getPassThrough(target))
						{
							continue;
						}
					}

					if (closed.contains(neighbor))
					{
						Tile tmpTile = new Tile(neighbor.getX(),neighbor.getY(), this);
						tmpTile.setParent(best);
						if (tmpTile.getPassThrough(target) >= neighbor.getPassThrough(target))
						{
							continue;
						}
					}
					neighbor.setParent(best);
					opened.remove(neighbor);
					closed.remove(neighbor);
					opened.add(0, neighbor);
				}
			}
		}

		System.out.println("No Path to target... PLEASE RUN AGAIN!");
	}

	private void populateBestList(Tile tile)
	{
		bestList.add(tile);
		if (tile.getParent().isStart() == false)
		{
			populateBestList(tile.getParent());
		}

		return;
	}

	private Tile findBestPassThrough()
	{
		Tile best = null;
		for (Tile tile : opened)
		{
			if (best == null || tile.getPassThrough(target) < best.getPassThrough(target))
			{
				best = tile;
			}
		}

		return best;
	}
}
