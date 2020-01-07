package matrix;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class BitMat {
	private int h,w;
	private long[][] m;

	private BitMat(int height, int width) {
		this.h = height;
		this.w = width;

		m = new long[h][w];
		for(long[] warray:m) {
			Arrays.fill(warray, 0);
		}
	}

	public BitMat sliced(int strh, int endh,int strw,int endw) {
		int h = endh-strh;
		int w = endw-strw;
		BitMat sliced = new BitMat(h,w);

		IntStream.range(strh, endh).parallel().forEach(i->{
			for(int j=strw;j<endw;j++) {
				sliced.m[i-strh][j-strw] = m[i][j];
			}
		});
		return sliced;
	}

	public BitMat powers(int exp) {
		if(exp<0) throw new IllegalArgumentException();
		BitMat powered = copy(this);
		for(int i=1;i<exp;i++)powered = product(powered,this);

		return powered;
	}

	public BitMat copies() {
		return copy(this);
	}

	public long[] mat2bitvecs() {
		if(w>64)return new long[0];
		long[] bitvecs = new long[h];

		IntStream.range(0, h).parallel().forEach(i->{
			bitvecs[i] = 0;
			for(int j=0;j<w;j++) {
				bitvecs[i]<<=1;
				bitvecs[i] |= m[i][j]&1;
			}
		});
		return bitvecs;
	}

	public int h() {
		return this.h;
	}

	public int w() {
		return this.w;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0;i<h;i++) {
			sb.append("[");
			for(int j=0;j<w;j++) {
				sb.append(m[i][j]+" ");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]\n");

		}
		sb.append("]");

		return sb.toString();
	}

	public static BitMat zeros(int h, int w) {
		return new BitMat(h,w);
	}

	public static BitMat rotl(int size,int shift) {
		BitMat rotl = new BitMat(size,size);
		for(int i=0;i<size;i++) {
			rotl.m[(size+i-shift)%size][i] = 1;
		}
		return rotl;
	}

	public static BitMat shiftl(int size, int shift) {
		BitMat shiftl = new BitMat(size,size);
		for(int i=0;i<size-shift;i++) {
			shiftl.m[i][i+shift] = 1;
		}
		return shiftl;
	}

	public static BitMat shiftr(int size, int shift) {
		BitMat shiftr = new BitMat(size,size);
		for(int i=shift;i<size;i++) {
			shiftr.m[i][i-shift] = 1;
		}
		return shiftr;
	}

	public static BitMat identity(int size) {
		BitMat id = new BitMat(size,size);
		for(int i=0;i<size;i++) {
			id.m[i][i] = 1;
		}
		return id;
	}

	public static BitMat block(BitMat[][] mats) {
		BitMat[] raws = new BitMat[mats.length];
		for(int i=0;i<mats.length;i++) {
			raws[i] = blockc(mats[i]);
		}

		BitMat block = blockr(raws);
		return block;
	}

	public static BitMat blockc(BitMat... mats){

		int baseh = mats[0].h;
		int basew = 0;
		for(BitMat mat:mats) {
			if (baseh!=mat.h) {
				String m = "Matrix height is illegal";
				throw new IllegalArgumentException(m);
			}
			basew+=mat.w;
		}

		BitMat block = new BitMat(baseh,basew);

		block.m = Stream.of(mats).map(m->m.m).reduce((long[][] l,long[][] r)->{
			for(int i=0;i<baseh;i++) {
				l[i] = LongStream.concat(Arrays.stream(l[i]), Arrays.stream(r[i])).toArray();
			}
			return l;
		}).get();

		return block;
	}

	public static BitMat blockr(BitMat... mats){
		int baseh = 0;
		int basew = mats[0].w;
		for(BitMat mat:mats) {
			if (basew!=mat.w) {
				String m = "Matrix width is illegal";
				throw new IllegalArgumentException(m);
			}
			baseh+=mat.h;
		}

		BitMat block = new BitMat(baseh,basew);

		int counter = 0;
		for(int i=0;i<mats.length;i++) {
			for(int j=0;j<mats[i].h;j++) {
				block.m[counter++] = mats[i].m[j];
			}
		}

		return block;
	}

	public static BitMat add(BitMat a, BitMat b){
		if(a.w!=b.w||a.h!=b.h) {
			String m = "Matrix size is illegal";
			throw new IllegalArgumentException(m);
		}

		long[][] a_m = a.m;
		long[][] b_m = b.m;

		BitMat mat = new BitMat(a.h,a.w);

		IntStream.range(0, a.h).parallel().forEach(i->{
			for(int j=0;j<a.w;j++) {
				mat.m[i][j] = a_m[i][j]^b_m[i][j];
			}
		});

		return mat;
	}

	public static BitMat add(BitMat... mats) {
		BitMat mat = Stream.of(mats).reduce((l,r)->add(l,r)).get();
		return mat;
	}

	public static BitMat product(BitMat a, BitMat b){
		if(a.w!=b.h) {
			String m = "Matrix size is illegal";
			throw new IllegalArgumentException(m);
		}

		long[][] a_m = a.m;
		long[][] b_m = b.m;

		BitMat mat = new BitMat(a.h,b.w);
		IntStream.range(0,a.h).parallel().forEach(i->{
			for(int j=0;j<b.w;j++) {
				for(int k=0;k<b.h;k++) {
					mat.m[i][j]^=a_m[i][k]&b_m[k][j];
				}
			}
		});
		return mat;
	}

	public static BitMat slice(BitMat mat, int strh, int endh,int strw,int endw) {
		int h = endh-strh;
		int w = endw-strw;
		BitMat sliced = new BitMat(h,w);

		IntStream.range(strh, endh).parallel().forEach(i->{
			for(int j=strw;j<endw;j++) {
				sliced.m[i-strh][j-strw] = mat.m[i][j];
			}
		});
		return sliced;
	}

	public static BitMat copy(BitMat mat) {
		BitMat copied = new BitMat(mat.h,mat.w);

		IntStream.range(0, mat.h).parallel().forEach(i->{
			for(int j=0;j<mat.w;j++) {
				copied.m[i][j] = mat.m[i][j];
			}
		});

		return copied;
	}


}
