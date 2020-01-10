package seedfinder;

import java.util.ArrayList;

import info.Nature;
import matrix.RevMatBuilder;

public class Main {

	public static void main(String[] args) {
		calc();
	}

	private static void calc() {
		long[] obsivs = {30,31,1,31,26,31};
		Pokemon observed = new Pokemon(obsivs);

		long[] pzlivs = {1,31,31,31,8,31};
		Pokemon puzzle = new Pokemon(pzlivs);

		RevMatBuilder rmb = new RevMatBuilder();
		SeedCollector clctr = new SeedCollector(1L,observed,puzzle);

		long[] sndivs = {31,8,31,31,27,21};
		long sndabi = 0;
		long sndgen = 2;
		long sndntr = Nature.getByJa("まじめ");
		Pokemon second = new Pokemon(sndivs, sndabi, sndgen,sndntr);

		long[] trdivs = {16,15,31,31,31,31};
		long trdabi = 0;
		long trdgen = 2;
		long trdntr = Nature.getByJa("のんき");
		Pokemon third = new Pokemon(trdivs, trdabi, trdgen,trdntr);

		long[] forivs = {17,31,31,31,31,16};
		Pokemon forth = new Pokemon(forivs);

		GeneratorBuilder gb1 = new GeneratorBuilder(3, false, true, 3, 0);
		//GeneratorBuilder gb2 = new GeneratorBuilder(4,true,true,3,0);

		SeedChecker chkr = new SeedChecker(second,third,forth,gb1,gb1);

		SeedFinder sf = new SeedFinder(clctr, chkr);

		for(int i=1;i<6;i++) {
			clctr.updateRevMat(rmb.build(i));
			long start = System.currentTimeMillis();
			ArrayList<Long> seedlist = sf.find();
			long end = System.currentTimeMillis();
			System.out.println((end - start)/1000.0  + "sec");

			for(Long l: seedlist) {
				System.out.println(l);
			}
		}
	}

}
