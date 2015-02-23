package simuframe;

/**
 * @author m-morita
 * This class generates random values using MersenneTwister
 */
public class RandomGenerator {
	private MersenneTwisterFast gen;
	
	public RandomGenerator(long seed){
		gen = new MersenneTwisterFast(seed);
	}
	
	/**
	 * @param n
	 * @return random integer value [0,n).
	 */
	public int nextInt(int n){
		return gen.nextInt(n);
	}
	
	/**
	 * @return random double value [0.0,1.0).
	 */
	public double nextDouble(){
		return gen.nextDouble();
	}
	
	/**
	 * @param mean
	 * @return random value (0,positive infinity), following the exponential distribution
	 */
	public double nextExp(double mean){
		return (-1.0/mean) *( Math.log(1.0-nextDouble()) );
	}

}
