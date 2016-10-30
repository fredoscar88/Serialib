package com.visellico.rainecloud.serialization;

public class ContainerType {

	//Mutual Exclusion. Also this is basically an enum, but with bytes
	public static final byte UNKNOWN 	= 0;
	public static final byte FIELD 		= 1;
	public static final byte ARRAY 		= 2;
	public static final byte STRING		= 3;
	public static final byte OBJECT 	= 4;
	public static final byte DATABASE 	= 5;
	
}
