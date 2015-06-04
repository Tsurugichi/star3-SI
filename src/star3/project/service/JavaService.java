package star3.project.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

public class JavaService 
{
	public JavaService() 
	{
		
	}
	
	public void putPawnHuman(HashMap<Integer, String> playboard, PrologService ps, int number, int xy) throws IOException
	{
		String position = Integer.toString(xy);
		
		try 
		{			
			String[] positionArray = position.split("");
			int[] xyArray = new int[positionArray.length];
			
			for (int i = 0; i < positionArray.length; i++) 
			{
				xyArray[i] = Integer.parseInt(positionArray[i]);
			}
			String[] playerTypeArray = playboard.get(xyArray[0] + (xyArray[1])).split("");

				//X + (Y-1)*5
				ps.setCoordHuman(Integer.parseInt(position));
				playboard.put((xyArray[0] + ((xyArray[1]-1)*5)), "B" + number);
			
		} 
		catch (NumberFormatException e) 
		{
		      System.out.println("Wprowadzona wartoœæ nie jest liczb¹. Spróbuj ponownie wprowadziæ poprawn¹ wartoœæ.");
		      putPawnHuman(playboard, ps, number, xy);
		}		
	}
	
	public int putPawnAI(HashMap<Integer, String> playboard, PrologService ps, int number)
	{
		int xy = ps.setCoordAI();
		playboard.put(xy, "R" + number);
		paintPlayboard(playboard);
		return xy;
	}
	
	public void movePawnHuman(HashMap<Integer, String> playboard, PrologService ps, int xyold, int xynew) throws IOException
	{	
		String positionold = Integer.toString(xyold);
		
		String[] positionoldArray = positionold.split("");
		int[] xyoldArray = new int[positionoldArray.length];
			
		for (int i = 0; i < positionoldArray.length; i++) 
		{
			xyoldArray[i] = Integer.parseInt(positionoldArray[i]);
		}
		
		String positionnew = Integer.toString(xynew);
		
		String[] positionnewArray = positionnew.split("");
		int[] xynewArray = new int[positionnewArray.length];
			
		for (int i = 0; i < positionnewArray.length; i++) 
		{
			xynewArray[i] = Integer.parseInt(positionnewArray[i]);
		}
		
		int n = xynewArray[0] + ((xynewArray[1]-1)*5);
		int o = xyoldArray[0] + ((xyoldArray[1]-1)*5);
		
		String direction = "";
		
		if(xynew == xyold-10)
		{
			direction = "g";
		}
		else if(xynew == xyold+10)
		{
			direction = "d";
		}
		else if(xynew == xyold-1)
		{
			direction = "l";
		}
		else if(xynew == xyold+1)
		{
			direction = "p";
		}
		else if(xynew == xyold-11)
		{
			direction = "gl";
		}
		else if(xynew == xyold-9)
		{
			direction = "gp";
		}
		else if(xynew == xyold+11)
		{
			direction = "dp";
		}
		else if(xynew == xyold+9)
		{
			direction = "dl";
		}
		
		
		
		String field=playboard.get(o);
		
			
			playboard.put(n, field);
			ps.moveHuman(field.toLowerCase() + direction.toLowerCase());
			playboard.put(o, "");
			paintPlayboard(playboard);

	}
	
	public int[] movePawnAI(HashMap<Integer, String> playboard, PrologService ps)
	{
		int[] xy = new int[2];
		xy = ps.moveAI(playboard);
		paintPlayboard(playboard);
		return xy;
	}
	
	public boolean checkWin(HashMap<Integer, String> playboard, String player)
	{
		String[] pawnIDArray;
		int firstPosition=0, winCounter=0;
		
		//pion	
		for (Entry<Integer, String> entry : playboard.entrySet()) 
		{
			pawnIDArray = entry.getValue().split("");
			
            if (pawnIDArray[0].equals(player)) 
            {    	          	
            	if(winCounter>=1 && entry.getKey()==(winCounter*5  + firstPosition))
            	{
            		winCounter++;
            	}
            	
            	if(winCounter==0 && entry.getKey() > 10)
            	{
            		break;
            	}
            	
            	if(winCounter==0)
            	{
            		firstPosition = entry.getKey();
            		winCounter++;
            	}          	
            }
        }
		
		if(winCounter==4)
		{
			return true;
		}
		else
		{
			winCounter=0;
		}
		
		//poziom
		for (Entry<Integer, String> entry : playboard.entrySet()) 
		{
			pawnIDArray = entry.getValue().split("");
			
            if (pawnIDArray[0].equals(player)) 
            {    	
            	if(winCounter>=1 && entry.getKey()==(winCounter + firstPosition))
            	{
            		winCounter++;
            	}
            	else
            	{
            		break;
            	}
            	
            	if((winCounter==0 && (entry.getKey()%5==1)) || (winCounter==0 && (entry.getKey()%5==2)))
            	{
            		firstPosition = entry.getKey();
            		winCounter++;
            	}
            	else
            	{
            		break;
            	}           	
            }
        }
		
		if(winCounter==4)
		{
			return true;
		}
		else
		{
			winCounter=0;
		}
		
		//skos prawo
		for (Entry<Integer, String> entry : playboard.entrySet()) 
		{
			pawnIDArray = entry.getValue().split("");
			
            if (pawnIDArray[0].equals(player)) 
            {    	
            	if(winCounter>=1 && entry.getKey()==(winCounter*6 + firstPosition))
            	{
            		winCounter++;
            	}
            	
            	if(winCounter==0 && entry.getKey() > 10)
            	{
            		break;
            	}
            	
            	if(winCounter==0)
            	{
            		firstPosition = entry.getKey();
            		winCounter++;
            	}            	
            }
        }
		
		if(winCounter==4)
		{
			return true;
		}
		else
		{
			winCounter=0;
		}
		
		//skos lewo
		for (Entry<Integer, String> entry : playboard.entrySet()) 
		{
			pawnIDArray = entry.getValue().split("");
			
            if (pawnIDArray[0].equals(player)) 
            {    	
            	if(winCounter>=1 && entry.getKey()==(winCounter*4 + firstPosition))
            	{
            		winCounter++;
            	}
            	
            	if(winCounter==0 && entry.getKey() > 10)
            	{
            		break;
            	}
            	
            	if(winCounter==0)
            	{
            		firstPosition = entry.getKey();
            		winCounter++;
            	}	
            }
        }
		
		if(winCounter==4)
		{
			return true;
		}
		else
		{
			winCounter=0;
		}
		
		//kwadrat
		for (Entry<Integer, String> entry : playboard.entrySet()) 
		{
			pawnIDArray = entry.getValue().split("");
			
            if (pawnIDArray[0].equals(player)) 
            {    	   	
            	if(winCounter==1 && entry.getKey()==(firstPosition+1))
            	{
            		winCounter++;
            	}
            	
            	if(winCounter==2 && entry.getKey()==(firstPosition+5))
            	{
            		winCounter++;
            	}
            	
            	if(winCounter==3 && entry.getKey()==(firstPosition+6))
            	{
            		winCounter++;
            	}
            	
            	if(winCounter==0)
            	{
            		firstPosition = entry.getKey();
            		winCounter++;
            	}
            }
        }
		
		if(winCounter==4)
		{
			return true;
		}
			
		return false;
	}
	
	public void endOfGame(HashMap<Integer, String> playboard, String player)
	{
		if(player.equals("B"))
		{
			paintPlayboard(playboard);
			System.out.println("\nGra skoñczona. Wygra³eœ!");
		}
		else
		{
			System.out.println("\nGra skoñczona. Przegra³eœ!");
		}
	}
	
	public void paintPlayboard(HashMap<Integer, String> playboard)
	{	
		String variable;
		
		System.out.println("\n\nY/X   1    2    3    4    5");
		System.out.print("    ———— ———— ———— ———— ———— \n1  ");
		
		for(int i=0;i<playboard.size();i++)
		{
			variable = playboard.get(i+1);
			
			if(variable.equals(""))
			{
				System.out.print("|    ");
			}
			else
			{
				System.out.print("| " + variable + " ");
			}
			
			if(i%5==4)
			{
				System.out.println("|");
				if(i!=24)
				{
					System.out.print("    ———— ———— ———— ———— ———— \n" + ((i+6)/5) + "  ");
				}
				else
				{
					System.out.println("    ———— ———— ———— ———— ———— ");
				}
			}
		}
	}	
}
