package net.epopy.network.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DataBuffer {

	public static final int HALF_BYTE = 0xf;
	public static final int ONE_BYTE = 0xff;
	public static final int TWO_BYTE = 0xffff;
	public static final int THREE_BYTE = 0xffffff;
	public static final int FOUR_BYTE = 0xffffffff;

	private int writeID;
	private int readID;
	private byte[] data;
	private final int dataSize;

	public DataBuffer() {
		this(2048);
	}

	public DataBuffer(final byte[] data) {
		dataSize = 2048;
		this.data = data;
		writeID = 0;
		readID = 0;
	}

	public DataBuffer(final int size) {
		dataSize = size;
		data = new byte[dataSize];
		writeID = 0;
		readID = 0;
	}

	public void flip() {
		if (writeID != 0) {
			byte[] nData = new byte[writeID];
			for (int i = 0; i < writeID; i++) {
				nData[i] = data[i];
			}
			data = nData;
		} else if (readID != 0) {
			byte[] nData = new byte[readID];
			for (int i = 0; i < readID; i++)
				nData[i] = data[i];
			data = nData;
		}
	}

	public void clear() {
		if (data != null) {
			for (int i = 0; i < data.length; i++)
				data[i] = 0;
		}
		data = new byte[dataSize];
		writeID = 0;
		readID = 0;
	}

	public void setData(final byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public int size() {
		return getData().length;
	}

	public void put(final byte value) {
		if (writeID >= data.length) {
			System.err.println("Write Overflow..." + writeID + "\n\tMax capacity: " + data.length);
			return;
		}
		data[writeID] = value;
		writeID++;
	}

	public void put(final byte... values) {
		for (byte value : values)
			put(value);
	}

	public byte getByte() {
		if (readID >= data.length) {
			System.err.println("Read Overflow..." + readID + "\n\tMax capacity: " + data.length);
			return -1;
		}
		return data[readID++];
	}

	public void put(final short w) {
		put((byte) (w >> 8));
		put((byte) w);
	}

	public short getShort() {
		return ByteBuffer.wrap(new byte[] {
				getByte(), getByte()
		}).getShort();
	}

	public void put(final int w) {
		put((byte) (w >> 24));
		put((byte) (w >> 16));
		put((byte) (w >> 8));
		put((byte) w);
	}

	public int getInt() {
		return ByteBuffer.wrap(new byte[] {
				getByte(), getByte(), getByte(), getByte()
		}).getInt();
	}

	public void put(final long w) {
		put((byte) (w >> 56));
		put((byte) (w >> 48));
		put((byte) (w >> 40));
		put((byte) (w >> 32));
		put((byte) (w >> 24));
		put((byte) (w >> 16));
		put((byte) (w >> 8));
		put((byte) w);
	}

	public long getLong() {
		return ByteBuffer.wrap(new byte[] {
				getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte()
		}).getLong();
	}

	public void put(final float w) {
		put(Float.floatToIntBits(w));
	}

	public float getFloat() {
		return ByteBuffer.wrap(new byte[] {
				getByte(), getByte(), getByte(), getByte()
		}).getFloat();
	}

	public void put(final double w) {
		put(Double.doubleToLongBits(w));
	}

	public double getDouble() {
		return ByteBuffer.wrap(new byte[] {
				getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte(), getByte()
		}).getDouble();
	}

	public DataBuffer put(final String w) {
		byte b[] = w.getBytes();
		put(b.length);
		put(b);
		return this;
	}

	public String getString() {
		byte[] b = new byte[getInt()];
		for (int i = 0; i < b.length; i++)
			b[i] = getByte();
		return new String(b);
	}

	public void write(final String path) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			fos.write(getData());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean read(final String path) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			fis.read(data);
			fis.close();

			return true;
		} catch (IOException e) {
			return false;
		}
	}
}