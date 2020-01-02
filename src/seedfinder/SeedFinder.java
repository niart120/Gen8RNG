package seedfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import matrix.RevMat;

public class SeedFinder {
	private final SeedCollector sc;
	private long nos;

	public SeedFinder(RevMat rev) {
		sc = new SeedCollector(rev);
		nos = rev.nos;
	}

	public ArrayList<Long> find(long ecbit, Pokemon observed, Pokemon puzzle, Pokemon second, Pokemon third, GeneratorBuilder gb) {
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

	private ArrayList<Long> checkSeed(long[] candidates, Pokemon second, Pokemon third, GeneratorBuilder gb) {

		CopyOnWriteArrayList<Long> seeds = new CopyOnWriteArrayList<>();

		Arrays.stream(candidates).parallel().forEach(seed->{
			Generator g = gb.build(seed);
			Pokemon secondCheck = g.genCheck();
			if(secondCheck.equals(second)) {
				Pokemon thirdCheck = g.genCheck();
				if(thirdCheck.equals(third))seeds.add(seed);
			}
		});

		return (ArrayList<Long>) seeds.stream().collect(Collectors.toList());
	}

}
