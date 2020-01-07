package seedfinder;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class SeedFinder {
	private SeedCollector collector;
	private SeedChecker checker;

	public SeedFinder(SeedCollector collector, SeedChecker checker) {
		this.collector = collector;
		this.checker = checker;
	}

	public ArrayList<Long> find() {
		ArrayList<Long> extracted = new ArrayList<Long>();
		IntStream.range(0,1<<28).parallel().forEach(i->{
//			long start = System.nanoTime();
			long[] seeds = collector.getSeed(i);
			//remove distinct
			for(long seed:seeds) {
				if(checker.checkSeed(seed))extracted.add(seed);
			}
//			long end = System.nanoTime();
//			System.out.println((end - start)/1000000.0);
		});



		return extracted;
	}

}
