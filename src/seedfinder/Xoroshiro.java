package seedfinder;

public class Xoroshiro {
	private long seed;

	private long seed0;
	private long seed1;

	public Xoroshiro(long seed) {
		this.seed = seed;
	}

	private long rotl(long x, int k) {
        return ((x << k) | (x >>> (64 - k))) & 0xFFFFFFFFFFFFFFFFL;
	}

	private long nextState(){
		long s0 = this.seed0;
		long s1 = this.seed1;

		long result = (s0 + s1) & 0xFFFFFFFFFFFFFFFFL;

        s1 ^= s0;
        seed0 = this.rotl(s0, 24) ^ s1 ^ ((s1 << 16) & 0xFFFFFFFFFFFFFFFFL);
        seed1 = this.rotl(s1, 37);

        return result;
	}

	private long nextPowerTwo(long num){
	    if ((num & (num - 1)) == 0)return num - 1;

	    long result = 1;
	    while (result < num)result <<= 1;
	    return result - 1;
	}

	public void nextVoid(int times) {
		for(int i=0;i<times;i++) {
			long s0 = this.seed0;
			long s1 = this.seed1;
			s1 ^= s0;
			seed0 = this.rotl(s0, 24) ^ s1 ^ ((s1 << 16) & 0xFFFFFFFFFFFFFFFFL);
			seed1 = this.rotl(s1, 37);
		}
	}


	public long next(long num) {
		long numTwo = nextPowerTwo(num);

	    long result = nextState() & numTwo;
	    while (result >= num)result = nextState() & numTwo;
	    return result;
	}

	public int nextInt(int num){
	    long numTwo = nextPowerTwo(num);

	    long result = nextState() & numTwo;
	    while (result >= num)result = nextState() & numTwo;
	    return (int) result;
	}

	public void reset() {
		seed += 0x82a2b175229d6a5bL;
		seed0 = seed;
		seed1 = 0x82A2B175229D6A5BL;
	}

}
