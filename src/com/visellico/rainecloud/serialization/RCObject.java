package com.visellico.rainecloud.serialization;

import static com.visellico.rainecloud.serialization.SerializationUtils.*;

import java.util.ArrayList;
import java.util.List;

public class RCObject extends RCBase{

	public static final byte CONTAINER_TYPE = ContainerType.OBJECT;	//DataStorageType (field, array, object- indicates what the next few bytes mean)
//	private int size = 1 + 4 + 2 + 2 + 2 + 2;	//these represent the bytes of the fields we have here, 1 for container type, 2 for name length, name is handled else, and 2 and 2 for the last shorts
	private short fieldCount;
	public List<RCField> fields = new ArrayList<RCField>();
	private short arrayCount;
	public List<RCString> strings = new ArrayList<RCString>();
	private short stringCount;
	public List<RCArray> arrays = new ArrayList<RCArray>();
	private short objectCount;
	public List<RCObject> objects = new ArrayList<RCObject>();

//	private final static int sizeOffset = 1 + 2 + 4;
	
	//Later on, we may need, if we are serializing a HUGE amount of data, then a table or list (hashtable) or something would contain information on looking up
	//	where pieces of data are, so we don't run into performance issues of trying to deserialize the whole shebang at once. A table of contents, or index
	
	private RCObject() {
//		size += 1 + 2 + 2 + 2 + 2;
	}
	
	
	public RCObject(String name) {
		setName(name);
		size += 1 + 2 + 2 + 2 + 2;

//		size += name.length();
	}
	
	//this is one way of doing it- instead of calculating size each time we need it, we calculate it as we add things to this object
	public int getSize() {
//		int size = 1 + 2 + name.length;
//		for (RCField field : fields) {
//			size += field.getSize();
//		}
//		for (RCArray array : arrays) {
//			size += array.getSize();
//		}
		return size;	
	}
	
	public void addField(RCField field) {
		fields.add(field);
		size += field.getSize();
		fieldCount = (short) fields.size();	//Could have incremented this each time, but hopefully this makes it easier to try and handle adding like an array of fields
	}
	public void addString(RCString string) {
		strings.add(string);
		size += string.getSize();
		stringCount = (short) strings.size();	//Could have incremented this each time, but hopefully this makes it easier to try and handle adding like an array of fields
	}
	public void addArray(RCArray array) {
		arrays.add(array);
		size += array.getSize();
		arrayCount = (short) arrays.size();
	}
	public void addObject(RCObject object) {
		objects.add(object);
		size += object.getSize();
		objectCount = (short) objects.size();
	}
	
	public RCField findField(String name) {
		for (RCField field : fields) {
			if (field.getName().equals(name)) 
				return field;
		}
		return null;
	}
	
	public RCString findString(String name) {
		for (RCString string : strings) {
			if (string.getName().equals(name)) 
				return string;
		}
		return null;
	}
	
	public RCArray findArray(String name) {
		for (RCArray array : arrays) {
			if (array.getName().equals(name)) 
				return array;
		}
		return null;
	}
	
	//TODO rename to setBytes
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
//		System.out.println(pointer);
		pointer = writeBytes(dest, pointer, fieldCount);
		for (RCField field : fields) 
			pointer = field.getBytes(dest, pointer);
	
		pointer = writeBytes(dest, pointer, stringCount);
		for (RCString string : strings) 
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (RCArray array : arrays) 
			pointer = array.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, objectCount);
		for (RCObject object : objects) 
			pointer = object.getBytes(dest, pointer);
			
		return pointer;
	}
	
	//Deserialize one object. We pass the pointer as an array since arrays are (practically) passByRef
	public static RCObject deserialize(byte[] data, int pointer) {
		
//		int pointer = pointerRef[0];
		
		byte containerType = data[pointer];
		pointer++;
		assert(containerType == CONTAINER_TYPE);
		
		RCObject result = new RCObject();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		//Not TOO sure what this is about- I believe it is meant to manage if we want to break out of an object before deserializing all of it
		//Size of the entire object, minus the length of some of the meta data (total of 7 bytes here) minus the length of the name, to advance the pointer so it can read the next object
//		EARLY OUT pointer += result.size - sizeOffset - result.nameLength;
		
//		if (true)
//			return result;
				
		result.fieldCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.fieldCount; i++) {
			RCField field = RCField.deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.stringCount; i++) {
			RCString string = RCString.deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}
		
		result.arrayCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.arrayCount; i++) {
			RCArray array = RCArray.deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		//TODO: do object deserialization
		
		return result;
		
	}
	
}
