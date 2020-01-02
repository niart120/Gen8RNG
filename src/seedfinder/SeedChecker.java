package seedfinder;

public class SeedChecker {
	Pokemon second;
	Pokemon third;
	GeneratorBuilder gb;

	public SeedChecker(Pokemon second, Pokemon third, GeneratorBuilder gb) {
		this.second = second;
		this.third = third;
		this.gb = gb;
	}

	public boolean checkSeed(long seed) {
		Generator g = gb.build(seed);
		Pokemon secondCheck = g.genCheck();
		if(secondCheck.equals(second)) {
			Pokemon thirdCheck = g.genCheck();
			return thirdCheck.equals(third);
		}
		return false;
	}
}
