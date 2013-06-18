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
import java.io.*;
import java.util.*;

/***************************************
 * The ReadMapFile Class opens a map file
 * and dumps it contains into a 2D arrays 
 * of strings
 ***************************************/

public class ReadMapFile
{
	    private static final int ARRAYSIZE = 51;
		private FileInputStream fstream;
	    private DataInputStream dataInputStream;
	    private BufferedReader bufferedReader;
	    
/***************************************
 * The constructor for ReadMapFile takes
 * a String argument, which is the name 
 * of the file you want to open
 ***************************************/
	    public ReadMapFile(String fname) throws Exception
	    {
    		fstream = new FileInputStream(fname);  
 			dataInputStream = new DataInputStream(fstream);  
 			bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));  
		}

/****************************************
* The readMap( ) method returns a map 
* containing elements of the file.
***************************************/	
		public String[][] readMap()throws IOException 
		{
 			String strLine; 
 			int arraySize = ARRAYSIZE;  
 			String map[][] = new String[arraySize][arraySize];
 			String tempString[][] = new String[arraySize][];  
 			int index = 0;
 			try
 			{
 				while ((strLine = bufferedReader.readLine()) != null)
 				{  
    				if (index >= arraySize - 1)
    				{  
   						System.out.println("Error : Increase array size !");  
   						break;  
  					}  
  					tempString[index] = strLine.split(" ");//read file line
  					for (int i = index; i < index + 1; i++)
  					{
	  					for (int j = 0; j < arraySize - 1; j++)
	  					{
		  					char ch = tempString[index][0].charAt(j); //read a letter from the string and convert to char 
		  					map[i][j] =  Character.toString(ch);// convert back to string and load to map array
			  			}
  					}  
  					index++;  
				}
			}
			catch(IOException e)
			{
				throw new RuntimeException("readLine() failed");
			}
			return map; 
    	}
    
    	/***************************************
 		 * The method prints the contents of the 
 		 * map file to the standard output
 		 ***************************************/
    public void drawMap(String map[][])
	{  
		for (int i = 0; i < map.length; i++)
 		{  
			for (int j = 0; j < map.length; j++)
   			{  
    			System.out.print(map[i][j] + " ");  
 			}  
   			System.out.println(" ");  
  		}  
 	}
/****************************************************
 * The dispose( ) method is called when 
 * the InputFile object is no longer needed. This will 
 * release the system resources (such as file handles) 
 *  that are used by the BufferedReader objects.
***************************************************/
 	public void dispose()
 	{
		try
		{
			dataInputStream.close();
			System.out.println("dispose() successful");
		}
		catch(IOException e2)
		{
			throw new RuntimeException("dataInputStream.close() failed");
		}
	}  
}