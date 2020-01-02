package matrix;

public class RevMat {
	public long[] kernel;
	public long[] emat;
	public long constant;

	public long swapped;

	public int nos;

	RevMat(long[] kernel ,long[] emat,long swapped,long constant) {
		this.kernel = kernel;
		this.emat = emat;
		this.swapped = swapped;
		this.constant = constant;
		this.nos = 1<<(64-kernel.length);
	}

}
