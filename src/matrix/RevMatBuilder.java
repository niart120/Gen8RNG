package matrix;

public class RevMatBuilder {
	final BitMat t;

	public RevMatBuilder() {
		BitMat tl = BitMat.add(BitMat.rotl(64,24),BitMat.identity(64),BitMat.shiftl(64, 16));
		BitMat tr = BitMat.add(BitMat.identity(64),BitMat.shiftl(64,16));
		BitMat bl = BitMat.rotl(64,37);
		BitMat br = BitMat.rotl(64, 37);

		t = BitMat.block(new BitMat[][] {{tl,tr},{bl,br}});
	}

	public RevMat build(int ivsReroll) {
		BitMat s = calcS(ivsReroll);
		BitMat s_left = s.sliced(0, s.h(), 0, s.w()/2);
		BitMat s_right = s.sliced(0, s.h(), s.w()/2, s.w());

		long[] param = s_left.mat2bitvecs();
		long[] emat = getInverse(param);
		long swapped = Long.highestOneBit(param[param.length-1]);
		getParametic(param);

		long[] constvec = s_right.mat2bitvecs();

		long constant = getConstant(constvec);

		return new RevMat(param,emat,swapped,constant);
	}

	//Attention:This method has side effect.
	private long[] getInverse(long[] bitvecs) {
		long[] emat = new long[bitvecs.length];
		for(int i=0;i<bitvecs.length;i++) {
			emat[i] = 1<<bitvecs.length-i-1;
		}
		int pivot = 0;
		for (int i=0;i<bitvecs.length;i++) {
			boolean isfound = false;
	        for (int j=i;j<bitvecs.length;j++) {
	            if (isfound) {
	                long check = 1<<(63-i);
	                if ((bitvecs[j]&check)==check) {
	                    bitvecs[j] ^= bitvecs[pivot];
	                    emat[j] ^= emat[pivot];
	                }
	            }else {
	                long check = 1<<(63-i);
	                if ((bitvecs[j]&check)==check){
	                    isfound = true;
	                    swap(bitvecs,j,pivot);
	                    swap(emat,j,pivot);
	                }
	            }
	        }
	        if (isfound)pivot++;
		}

	    for (int i=bitvecs.length-1;0<=i;i--) {
	        long check = Long.highestOneBit(bitvecs[i]);
	        for (int j=i-1;0<=j;j--) {
	            if ((bitvecs[j]&check)==check) {
	                bitvecs[j] ^= bitvecs[i];
	                emat[j] ^= emat[i];
	            }
	        }
	    }

	    return emat;

	}

	private void getParametic(long[] eliminated) {
		long srcpvt = Long.highestOneBit(eliminated.length-1);
		long tgtpvt = 1<<(62-eliminated.length);
		long mask = ~(srcpvt|tgtpvt);
		long pmask = (tgtpvt<<1)-1;

		int dist = 62-eliminated.length - Long.numberOfTrailingZeros(srcpvt);
		if(dist==0)return;
		for(int i=0;i<eliminated.length;i++) {
			long srctmp = (eliminated[i]&srcpvt)<<dist;
			long tgttmp = (eliminated[i]&tgtpvt)>>>dist;

			eliminated[i]&=mask;
			eliminated[i]|=srctmp|tgttmp;
			eliminated[i]&=pmask;
		}

		return;
	}

	private long getConstant(long[] constvec) {
		long seed1 = 0x82A2B175229D6A5BL;
		long constant = 0;
		for(int i=0;i<constvec.length;i++) {
			long inner = constvec[i]&seed1;
			constant<<=1;
			constant |= isBitOdd(inner);
		}
		return constant;
	}

	private void swap(long[] arr, int i, int j) {
		long tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}


	private BitMat calcS(int ivsReroll) {
		BitMat s_upper = BitMat.zeros(0, 128);
		BitMat s_lower = BitMat.zeros(0, 128);
		BitMat t_ = t.copies();

		//EC
		s_upper = BitMat.blockr(s_upper,calcSrows(63,1,t_));
		s_lower = BitMat.blockr(s_lower,calcSrows(127,1,t_));

		//PID,SID,Vfix (2pos);only consume
		t_ = BitMat.product(t_, t.powers(5));
		if(ivsReroll>0)t_ = BitMat.product(t_, t.powers(ivsReroll));

		//Vfix(3rdpos)
		s_upper = BitMat.blockr(s_upper,calcSrows(61,3,t_));
		s_lower = BitMat.blockr(s_lower,calcSrows(125,3,t_));
		t_ = BitMat.product(t_, t);

		//ivs
		for(int i=0;i<5;i++) {
			s_upper = BitMat.blockr(s_upper,calcSrows(59,5,t_));
			s_lower = BitMat.blockr(s_lower,calcSrows(123,5,t_));
			t_ = BitMat.product(t_, t);
		}

		BitMat s = BitMat.blockr(s_upper,s_lower);
		return s;

	}

	private BitMat calcSrows(int axis, int length, BitMat trans) {
		return trans.sliced(axis, axis+length, 0, 128);
	}

	private long isBitOdd(long l) {
		l ^= l>>>32;
		l ^= l>>>16;
		l ^= l>>>8;
		l ^= l>>>4;
	    l ^= l>>>2;
	    l ^= l>>>1;
		return l;
	}

}
