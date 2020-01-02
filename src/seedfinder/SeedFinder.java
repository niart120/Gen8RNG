package seedfinder;

import java.util.ArrayList;
import java.util.Arrays;

public class SeedFinder {
	private SeedCollector collector;
	private SeedChecker checker;

	public SeedFinder(SeedCollector collector, SeedChecker checker) {
		this.collector = collector;
		this.checker = checker;
	}

	public ArrayList<Long> find() {
		ArrayList<Long> extracted = new ArrayList<Long>();

		for(int b = 0;b<nos;b++) {
			int bit = b;
			Arrays.stream(observed.getFixedPos()).forEach(f->{
				long start = System.currentTimeMillis();
				long[] candidates = sc.getSeedCandidates(ecbit,f,bit,observed,puzzle);
				ArrayList<Long> e = checkSeed(candidates,second,third,gb);
				long end = System.currentTimeMillis();
				System.out.println((end - start)  + "ms");
				extracted.addAll(e);
			});
		}


		return extracted;
	}

}
