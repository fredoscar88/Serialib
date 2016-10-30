package com.visellico.rainecloud.serialization;


import static com.visellico.rainecloud.serialization.SerializationUtils.*;

public class RCArray extends RCBase{

	public static final byte CONTAINER_TYPE = ContainerType.ARRAY;	//DataStorageType (field, array, object- indicates what the next few bytes mean)
//	public int size = 1 + 1 + 4;
	public byte type;
	public int count;	//int, because we may have many, many elements. More than 255, specifically, or more than 65535
	public byte[] data;

	//This is NOT the best solution~!
	private short[] shortData;
	private char[] charData;
	private int[] intData;
	private long[] longData;
	private float[] floatData;
	private double[] doubleData;
	private boolean[] booleanData;
	
	private RCArray() {
		size += 1 + 1 + 4;
	}
		
	private void updateSize() {
		size += getDataSize();
	}
	
	//Writes the bytes of this field
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, count);
		
		switch(type) {
		case Type.BYTE: 
			pointer = writeBytes(dest, pointer, data); 
			break;
		case Type.SHORT: 
			pointer = writeBytes(dest, pointer, shortData); 
			break;
		case Type.CHAR: 
			pointer = writeBytes(dest, pointer, charData); 
			break;
		case Type.INTEGER: 
			pointer = writeBytes(dest, pointer, intData); 
			break;
		case Type.LONG: 
			pointer = writeBytes(dest, pointer, longData); 
			break;
		case Type.FLOAT: 
			pointer = writeBytes(dest, pointer, floatData); 
			break;
		case Type.DOUBLE: 
			pointer = writeBytes(dest, pointer, doubleData); 
			break;
		case Type.BOOLEAN: 
			pointer = writeBytes(dest, pointer, booleanData); 
			break;
		}
		
				
		return pointer;
	}
	
	/**
	 * Get the amount of bytes this field is serialized in
	 */
	public int getSize() {
		
		//We're using asserts 'everywhere' because this is an API, it needs to work
		assert(data.length == Type.getSize(type));	//if this is not true, then someone tampered with the binary. So this is a bit of an integrity check on the file.
		
		//To be EXTREMELY anal, we could use Type.getSize, JUST in case for some ungodly reason the amount of bits in each primitive changes.
		return size;
	}
	
	public int getDataSize() {
		switch(type) {
		case Type.BYTE: 	return data.length * Type.getSize(Type.BYTE);
		case Type.SHORT: 	return shortData.length * Type.getSize(Type.SHORT);
		case Type.CHAR: 	return charData.length * Type.getSize(Type.CHAR);
		case Type.INTEGER: 	return intData.length * Type.getSize(Type.INTEGER);
		case Type.LONG: 	return longData.length * Type.getSize(Type.LONG);
		case Type.FLOAT: 	return floatData.length * Type.getSize(Type.FLOAT);
		case Type.DOUBLE: 	return doubleData.length * Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN: 	return booleanData.length * Type.getSize(Type.BOOLEAN);
		}
		return 0;
	}
	
	//Could subclass these but meh.
	//Also this isn't very OO, particularly since we have a billion arrays for each data type when only one will be used at a time.
	public static RCArray Byte(String name, byte[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.BYTE;
		array.count = data.length;
		array.data = data;	//here's hoping this doesn't get garbage collected
		array.updateSize();
		return array;
	}

	public static RCArray Short(String name, short[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.SHORT;
		array.count = data.length;
		array.shortData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Char(String name, char[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.CHAR;
		array.count = data.length;
		array.charData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Int(String name, int[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.INTEGER;
		array.count = data.length;
		array.intData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Long(String name, long[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.LONG;
		array.count = data.length;
		array.longData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Float(String name, float[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.FLOAT;
		array.count = data.length;
		array.floatData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Double(String name, double[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.DOUBLE;
		array.count = data.length;
		array.doubleData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Boolean(String name, boolean[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.BOOLEAN;
		array.count = data.length;
		array.booleanData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray deserialize(byte[] data, int pointer) {
		byte containerType = readByte(data, pointer);
		pointer++;
		assert (containerType == CONTAINER_TYPE);
		
		RCArray result = new RCArray();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
				
		result.type = readByte(data, pointer);
		pointer++;
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		switch(result.type) {
		case Type.BYTE: 
			result.data = new byte[result.count];
			readBytes(data, pointer, result.data); 
			break;
		case Type.SHORT: 
			result.shortData = new short[result.count];
			readShorts(data, pointer, result.shortData); 
			break;
		case Type.CHAR: 
			result.charData = new char[result.count];
			readChars(data, pointer, result.charData); 
			break;
		case Type.INTEGER: 
			result.intData = new int[result.count];
			readInts(data, pointer, result.intData); 
			break;
		case Type.LONG: 
			result.longData = new long[result.count];
			readLongs(data, pointer, result.longData); 
			break;
		case Type.FLOAT: 
			result.floatData = new float[result.count];
			readFloats(data, pointer, result.floatData); 
			break;
		case Type.DOUBLE: 
			result.doubleData = new double[result.count];
			readDoubles(data, pointer, result.doubleData); 
			break;
		case Type.BOOLEAN: 
			result.booleanData = new boolean[result.count];
			readBooleans(data, pointer, result.booleanData); 
			break;
		}
		
		//dont really need to advance the pointer
		pointer += result.count * Type.getSize(result.type);
		
		return result;
	}
	
}
