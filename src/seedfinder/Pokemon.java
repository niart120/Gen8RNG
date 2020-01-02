package seedfinder;

import java.util.Arrays;

public class Pokemon {
	private long[] ivs;
	private long ability;
	private long gender;
	private long nature;

	public Pokemon(long[] ivs, long ability, long gender, long nature) {
		this.ivs = ivs;
		this.ability = ability;
		this.gender = gender;
		this.nature = nature;
	}

	public Pokemon(long[] ivs, long ability, long nature) {
		this.ivs = ivs;
		this.ability = ability;
		this.nature = nature;
	}

	public Pokemon(long[] ivs, long nature) {
		this.ivs = ivs;
		this.nature = nature;
	}

	public Pokemon(long[] ivs) {
		this.ivs = ivs;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pokemon) {
			Pokemon p = (Pokemon)o;
			if (!Arrays.equals(p.ivs, this.ivs)) return false;
			return true;
//			if (p.ability!=this.ability) return false;
//			if (p.gender!=this.gender) return false;
//			return p.nature == this.nature;
		}
		return false;
	}

	public long[] getIvs() {
		return ivs;
	}

	public long[] getFixedPos() {
		int fixed = (int)Arrays.stream(ivs).filter(i->i==31).count();
		long[] pos = new long[fixed];
		for(int i=0,j=0;i<6;i++) {
			if(ivs[i]==31) pos[j++]=i;
		}
		return pos;
	}

	public long getNature() {
		return nature;
	}
}
