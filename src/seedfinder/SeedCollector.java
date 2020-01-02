package seedfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import matrix.RevMat;

public class SeedCollector {
	private long[] kernel;
	private long[] emat;
	private long[] ivs;
	private long[] vfixed;

	private long constant;
	private int nos;
	private long swapped;

	private long ecbit;

	public SeedCollector(RevMat rev, long ecbit, Pokemon star3, Pokemon star5){
		this.kernel = rev.kernel;
		this.emat = rev.emat;
		this.ivs = solveIvsPuzzle(star3,star5);
		this.vfixed = star3.getFixedPos();

		this.constant = rev.constant;
		this.nos = rev.nos;
		this.swapped = rev.swapped;
		this.ecbit = ecbit;
	}

	public long[] getSeed(long i) {
		long[] seeds = new long[nos*vfixed.length];

		int paramlen = Long.numberOfTrailingZeros(nos);
		int dist = Long.numberOfTrailingZeros(nos)-Long.numberOfTrailingZeros(swapped);
		for(int j=0;j<vfixed.length;j++) {
			long observed = getObservedBit(i,vfixed[j]);
			long c = observed^constant;
			for(int fixedbit=0;fixedbit<nos;fixedbit++) {
				long t = 0;
				for(int k=0;k<kernel.length;k++) {
					t<<=1;
					t |= Long.bitCount(kernel[k]&fixedbit^emat[k]&c)&1L;
				}
				t<<=paramlen;
				t|=fixedbit;

				t = bitSwap(t,dist,nos,swapped);
				seeds[j<<paramlen|fixedbit]=t;
			}
		}
		return seeds;
	}

	private long getObservedBit(long i,long lastfixed) {
		long ubit = i;
    	long lbit = 0;
    	lbit = ubit^this.ecbit&1L;
    	ubit>>>=1;
    	lbit<<=3;
    	lbit |= (lastfixed-ubit)&7L;
    	for(int j=0;j<ivs.length;j++) {
    		ubit>>>=5;
    		lbit<<=5;
    		lbit |= (this.ivs[j]-ubit)&31L;
    	}
    	return i<<29|lbit;

	}


	private long[] solveIvsPuzzle(Pokemon star3, Pokemon star5) {

		ArrayList<Long> unfixed3 = (ArrayList<Long>) Arrays.stream(star3.getIvs()).boxed().filter(i->i!=31).collect(Collectors.toList());
		ArrayList<Long> unfixed5 = (ArrayList<Long>) Arrays.stream(star5.getIvs()).boxed().filter(i->i!=31).collect(Collectors.toList());

		long[] ivs3 = star3.getIvs();
		long[] ivs5 = star5.getIvs();

		if(unfixed3.size()==3) {
			if(unfixed5.stream().anyMatch(i->unfixed3.contains(i)))return new long[] {};
			int checkpos = (int)(long)unfixed3.get(2);
			if(ivs3[checkpos]==31||ivs5[checkpos]!=31)return new long[] {};

			unfixed3.addAll(unfixed5);
			long[] result = new long[unfixed3.size()];
			for(int i=0;i<unfixed3.size();i++) {
				result[i] = unfixed3.get(i);
			}
			return result;
		}

		return new long[] {};
	}


	private long bitSwap(long bit, long dist, long srcpos, long tgtpos) {
		long mask = ~(srcpos|tgtpos);
		long tmp = (srcpos&bit)>>>dist|(tgtpos&bit)<<dist;
		bit &= mask;
		return bit|tmp;
	}

}
