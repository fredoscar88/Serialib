package com.visellico.rainecloud.serialization;

import java.nio.ByteBuffer;

/**
 * Interacts with the lowest level of operation- writes our format.
 * @author fredo
 *
 */
public class SerializationUtils {
		
	/**
	 * Writes a byte array to the destination byte array
	 * @param dest
	 * @param pointer
	 * @param src
	 * @return
	 */
	public static int writeBytes(byte[] dest, int pointer, byte[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			dest[pointer++] = src[i];
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, short[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, char[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, int[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, long[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, float[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, double[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	//we going to optimize thie for bit fielding?
	public static int writeBytes(byte[] dest, int pointer, boolean[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes a byte to a byte array
	 * @param dest Array to write to
	 * @param pointer Index in <code>dest</code> to write to
	 * @param value Value to write
	 * @return
	 */
	public static int writeBytes(byte[] dest, int pointer, byte value) {
		//"We've written bytes to this pointer, here is the next one over"
		//	Separation of concerns here is a little off- why would the writer have to keep track? oh well.
		assert(dest.length > pointer + Type.getSize(Type.BYTE));
		dest[pointer++] = value;
		return pointer;
	}
	
	/**
	 * Writes a char to a byte array
	 * @param dest Array to write to
	 * @param pointer Index in <code>dest</code> to write to
	 * @param value Value to write
	 * @return
	 */
	public static int writeBytes(byte[] dest, int pointer, short value) {
		assert(dest.length > (pointer + Type.getSize(Type.SHORT)));
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes a short to a byte array. Chars are the equivalent of an unsigned short.
	 * @param dest Array to write to
	 * @param pointer Index in <code>dest</code> to write to
	 * @param value Value to write
	 * @return
	 */
	public static int writeBytes(byte[] dest, int pointer, char value) {
		assert(dest.length > pointer + Type.getSize(Type.CHAR));
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes an int to a byte array
	 * @param dest Array to write to
	 * @param pointer Index in <code>dest</code> to write to
	 * @param value Value to write
	 * @return
	 */
	public static int writeBytes(byte[] dest, int pointer, int value) {
		//Must write the most significant bits first, because they are what COME first
		assert(dest.length > pointer + Type.getSize(Type.INTEGER));
		dest[pointer++] = (byte) ((value >> 24) & 0xff);
		dest[pointer++] = (byte) ((value >> 16) & 0xff);
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes a long to a byte array
	 * @param dest Array to write to
	 * @param pointer Index in <code>dest</code> to write to
	 * @param value Value to write
	 * @return
	 */
	public static int writeBytes(byte[] dest, int pointer, long value) {
		assert(dest.length > pointer + Type.getSize(Type.LONG));
		//Must write the most significant bits first, because they are what COME first
		dest[pointer++] = (byte) ((value >> 56) & 0xff);
		dest[pointer++] = (byte) ((value >> 48) & 0xff);
		dest[pointer++] = (byte) ((value >> 40) & 0xff);
		dest[pointer++] = (byte) ((value >> 32) & 0xff);
		dest[pointer++] = (byte) ((value >> 24) & 0xff);
		dest[pointer++] = (byte) ((value >> 16) & 0xff);
		dest[pointer++] = (byte) ((value >> 8) & 0xff);
		dest[pointer++] = (byte) ((value >> 0) & 0xff);
		return pointer;
	}

	public static int writeBytes(byte[] dest, int pointer, float value) {
		assert(dest.length > pointer + Type.getSize(Type.FLOAT));
		int data = Float.floatToIntBits(value);
		return writeBytes(dest, pointer, data);
	}
	
	public static int writeBytes(byte[] dest, int pointer, double value) {
		assert(dest.length > pointer + Type.getSize(Type.DOUBLE));
		long data = Double.doubleToLongBits(value);
		return writeBytes(dest, pointer, data);
	}
	
	public static int writeBytes(byte[] dest, int pointer, boolean value) {
		assert(dest.length > pointer + Type.getSize(Type.BOOLEAN));
		dest[pointer++] = (byte)(value ? 1 : 0);
		return pointer;
	}
	
	/**
	 * Writes a boolean array to a byte array. MAY REMOVE
	 * @param dest Array to write to
	 * @param pointer Index in <code>dest</code> to write to
	 * @param value Value to write (indices greater than or equal to 8 are ignored)
	 * @deprecated
	 * @return
	 */
	public static int MYwriteBytes(byte[] dest, int pointer, boolean[] value) throws Exception {

		byte b = 0;
		int len = value.length;
		
		//Puts the bits in left to right
		if (len < 8) {
			for (int i = 0; i < len; i++) {
				b |= ((byte) (value[i] ? 1 : 0)) << (len - 1) - i;
			}			
		}
		else {
			for (int i = 0; i < 8; i++) {
				b |= ((byte) (value[i] ? 1 : 0)) << 7 - i;
			}
		}
		dest[pointer] = b;
		
		return pointer;
	}
	
	public static int writeBytes(byte[] dest, int pointer, String str) {
		// WAYS TO GO ABOUT WRITING THIS
		// 1. Write size before the string (05 F R E D O 0)
		// 2. Append null-termination char after the string
		// 3. Both lol
		// Using first option as it is TheCherno's preference
		
		pointer = writeBytes(dest, pointer, (short) str.length());
		return writeBytes(dest, pointer, str.getBytes());
	}
	
	//TODO SHOULDNT THIS GO INTO A SERIALIZATION READER CLASS?
	public static byte readByte(byte[] src, int pointer) {
//		return ByteBuffer.wrap(src, pointer, 1).getInt();	//LOL BYTES ARE SIGNED SO DAS WHY WE GET CRAZYNESS
		return src[pointer];	//a little useless
	}
	
	public static short readShort(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 2).getShort();	//LOL BYTES ARE SIGNED SO DAS WHY WE GET CRAZYNESS
//		return (short) ((src[pointer] << 8) | (src[pointer + 1] << 0));
	}
	
	public static char readChar(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 2).getChar();	//LOL BYTES ARE SIGNED SO DAS WHY WE GET CRAZYNESS
//		return (char) ((src[pointer] << 8) | (src[pointer + 1] << 0));
	}
	
	public static int readInt(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 4).getInt();	//LOL BYTES ARE SIGNED SO DAS WHY WE GET CRAZYNESS
//		return (int) ((src[pointer] << 24) | (src[pointer + 1] << 16) | (src[pointer + 2] << 8) | (src[pointer + 3]));
	}
	
	public static long readLong(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 8).getLong();	//LOL BYTES ARE SIGNED SO DAS WHY WE GET CRAZYNESS
//		return (long) ((src[pointer + 0] << 56) | (src[pointer + 1] << 48) | (src[pointer + 2] << 40) | (src[pointer + 3] << 32) | 
//					  ((src[pointer + 4] << 24) | (src[pointer + 5] << 16) | (src[pointer + 6] << 8) | (src[pointer + 7])));
	}
	
	public static float readFloat(byte[] src, int pointer) {
		return Float.intBitsToFloat(readInt(src, pointer));
	}
	
	public static double readDouble(byte[] src, int pointer) {
		return Double.longBitsToDouble(readLong(src, pointer));
	}
	
	public static boolean readBoolean(byte[] src, int pointer) {
		assert(src[pointer] == 0 || src[pointer] == 1);
		return src[pointer] != 0;
	}
	
	public static String readString(byte[] src, int pointer, int length) {
		return new String(src, pointer, length);
	}
	
	public static void readBytes(byte[] src, int pointer, byte[] dest) {
		for (int i = 0; i < dest.length; i++) 
			dest[i] = src[pointer + i];
	}
	
	public static void readShorts(byte[] src, int pointer, short[] dest) {
		for (int i = 0; i < dest.length; i++) 
			dest[i] = readShort(src, pointer);
		pointer += Type.getSize(Type.SHORT);
	}
	
	public static void readChars(byte[] src, int pointer, char[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readChar(src, pointer);
			pointer += Type.getSize(Type.CHAR);
		}
	}
	
	public static void readInts(byte[] src, int pointer, int[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readInt(src, pointer);
			pointer += Type.getSize(Type.INTEGER);
		}
	}
	
	public static void readLongs(byte[] src, int pointer, long[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readLong(src, pointer);
			pointer += Type.getSize(Type.LONG);
		}
	}
	
	public static void readFloats(byte[] src, int pointer, float[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readFloat(src, pointer);
			pointer += Type.getSize(Type.FLOAT);
		}
	}
	
	public static void readDoubles(byte[] src, int pointer, double[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readDouble(src, pointer);
			pointer += Type.getSize(Type.DOUBLE);
		}
	}
	
	public static void readBooleans(byte[] src, int pointer, boolean[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readBoolean(src, pointer);
			pointer += Type.getSize(Type.BOOLEAN);
		}
	}
	
	//TODO make sure this works. MAY REMOVE
	@Deprecated
	public static boolean[] readBitField(byte[] src, int pointer) {
		byte b = src[pointer];
		boolean[] result = new boolean[8];
		for (int i = 0; i < 8; i++) {
			result[i] = (b >> 7 - i) != 0;
		}
		return result;
	}
}
