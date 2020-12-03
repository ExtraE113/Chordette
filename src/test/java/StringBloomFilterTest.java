import junit.framework.TestCase;

public class StringBloomFilterTest extends TestCase {

	public void testAddAndContains() {
		StringBloomFilter filter = new StringBloomFilter();
		filter.add("hello");
		filter.add("world");
		assert filter.contains("hello");
		assert filter.contains("world");

		assert !filter.contains("goodbye");
		assert !filter.contains("universe");
	}

	public void testProbability() {
//		n = 4000000
//		p = 0.010039217 (1 in 100)
//		m = 38340234 (4.57MiB)
//		k = 7
		StringBloomFilter filter = new StringBloomFilter(38340234, 7);

		int contains = 0;

		for (int i = 0; i < 4000000; i++) {
			String s = ((Integer) i).toString();
			filter.add(s);
		}

		for (int i = 0; i < 10000; i++) {
			String s = ((Integer) i).toString() + "abcd";
			if (filter.contains(s)) {
				contains++;
			}
		}

		//our collisions should be less than 1% (with above criteria).
		//add a little padding so <110 collisions (1% would be 100)
		System.out.println(contains);
		assert contains < 110;
	}

	public void testToFromString() {
		StringBloomFilter filter = new StringBloomFilter(3800234, 7);
		for (int i = 0; i < 4000000; i++) {
			String s = ((Integer) i).toString();
			filter.add(s);
		}

		String s = filter.toString();
		StringBloomFilter newFilter = new StringBloomFilter(s);
		assertEquals(filter, newFilter);
		assertNotSame(filter, new StringBloomFilter());
	}
}