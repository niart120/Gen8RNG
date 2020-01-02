package seedfinder;

public class GeneratorBuilder {
	private int fixed;
	private boolean hasHidden;
	private boolean isSameAbility;
	private int genderType;
	private int genderRatio;

	public GeneratorBuilder(int fixed, boolean hasHidden, boolean isSameAbility, int genderType, int genderRatio) {
		this.fixed = fixed;
		this.hasHidden = hasHidden;
		this.isSameAbility = isSameAbility;
		this.genderType = genderType;
		this.genderRatio = genderRatio;
	}

	public Generator build(long seed) {
		return new Generator(fixed,hasHidden,isSameAbility,genderType,genderRatio,new Xoroshiro(seed));
	}

	public GeneratorBuilder setFixed(int fixed) {
		this.fixed = fixed;
		return this;
	}

	public GeneratorBuilder setHasHidden(boolean hasHidden) {
		this.hasHidden = hasHidden;
		return this;
	}

	public GeneratorBuilder setSameAbility(boolean isSameAbility) {
		this.isSameAbility = isSameAbility;
		return this;
	}

	public GeneratorBuilder setGenderType(int genderType) {
		this.genderType = genderType;
		return this;
	}

	public GeneratorBuilder setGenderRatio(int genderRatio) {
		this.genderRatio = genderRatio;
		return this;
	}
}
