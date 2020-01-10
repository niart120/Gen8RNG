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
	private long[] swaps;

	private long ecbit;

	public SeedCollector(RevMat rev, long ecbit, Pokemon star3, Pokemon star5){
		this.kernel = rev.kernel;
		this.emat = rev.emat;
		this.ivs = solveIvsPuzzle(star3,star5);
		this.vfixed = star3.getFixedPos();

		this.constant = rev.constant;
		this.nos = rev.nos;
		this.swaps = rev.swaps;
		this.ecbit = ecbit;
	}

	public SeedCollector(long ecbit, Pokemon observed, Pokemon puzzle) {
		this.ivs = solveIvsPuzzle(observed,puzzle);
		this.vfixed = observed.getFixedPos();
		this.ecbit = ecbit;
	}

	public long[] getSeed(long i) {
		long[] seeds = new long[nos*vfixed.length];

		int paramlen = Long.numberOfTrailingZeros(nos);

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
				t|=(long)fixedbit;

				for(int s=swaps.length-1;s>=0;s--) t = bitSwap(t,nos<<(swaps.length-s-1),swaps[s]);
				seeds[j<<paramlen|fixedbit]=t;
			}
//			System.out.println(vfixed[j]);
//			System.out.println(seeds[j<<paramlen]);
		}
		return seeds;
	}

	private long getObservedBit(long i,long lastfixed) {
		long lbit = i;

    	long ubit = this.ecbit<<3;
    	ubit |= (lastfixed-i)&7L;
    	i>>>=3;

    	for(int j=0;j<ivs.length;j++) {
    		ubit<<=5;
    		ubit |= (ivs[j]-i)&31L;
    		i>>>=5;
    	}
    	long observed = ubit<<28|lbit;
//		System.out.println(String.format("%64s",Long.toBinaryString(observed)).replace(' ', '0'));
    	return observed;

	}


	private long[] solveIvsPuzzle(Pokemon star3, Pokemon star5) {

		ArrayList<Long> unfixed3 = (ArrayList<Long>) Arrays.stream(star3.getIvs()).boxed().filter(i->i!=31).collect(Collectors.toList());
		ArrayList<Long> unfixed5 = (ArrayList<Long>) Arrays.stream(star5.getIvs()).boxed().filter(i->i!=31).collect(Collectors.toList());

		long[] ivs3 = star3.getIvs();
		long[] ivs5 = star5.getIvs();

		if(unfixed3.size()==3) {
			int checkpos = (int)(long)unfixed3.get(2)%8;
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

	private long bitSwap(long bit, long srcpos, long tgtpos) {
		int dist = Long.numberOfTrailingZeros(srcpos)-Long.numberOfTrailingZeros(tgtpos);
		long mask = ~(srcpos|tgtpos);
		long tmp = (srcpos&bit)>>>dist|(tgtpos&bit)<<dist;
		bit &= mask;
		return bit|tmp;
	}


	@SuppressWarnings("unused")
	private long bitSwap(long bit, long dist, long srcpos, long tgtpos) {
		long mask = ~(srcpos|tgtpos);
		long tmp = (srcpos&bit)>>>dist|(tgtpos&bit)<<dist;
		bit &= mask;
		return bit|tmp;
	}

	public void updateRevMat(RevMat rev) {
		this.kernel = rev.kernel;
		this.emat = rev.emat;
		this.constant = rev.constant;
		this.nos = rev.nos;
		this.swaps = rev.swaps;

	}

}
