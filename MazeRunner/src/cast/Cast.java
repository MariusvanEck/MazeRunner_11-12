package cast;

public class Cast {
	
	/**
	 * Used to put 2 or 4 bytes in an integer format (in Big-endian).
	 * @param b							A byte-array that contains the bytes
	 * @return							returns a int representing the byte-array
	 * @throws InvalidByteArraySize		Thrown when the array lenght is not 2 or 4
	 */
	public static int byteArrayToInt(byte[] b) throws InvalidByteArraySize{
		if(b.length == 4){
			return b[0] << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF);
		}
		else if(b.length == 2){
			return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xFF) << 8 | (b[1] & 0xFF);
		}
		throw new InvalidByteArraySize("b.length: " + b.length + " it should be 4 or 2");
	}
	/**
	 * Used to put an integer to a 4 byte format
	 * @param value		A int that needs to be converted
	 * @return			returns a byte-array representation of the value (in Big-endian).
	 */
	public static byte[] intToByteArray(int value) {
	    return new byte[] {(byte)(value >>> 24),(byte)(value >>> 16),(byte)(value >>> 8),(byte)value};
	}
}
