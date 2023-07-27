package Display;

import Main.Display;

public class CharacterGenerator {

	public static boolean[][][] CharacterROMS = {
			{ 	{false , false , false , false , false} , 
				{false , true , true , true , false} ,
				{true , false , false , false , true} ,
				{true , false , true , false , true} ,
				{true , false , true , true , true} ,
				{true , false , true , true , false} ,
				{true , false , false , false , false} ,
				{false , true , true , true , true} ,
				
			},
			
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , true , false , true , false} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				
			}
			
			};
	
	public static void main(String[] args) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print(CharacterROMS[1][j][i]);
			}
			System.out.print("\n");
		}

	}
}
