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
	private Square[][] elements;
	private Square target;
	private String map[][];
		
	private static final String SPACE = "  ";
	private static final String PATH = "# ";
	private static final String TARGET = "X ";
	
	private List<Square> opened = new ArrayList<Square>();
	private List<Square> closed = new ArrayList<Square>();
	private List<Square> bestList = new ArrayList<Square>();
	
	public UserMap(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		elements = new Square[rows][columns];
		
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
 *This create a Square object representing 
 *an x and y coordinates for each position
 *in the map
 *
 */
	private void init()
	{
		createSquares();
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

	private void createSquares()
	{

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < columns; j++)
			{
				elements[i][j] = new Square(i, j, this);				
			}
		}
	}

	public Square getSquare(int x, int y)
	{
		return elements[x][y];
	}

	public void setSquare(Square square)
	{
		elements[square.getX()][square.getY()] = square;
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
				Square square = elements[i][j];
			}
			System.out.println("");

			for (int j = 0; j < columns; j++)
			{
				Square square = elements[i][j];
				drawMap(square);
			}
			System.out.println("");
		}
	}
		
	private void drawMap(Square square)
	{
		int x = square.getX();
		int y = square.getY();
    	System.out.print(map[x][y] + " ");  
 		
		for (Square neighbor : square.getAdjacencies())
		{
			if (neighbor.getX() == x && neighbor.getY() == y - 1)
			{
				if (square.isEnd())
				{
					System.out.print(TARGET);
					return;
				}
				if (bestList.contains(square))
				{
					System.out.print(PATH);
					return;
				}
				System.out.print(SPACE);
				return;
			}
		}

		if (square.isEnd())
		{
			System.out.print(TARGET);
			return;
		}

		if (bestList.contains(square))
		{
			System.out.print(PATH);
			return;
		}
		System.out.print(SPACE);

	}

		public void findBestPath()
		{
		System.out.println("Calculating best path...");
		
		Set<Square> adjacencies = elements[0][0].getAdjacencies();
		
		for (Square adjacency : adjacencies)
		{
			adjacency.setParent(elements[0][0]);
			if (adjacency.isStart() == false)
			{
				opened.add(adjacency);				
			}
		}
		
		while (opened.size() > 0)
		{
			Square best = findBestPassThrough();
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
				Set<Square> neighbors = best.getAdjacencies();
				for (Square neighbor : neighbors)
				{
					if (opened.contains(neighbor))
					{
						Square tmpSquare = new Square(neighbor.getX(),neighbor.getY(), this);
						tmpSquare.setParent(best);
						if (tmpSquare.getPassThrough(target) >= neighbor.getPassThrough(target))
						{
							continue;
						}
					}

					if (closed.contains(neighbor))
					{
						Square tmpSquare = new Square(neighbor.getX(),neighbor.getY(), this);
						tmpSquare.setParent(best);
						if (tmpSquare.getPassThrough(target) >= neighbor.getPassThrough(target))
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

	private void populateBestList(Square square)
	{
		bestList.add(square);
		if (square.getParent().isStart() == false)
		{
			populateBestList(square.getParent());
		}

		return;
	}

	private Square findBestPassThrough()
	{
		Square best = null;
		for (Square square : opened)
		{
			if (best == null || square.getPassThrough(target) < best.getPassThrough(target))
			{
				best = square;
			}
		}

		return best;
	}
}
