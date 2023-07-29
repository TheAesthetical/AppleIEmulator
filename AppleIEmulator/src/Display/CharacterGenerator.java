package Display;

public class CharacterGenerator {

	public boolean[][][] CharacterROMS = {

			//@ - 000000
			{ 	{false , false , false , false , false} , 
				{false , true , true , true , false} ,
				{true , false , false , false , true} ,
				{true , false , true , false , true} ,
				{true , false , true , true , true} ,
				{true , false , true , true , false} ,
				{true , false , false , false , false} ,
				{false , true , true , true , true} ,

			},

			//A - 000001
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , true , false , true , false} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,

			},

			//B - 000001
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , false} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , false} ,

			},

			//C - 000011
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//D - 000100
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , false} ,

			},

			//E - 000101
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , true} ,

			},

			//F - 000110
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,

			},

			//G - 000111
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , true} , 
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , true , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , true} ,

			},

			//H - 001000
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,

			},

			//I - 001001
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , true , true , true , false} ,

			},

			//J - 001010
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , true} , 
				{false , false , false , false , true} ,
				{false , false , false , false , true} ,
				{false , false , false , false , true} ,
				{false , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//K - 001011
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , true , false} ,
				{true , false , true , false , false} ,
				{true , true , false , false , false} ,
				{true , false , true , false , false} ,
				{true , false , false , true , false} ,
				{true , false , false , false , true} ,

			},

			//L - 001100
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , false} , 
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , true} ,

			},

			//M - 001101
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , true , false , true , true} ,
				{true , false , true , false , true} ,
				{true , false , true , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,

			},

			//N - 001110
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{true , true , false , false , true} ,
				{true , false , true , false , true} ,
				{true , false , false , true , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,

			},

			//O - 001111
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//P - 010000
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,

			},

			//Q - 010001
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , true , false , true} ,
				{true , false , false , true , false} ,
				{false , true , true , false , true} ,

			},

			//R - 010010
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , true , true , true , false} ,
				{true , false , true , false , false} ,
				{true , false , false , true , false} ,
				{true , false , false , false , true} ,

			},

			//S - 010011
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , false} ,
				{false , true , true , true , false} ,
				{false , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//T - 010100
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,

			},

			//U - 010101
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//V - 010110
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , false , true , false} ,
				{false , false , true , false , false} ,

			},

			//W - 010111
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{true , false , true , false , true} ,
				{true , false , true , false , true} ,
				{true , true , false , true , true} ,
				{true , false , false , false , true} ,

			},

			//X - 011000
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{false , true , false , true , false} ,
				{false , false , true , false , false} ,
				{false , true , false , true , false} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,

			},

			//Y - 011001
			{ 	{false , false , false , false , false} ,
				{true , false , false , false , true} , 
				{true , false , false , false , true} ,
				{false , true , false , true , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,

			},

			//Z - 011010
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , true} ,

			},

			//[ - 011011
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{true , true , false , false , false} ,
				{true , true , false , false , false} ,
				{true , true , false , false , false} ,
				{true , true , false , false , false} ,
				{true , true , false , false , false} ,
				{true , true , true , true , true} ,

			},

			//\ - 011100
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{true , false , false , false , false} ,
				{false , true , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , true , false} ,
				{false , false , false , false , true} ,
				{false , false , false , false , false} ,

			},

			//] - 011101
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{false , false , false , true , true} ,
				{false , false , false , true , true} ,
				{false , false , false , true , true} ,
				{false , false , false , true , true} ,
				{false , false , false , true , true} ,
				{true , true , true , true , true} ,

			},

			//^ - 011110
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,
				{false , true , false , true , false} ,
				{true , false , false , false , true} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//_ - 011111
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{true , true , true , true , true} ,

			},

			//  - 100000
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//! - 100001
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,

			},

			//" - 100010
			{ 	{false , false , false , false , false} ,
				{false , true , false , true , false} , 
				{false , true , false , true , false} ,
				{false , true , false , true , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//# - 100011
			{ 	{false , false , false , false , false} ,
				{false , true , false , true , false} , 
				{false , true , false , true , false} ,
				{true , true , true , true , true} ,
				{false , true , false , true , false} ,
				{true , true , true , true , true} ,
				{false , true , false , true , false} ,
				{false , true , false , true , false} ,

			},

			//$ - 100100
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , true , true , true , true} ,
				{true , false , true , false , false} ,
				{false , true , true , true , false} ,
				{false , false , true , false , true} ,
				{true , true , true , true , false} ,
				{false , false , true , false , false} ,

			},

			//% - 100101
			{ 	{false , false , false , false , false} ,
				{true , true , false , false , false} , 
				{true , true , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,
				{true , false , false , true , true} ,
				{false , false , false , true , true} ,

			},

			//& - 100110
			{ 	{false , false , false , false , false} ,
				{false , true , false , false , false} , 
				{true , false , true , false , false} ,
				{true , false , true , false , false} ,
				{false , true , false , false , false} ,
				{true , false , true , false , true} ,
				{true , false , false , true , false} ,
				{false , true , true , false , true} ,

			},

			//' - 100111
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//( - 101000
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , true , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{true , false , false , false , false} ,
				{false , true , false , false , false} ,
				{false , false , true , false , false} ,

			},

			//) - 101001
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , false , false , true , false} ,
				{false , false , false , false , true} ,
				{false , false , false , false , true} ,
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,

			},

			//* - 101010
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{true , false , true , false , true} ,
				{false , true , true , true , false} ,
				{false , false , true , false , false} ,
				{false , true , true , true , false} ,
				{true , false , true , false , true} ,
				{false , false , true , false , false} ,

			},

			//+ - 101011
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{true , true , true , true , true} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,

			},

			//, - 101100
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,

			},

			//- - 101101
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{true , true , true , true , true} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//. - 101110
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,

			},

			/// - 101111
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,
				{true , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//0 - 110000
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , true , true} ,
				{true , false , true , false , true} ,
				{true , true , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//1 - 110001
			{ 	{false , false , false , false , false} ,
				{false , false , true , false , false} , 
				{false , true , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , true , true , true , false} ,

			},

			//2 - 110010
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{false , false , false , false , true} ,
				{false , false , true , true , false} ,
				{false , true , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , true} ,

			},

			//3 - 110011
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , true , false} ,
				{false , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//4 - 110100
			{ 	{false , false , false , false , false} ,
				{false , false , false , true , false} , 
				{false , false , true , true , false} ,
				{false , true , false , true , false} ,
				{true , false , false , true , false} ,
				{true , true , true , true , true} ,
				{false , false , false , true , false} ,
				{false , false , false , true , false} ,

			},

			//5 - 110101
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{true , false , false , false , false} ,
				{true , true , true , true , false} ,
				{false , false , false , false , true} ,
				{false , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//6 - 110110
			{ 	{false , false , false , false , false} ,
				{false , false , true , true , true} , 
				{false , true , false , false , false} ,
				{true , false , false , false , false} ,
				{true , true , true , true , false} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//7 - 110111
			{ 	{false , false , false , false , false} ,
				{true , true , true , true , true} , 
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,
				{false , true , false , false , false} ,
				{false , true , false , false , false} ,

			},

			//8 - 111000
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , false} ,

			},

			//9 - 111001

			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{true , false , false , false , true} ,
				{false , true , true , true , true} ,
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{true , true , true , false , false} ,

			},

			//: - 111010

			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//; - 111011

			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,

			},

			//< - 111100

			{ 	{false , false , false , false , false} ,
				{false , false , false , true , false} , 
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,
				{true , false , false , false , false} ,
				{false , true , false , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , true , false} ,

			},

			//= - 111101
			{ 	{false , false , false , false , false} ,
				{false , false , false , false , false} , 
				{false , false , false , false , false} ,
				{true , true , true , true , true} ,
				{false , false , false , false , false} ,
				{true , true , true , true , true} ,
				{false , false , false , false , false} ,
				{false , false , false , false , false} ,

			},

			//> - 111110
			{ 	{false , false , false , false , false} ,
				{false , true , false , false , false} , 
				{false , false , true , false , false} ,
				{false , false , false , true , false} ,
				{false , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,
				{false , true , false , false , false} ,

			},

			//? - 111111
			{ 	{false , false , false , false , false} ,
				{false , true , true , true , false} , 
				{true , false , false , false , true} ,
				{false , false , false , true , false} ,
				{false , false , true , false , false} ,
				{false , false , true , false , false} ,
				{false , false , false , false , false} ,
				{false , false , true , false , false} ,

			}

	};

}
