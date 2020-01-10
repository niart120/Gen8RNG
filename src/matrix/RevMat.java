package matrix;

public class RevMat {
	public long[] kernel;
	public long[] emat;
	public long constant;

	public long[] swaps;

	public int nos;

	RevMat(long[] kernel ,long[] emat,long[] swaps,long constant) {
		this.kernel = kernel;
		this.emat = emat;
		this.swaps = swaps;
		this.constant = constant;
		this.nos = 1<<(64-kernel.length);
	}

}
