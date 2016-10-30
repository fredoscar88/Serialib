package com.visellico.rainecloud.serialization;

import static com.visellico.rainecloud.serialization.SerializationUtils.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RCDatabase extends RCBase {

	//We only use one database at a time, so this goes to the header, only reading one file.
	public static final byte[] HEADER = "RCDB".getBytes();
	public static final short VERSION = 0x0100;	//Major Minor format- 1.0, 1.1, 2.3 etc.
	//Two bytes, a short, for our version. Big endian. (Endian describes the order of our bytes). Little endian this would be 0x0001, kinda flipping them here, putting the most significant on the right.

	public static final byte CONTAINER_TYPE = ContainerType.DATABASE;	//DataStorageType (field, array, object- indicates what the next few bytes mean)
//	private int size = HEADER.length + 2 + 1 + 2 + 4 + 2;
	private short objectCount;
	public List<RCObject> objects = new ArrayList<RCObject>();
	
	private RCDatabase() {
		
	}
	
	public RCDatabase(String name) {
		setName(name);
		size += HEADER.length + 2 + 1 + 2;
	}
	
	public int getSize() {
		return size;	
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, HEADER);
		pointer = writeBytes(dest, pointer, VERSION);
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, objectCount);
		for (RCObject object : objects) 
			pointer = object.getBytes(dest, pointer);
			
		return pointer;
	}
		
	public void addObject(RCObject object) {
		objects.add(object);
		size += object.getSize();
		objectCount = (short) objects.size();
	}

	public static RCDatabase Deserialize(byte[] data) {
		
		int pointer = 0;
		assert(readString(data, pointer, HEADER.length).equals(HEADER));
		pointer += HEADER.length;
		
		if (readShort(data, pointer) != VERSION) {
			System.err.println("Invalid RCDB Version");
			return null;
		}
		pointer += Type.getSize(Type.SHORT);
		
		
		byte containerType = readByte(data, pointer);
		assert (containerType == CONTAINER_TYPE);
		pointer++;	//I dont want to do this inline for consistency's sake
		
		RCDatabase result = new RCDatabase();
		
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.objectCount = readShort(data, pointer);
		pointer += 2;
		
		//-------------
//		int[] pointerRef = new int[] { pointer };
		for (int i = 0 ; i < result.objectCount; i++) {	
			RCObject object = RCObject.deserialize(data, pointer);
			result.objects.add(object);
			pointer += object.getSize();
			//Hopefully pointer increments if we make it pass by ref.
		}
//		pointer = pointerRef[0];
		
		
		return result;
		
	}
	
	public RCObject findObject(String name) {
		for (RCObject object : objects) {
			if (object.getName().equals(name)) 
				return object;
		}
		return null;
	}
	
	public static RCDatabase deserializeFromFile(String path) {
		byte[] buffer = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Deserialize(buffer);
	}
	
	public void serializeToFile(String path) {
		
		byte[] data = new byte[getSize()];
		getBytes(data, 0);
		
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
			stream.write(data);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
