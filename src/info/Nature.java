package info;

import java.util.ArrayList;

public enum Nature {
	Hardy(0,"がんばりや"),Lonely(1,"さみしがり"),Brave(2,"ゆうかん"),Adamant(3,"いじっぱり"),Naughty(4,"やんちゃ"),Bold(5,"ずぶとい"),
	Docile(6,"すなお"),Relaxed(7,"のんき"),Impish(8,"わんぱく"),Lax(9,"のうてんき"),Timid(10,"おくびょう"),Hasty(11,"せっかち"),
	Serious(12,"まじめ"),Jolly(13,"ようき"),Naive(14,"むじゃき"),Modest(15,"ひかえめ"),Mild(16,"おっとり"),Quiet(17,"れいせい"),
	Bashful(18,"てれや"),Rash(19,"うっかりや"),Calm(20,"おだやか"),Gentle(21,"おとなしい"),Sassy(22,"なまいき"),Careful(23,"しんちょう"),Quirky(24,"きまぐれ");

	private final long nature;
	private final String ja;

	private Nature(int t,String ja) {
		this.nature= t;
		this.ja = ja;
	}

	private static final ArrayList<Nature> natures = new ArrayList<Nature>();

	static {
		for(Nature nature:Nature.values())natures.add(nature);
	}

	public static long getByJa(String name) {
		return natures.stream().filter(x->x.ja.equals(name)).findFirst().get().getNature();
	}

	public long getNature() {
		return nature;
	}
}

