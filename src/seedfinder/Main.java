package seedfinder;

import java.util.ArrayList;

import info.Nature;
import matrix.RevMat;
import matrix.RevMatBuilder;

public class Main {

	public static void main(String[] args) {

		long[] obsivs = {31,6,7,31,31,5};
		Pokemon observed = new Pokemon(obsivs);

		long[] pzlivs = {31,29,5,31,31,31};
		Pokemon puzzle = new Pokemon(pzlivs);

		RevMatBuilder rmb = new RevMatBuilder();
		RevMat rm = rmb.build(0);
		SeedCollector clctr = new SeedCollector(rm,1,observed,puzzle);

		long[] sndivs = {18,31,31,31,14,31};
		long sndabi = 0;
		long sndgen = 2;
		long sndntr = Nature.getByJa("ようき");
		Pokemon second = new Pokemon(sndivs, sndabi, sndgen,sndntr);

		long[] trdivs = {10,0,31,31,9,31};
		long trdabi = 1;
		long trdgen = 2;
		long trdntr = Nature.getByJa("れいせい");
		Pokemon third = new Pokemon(trdivs, trdabi, trdgen,trdntr);

		GeneratorBuilder gb = new GeneratorBuilder(3, true, true, 3, 0);

		SeedChecker chkr = new SeedChecker(second,third,gb);

		SeedFinder sf = new SeedFinder(clctr, chkr);


		long start = System.currentTimeMillis();
		ArrayList<Long> seedlist = sf.find();
		long end = System.currentTimeMillis();
		System.out.println((end - start)/1000.0  + "sec");

		for(Long l: seedlist) {
			System.out.println(l);
		}


	}

}
