package com.example.demo.utils;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectId implements Comparable<ObjectId>, Serializable {
	private static final long serialVersionUID = -4415279469780082174L;

	final int _time;
	final int _machine;
	final int _inc;
	boolean _new;
	private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());
	private static final int _genmachine;

	static {
		try {
			int machinePiece;
			try {
				StringBuilder sb = new StringBuilder();
				Enumeration e = NetworkInterface.getNetworkInterfaces();

				while (e.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) e.nextElement();
					sb.append(ni.toString());
				}

				machinePiece = sb.toString().hashCode() << 16;
			} catch (Throwable var7) {
				machinePiece = (new Random()).nextInt() << 16;
			}

			int processId = (new Random()).nextInt();

			try {
				processId = ManagementFactory.getRuntimeMXBean().getName().hashCode();
			} catch (Throwable var6) {
				;
			}

			ClassLoader loader = ObjectId.class.getClassLoader();
			int loaderId = loader != null ? System.identityHashCode(loader) : 0;
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toHexString(processId));
			sb.append(Integer.toHexString(loaderId));
			int processPiece = sb.toString().hashCode() & '\uffff';
			_genmachine = machinePiece | processPiece;
		} catch (Exception var8) {
			throw new RuntimeException(var8);
		}
	}

	public static ObjectId get() {
		return new ObjectId();
	}

	public static boolean isValid(String s) {
		if (s == null) {
			return false;
		} else {
			int len = s.length();
			if (len != 24) {
				return false;
			} else {
				for (int i = 0; i < len; ++i) {
					char c = s.charAt(i);
					if ((c < 48 || c > 57) && (c < 97 || c > 102) && (c < 65 || c > 70)) {
						return false;
					}
				}

				return true;
			}
		}
	}

	public static ObjectId massageToObjectId(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof ObjectId) {
			return (ObjectId) o;
		} else {
			if (o instanceof String) {
				String s = o.toString();
				if (isValid(s)) {
					return new ObjectId(s);
				}
			}

			return null;
		}
	}

	public ObjectId(Date time) {
		this(time, _genmachine, _nextInc.getAndIncrement());
	}

	public ObjectId(Date time, int inc) {
		this(time, _genmachine, inc);
	}

	public ObjectId(Date time, int machine, int inc) {
		this._time = (int) (time.getTime() / 1000L);
		this._machine = machine;
		this._inc = inc;
		this._new = false;
	}

	public ObjectId(String s) {
		this(s, false);
	}

	public ObjectId(String s, boolean babble) {
		if (!isValid(s)) {
			throw new IllegalArgumentException("invalid ObjectId [" + s + "]");
		} else {
			if (babble) {
				s = babbleToMongod(s);
			}

			byte[] b = new byte[12];

			for (int i = 0; i < b.length; ++i) {
				b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
			}

			ByteBuffer bb = ByteBuffer.wrap(b);
			this._time = bb.getInt();
			this._machine = bb.getInt();
			this._inc = bb.getInt();
			this._new = false;
		}
	}

	public ObjectId(byte[] b) {
		if (b.length != 12) {
			throw new IllegalArgumentException("need 12 bytes");
		} else {
			ByteBuffer bb = ByteBuffer.wrap(b);
			this._time = bb.getInt();
			this._machine = bb.getInt();
			this._inc = bb.getInt();
			this._new = false;
		}
	}

	public ObjectId(int time, int machine, int inc) {
		this._time = time;
		this._machine = machine;
		this._inc = inc;
		this._new = false;
	}

	public ObjectId() {
		this._time = (int) (System.currentTimeMillis() / 1000L);
		this._machine = _genmachine;
		this._inc = _nextInc.getAndIncrement();
		this._new = true;
	}

	public int hashCode() {
		int x = this._time;
		x += this._machine * 111;
		x += this._inc * 17;
		return x;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else {
			ObjectId other = massageToObjectId(o);
			return other == null ? false
					: this._time == other._time && this._machine == other._machine && this._inc == other._inc;
		}
	}

	public String toStringBabble() {
		return babbleToMongod(this.toStringMongod());
	}

	public String toStringMongod() {
		byte[] b = this.toByteArray();
		StringBuilder buf = new StringBuilder(24);

		for (int i = 0; i < b.length; ++i) {
			int x = b[i] & 255;
			String s = Integer.toHexString(x);
			if (s.length() == 1) {
				buf.append("0");
			}

			buf.append(s);
		}

		return buf.toString();
	}

	public byte[] toByteArray() {
		byte[] b = new byte[12];
		ByteBuffer bb = ByteBuffer.wrap(b);
		bb.putInt(this._time);
		bb.putInt(this._machine);
		bb.putInt(this._inc);
		return b;
	}

	static String _pos(String s, int p) {
		return s.substring(p * 2, p * 2 + 2);
	}

	public static String babbleToMongod(String b) {
		if (!isValid(b)) {
			throw new IllegalArgumentException("invalid object id: " + b);
		} else {
			StringBuilder buf = new StringBuilder(24);

			int i;
			for (i = 7; i >= 0; --i) {
				buf.append(_pos(b, i));
			}

			for (i = 11; i >= 8; --i) {
				buf.append(_pos(b, i));
			}

			return buf.toString();
		}
	}

	public String toString() {
		return this.toStringMongod();
	}

	int _compareUnsigned(int i, int j) {
		long li = 4294967295L;
		li &= (long) i;
		long lj = 4294967295L;
		lj &= (long) j;
		long diff = li - lj;
		return diff < -2147483648L ? -2147483648 : (diff > 2147483647L ? 2147483647 : (int) diff);
	}

	public int compareTo(ObjectId id) {
		if (id == null) {
			return -1;
		} else {
			int x = this._compareUnsigned(this._time, id._time);
			if (x != 0) {
				return x;
			} else {
				x = this._compareUnsigned(this._machine, id._machine);
				return x != 0 ? x : this._compareUnsigned(this._inc, id._inc);
			}
		}
	}

	public int getMachine() {
		return this._machine;
	}

	public long getTime() {
		return (long) this._time * 1000L;
	}

	public int getTimeSecond() {
		return this._time;
	}

	public int getInc() {
		return this._inc;
	}

	public int _time() {
		return this._time;
	}

	public int _machine() {
		return this._machine;
	}

	public int _inc() {
		return this._inc;
	}

	public boolean isNew() {
		return this._new;
	}

	public void notNew() {
		this._new = false;
	}

	public static int getGenMachineId() {
		return _genmachine;
	}

	public static int getCurrentInc() {
		return _nextInc.get();
	}

	public static int _flip(int x) {
		int z = 0;
		z = z | x << 24 & -16777216;
		z |= x << 8 & 16711680;
		z |= x >> 8 & '\uff00';
		z |= x >> 24 & 255;
		return z;
	}

	public static void main(String[] args) {
		long num = 50L;
		long start = System.currentTimeMillis();

		long i;
		for (i = 0L; i < num; ++i) {
			ObjectId id = new ObjectId();
			System.out.println(id);
		}

		i = System.currentTimeMillis();
		System.out.println(num / 1000L / (i - start) + "M ObjectId gen/sec");
	}
}
