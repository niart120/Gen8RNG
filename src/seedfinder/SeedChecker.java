package seedfinder;

public class SeedChecker {
	Pokemon second;
	Pokemon third;
	Pokemon forth;
	GeneratorBuilder gb1,gb2,gb3;


	public SeedChecker(Pokemon second, Pokemon third, Pokemon forth, GeneratorBuilder gb1, GeneratorBuilder gb2, GeneratorBuilder gb3) {
		this.second = second;
		this.third = third;
		this.forth = forth;
		this.gb1 = gb1;
		this.gb2 = gb2;
		this.gb3 = gb3;
	}
	public SeedChecker(Pokemon second, Pokemon third, Pokemon forth, GeneratorBuilder gb1, GeneratorBuilder gb2) {
		this.second = second;
		this.third = third;
		this.forth = forth;
		this.gb1 = gb1;
		this.gb2 = gb2;

	}

	public boolean checkSeed(long seed) {
		Generator g1 = gb1.build(seed);

		Pokemon secondCheck = g1.genCheck();
		if(secondCheck.equals(second)) {
			Generator g2 = gb2.build(seed+0x82A2B175229D6A5BL);
			Pokemon thirdCheck = g2.genCheck();
			if(thirdCheck.equals(third)) {
				Generator g3 = gb3.build((seed+0x82A2B175229D6A5BL)+0x82A2B175229D6A5BL);
				Pokemon forthCheck = g3.genCheck();
				return forthCheck.equals(forth);
//				return true;
			}
		}
		return false;
	}
}
