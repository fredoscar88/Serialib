package com.visellico.rainecloud.serialization;

import static com.visellico.rainecloud.serialization.SerializationUtils.*;

public class RCString extends RCBase {
	public static final byte CONTAINER_TYPE = ContainerType.STRING;	//DataStorageType (field, array, object- indicates what the next few bytes mean)
//	public int size = 1 + 2 + 4 + 4;
	public int count;
	public char[] characters;

	private RCString() {
		size += 1 + 4;
	}
	
	public String getString() {
		return new String(characters);
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
		pointer = writeBytes(dest, pointer, count);	//char count
		pointer = writeBytes(dest, pointer, characters); 
		return pointer;
	}
	
	/**
	 * Get the amount of bytes this field is serialized in
	 */
	public int getSize() {
		return size;
	}

	public int getDataSize() {
		return characters.length * Type.getSize(Type.CHAR);
	}
	
	//We may as well just use our constructor, but to keep in line with everything else this is fine
	public static RCString Create(String name, String data) {
		RCString string = new RCString();
		string.setName(name);
		string.count = data.length();
		string.characters = data.toCharArray();
		string.updateSize();
		return string;
	}
	public static RCString deserialize(byte[] data, int pointer) {
		byte containerType = readByte(data, pointer);
		pointer++;
		assert (containerType == CONTAINER_TYPE);
		
		RCString result = new RCString();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		result.characters = new char[result.count];
		readChars(data, pointer, result.characters);
		
		pointer += result.count * Type.getSize(Type.CHAR);
		return result;
	}
	
}
