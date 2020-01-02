package seedfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import matrix.RevMat;

public class SeedCollector {
	long[] kernel;
	long[] emat;
	long constant;
	int nos;
	long swapped;

	public SeedCollector(RevMat rev){
		this.kernel = rev.kernel;
		this.emat = rev.emat;
		this.constant = rev.constant;
		this.nos = rev.nos;
		this.swapped = rev.swapped;
	}

	private long[] convObs2Bits(long ecbit, long lastfixed, Pokemon star3, Pokemon star5) {

		long[] ivs = solveIvsPuzzle(star3,star5);
	    long[] bits = new long[(1<<29)];

    	IntStream.range(0,1<<29).parallel().forEach(
    			i->{
	    		long ubit = i;
		    	long lbit = 0;
		    	lbit = i^ecbit&1L;
		    	lbit<<=3;
		    	lbit |= (lastfixed-ubit)&7L;
		    	for(int j=0;j<ivs.length;j++) {
		    		lbit<<=5;
		    		lbit |= (ivs[j]-ubit)&31L;
		    	}
		    	bits[i] = ubit<<29|lbit;

    			});

	    return bits;
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


	public long[] getSeedCandidates(long ecbit, long lastfixed, long b, Pokemon star3, Pokemon star5) {
		long[] seeds = new long[1<<29];
		long[] observed = convObs2Bits(ecbit,lastfixed,star3,star5);

		int dist = Long.numberOfTrailingZeros(nos)-Long.numberOfTrailingZeros(swapped);
		int paramlen = Long.numberOfTrailingZeros(nos);

		IntStream.range(0, seeds.length).parallel().forEach(
			j->{
				long t = 0;
				long c = observed[j]^constant;

				for(int i=0;i<kernel.length;i++) {
					t<<=1;
					t |= Long.bitCount(kernel[i]&b^emat[i]&c)&1L;
				}
				t<<=paramlen;
				t|=b;

				t = bitSwap(t,dist,nos,swapped);
				seeds[j]=t;
			});

		return seeds;
	}

	private long bitSwap(long bit, long dist, long srcpos, long tgtpos) {
		long mask = ~(srcpos|tgtpos);
		long tmp = (srcpos&bit)>>>dist|(tgtpos&bit)<<dist;
		bit &= mask;
		return bit|tmp;
	}

}
