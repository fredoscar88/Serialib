package com.visellico.rainecloud.serialization;

import static com.visellico.rainecloud.serialization.SerializationUtils.*;


/**
 * Primitive Field, serialized version of a primitive field
 * @author Henry
 *
 */
public class RCField extends RCBase {
	
	//containerType is a constant, surely, and also type
	public static final byte CONTAINER_TYPE = ContainerType.FIELD;	//DataStorageType (field, array, object- indicates what the next few bytes mean)
	public byte type;	//which primitive we have.
	//Dont need a size field- why? Because type indicates that the next X bytes are the actual data, type == size, or amount of bytes.
	public byte[] data;
	
	private RCField() {
		
	}
	
	public byte getByte() {
		return readByte(data, 0);	//return data[0]; but I like consistency
	}
	
	public short getShort() {
		return readShort(data, 0);
	}
	
	public char getChar() {
		return readChar(data, 0);
	}
	
	public int getInt() {
		return readInt(data, 0);
	}
	
	public long getLong() {
		return readLong(data, 0);
	}
	
	public float getFloat() {
		return readFloat(data, 0);
	}
	
	public double getDouble() {
		return readDouble(data, 0);
	}
	
	public boolean getBoolean() {
		return readBoolean(data, 0);
	}
	
	//Writes the bytes of this field
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, data);
		return pointer;
	}
	
	/**
	 * Get the amount of bytes this field is serialized in
	 */
	public int getSize() {
		
		//We're using asserts 'everywhere' because this is an API, it needs to work
		assert(data.length == Type.getSize(type));	//if this is not true, then someone tampered with the binary. So this is a bit of an integrity check on the file.
		
		//To be EXTREMELY anal, we could use Type.getSize, JUST in case for some ungodly reason the amount of bits in each primitive changes.
		return 1 + 2 + name.length + 1 + data.length;
	}
	
	//Could subclass these but meh
	public static RCField Byte(String name, byte value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.BYTE;
		field.data = new byte[Type.getSize(Type.BYTE)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Short(String name, short value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.SHORT;
		field.data = new byte[Type.getSize(Type.SHORT)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Char(String name, char value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.CHAR;
		field.data = new byte[Type.getSize(Type.CHAR)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Int(String name, int value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.INTEGER;
		field.data = new byte[Type.getSize(Type.INTEGER)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Long(String name, long value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.LONG;
		field.data = new byte[Type.getSize(Type.LONG)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Float(String name, float value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.FLOAT;
		field.data = new byte[Type.getSize(Type.FLOAT)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Double(String name, double value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.DOUBLE;
		field.data = new byte[Type.getSize(Type.DOUBLE)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField Boolean(String name, boolean value) {
		RCField field = new RCField();
		field.setName(name);
		field.type = Type.BOOLEAN;
		field.data = new byte[Type.getSize(Type.BOOLEAN)];
		writeBytes(field.data, 0 ,value);
		return field;
	}
	
	public static RCField deserialize(byte[] data, int pointer) {

		byte containerType = readByte(data, pointer);
		pointer++;
		assert (containerType == CONTAINER_TYPE);
		
		RCField result = new RCField();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.type = readByte(data, pointer);
		pointer++;
		
		result.data = new byte[Type.getSize(result.type)];
		readBytes(data, pointer, result.data);
		pointer += Type.getSize(result.type);
		return result;
	}
	
}
