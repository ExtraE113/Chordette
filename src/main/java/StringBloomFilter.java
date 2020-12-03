import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

// based off https://llimllib.github.io/bloomfilter-tutorial/
public class StringBloomFilter {
	BitSet filter; //todo use a bitset?
	int m, k;

	public StringBloomFilter(int m, int k) {

		this.m = m; //(1.37MiB) # of bits in filter
		this.k = k; //number of hash functions to use

		filter = new BitSet();
	}

	public StringBloomFilter() {
		//grabbing values for now from here
		//todo calculate dynamically
		//https://hur.st/bloomfilter/?n=24189467&p=10000&m=&k=
		// n = 24189467
		// p = 0.000100135 (1 in 9987)
		// m = 463714907 (55.28MiB)
		// k = 13
		this(463714907, 13);
	}

	public StringBloomFilter(String s) {
		String filterString = s.split("filter=")[1].split("\n")[0];
		int m = Integer.parseInt(s.split("m=")[1].split("\n")[0]);
		int k = Integer.parseInt(s.split("k=")[1].split("\n")[0]);


		this.m = m; //(1.37MiB) # of bits in filter
		this.k = k; //number of hash functions to use

		filter = new BitSet();
		char[] charArray = filterString.toCharArray();
		for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
			char c = charArray[i];
			filter.set(i, c == '1');
		}
	}

	public void add(String s) {
		//todo could be more efficent
		//https://github.com/Claudenw/BloomFilter/wiki/Bloom-Filters----An-overview
		for (int i = 0; i < k; i++) {
			filter.set(Math.abs(MurmurHash.hash(s.getBytes(), i)) % (m - 1), true);
		}
	}

	public boolean contains(String s) {
		for (int i = 0; i < k; i++) {
			if (!filter.get(Math.abs(MurmurHash.hash(s.getBytes(), i)) % (m - 1))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < filter.size(); i++) {
			boolean b = filter.get(i);
			out.append(b ? "1" : "0");
		}
		return "filter=" + out.toString() +
				"\nm=" + m +
				"\nk=" + k;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StringBloomFilter filter1 = (StringBloomFilter) o;
		return m == filter1.m &&
				k == filter1.k &&
				Objects.equals(filter, filter1.filter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(filter, m, k);
	}
}
