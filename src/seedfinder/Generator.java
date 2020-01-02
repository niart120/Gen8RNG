package seedfinder;

import java.util.Arrays;

public class Generator {
	private final int fixed;
	private final boolean hasHidden;
	private final boolean isSameAbility;
	private final int genderType;
	private final int genderRatio;

	private final Xoroshiro xor;

	private long[] ivs = new long[6];

	private long ability;
	private int gender;
	private int nature;

	public Generator(int fixed, boolean hasHidden, boolean isSameAbility, int genderType, int genderRatio, Xoroshiro xor) {
		this.fixed = fixed;
		this.hasHidden = hasHidden;
		this.isSameAbility = isSameAbility;
		this.genderType = genderType;
		this.genderRatio = genderRatio;
		this.xor = xor;
	}

	public Pokemon genCheck() {
		xor.reset();
		xor.nextVoid(3);
		generateIVs();
		generateAbility();
		generateGender();
		generateNature();

		return new Pokemon(ivs,ability,gender,nature);
	}

	public Pokemon generate() {
		xor.reset();
		xor.nextVoid(3);
		generateIVs();
//		generateAbility();
//		generateGender();
//		generateNature();

		return new Pokemon(Arrays.copyOf(ivs, ivs.length),ability,gender,nature);
	}

	private void generateIVs() {
		Arrays.fill(ivs, -1);
		int i = 0;
		while (i < fixed) {
		    int stat = xor.nextInt(6);
		    if (ivs[stat] == -1) {
		        ivs[stat] = 31;
		        i += 1;
		    }
		}
		for (i=0;i<6;i++) {
		    if (ivs[i] == -1) ivs[i] = xor.nextInt(32);

		}
	}

	private void generateAbility() {
		if(hasHidden) {
			ability = xor.nextInt(3);
		}else {
			ability = xor.nextInt(2);
		}
		if(isSameAbility&&ability==1) {
			ability = 0;
			return;
		}
	}

	private void generateGender() {
		switch (genderType){
		case 0:
			switch(genderRatio) {
			case 255:
				gender = 2;
				break;
			case 254:
			    gender = 1;
			    break;
			case 0:
			    gender = 0;
			    break;
			default:
			    gender = xor.nextInt(253) + 1 < genderRatio ? 1:0;
			}
			break;

		case 1:
		    gender = 0;//male
		    break;
		case 2:
		    gender = 1;//female
		    break;
		case 3:
		    gender = 2;//genderless
		}
	}

	private void generateNature() {
		nature = xor.nextInt(25);
	}

}
