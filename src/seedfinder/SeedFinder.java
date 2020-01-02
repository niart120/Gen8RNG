package seedfinder;

import java.util.ArrayList;
import java.util.Arrays;
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
		IntStream.range(0,1<<29).parallel().forEach(i->{
			long[] seeds = collector.getSeed(i);
			//remove distinct
			seeds = Arrays.stream(seeds).distinct().toArray();
			for(long seed:seeds) {
				if(checker.checkSeed(seed))extracted.add(seed);
			}
		});



		return extracted;
	}

}
