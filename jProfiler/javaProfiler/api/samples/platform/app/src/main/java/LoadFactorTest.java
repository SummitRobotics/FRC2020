import com.jprofiler.api.probe.embedded.Payload;

import java.util.HashMap;
import java.util.Random;

// This is the observed test program, see TestProfiler.java for more info

public class LoadFactorTest {

    private static final int TEST_OBJECT_COUNT = 10000;

    private TestObject[] testObjects = new TestObject[TEST_OBJECT_COUNT];
    private Random random;


    public void execute() {
        initializeTestObjects();

        for (int i = 0; i < 3; i++) {
            testLoadFactors(true);
        }

        for (int i = 0; i < 10; i++) {
            testLoadFactors(false);
        }
    }

    private void initializeTestObjects() {
        for (int i = 0; i < TEST_OBJECT_COUNT; i++) {
            testObjects[i] = new TestObject(getRandomInteger());
        }
    }

    public int getRandomInteger() {
        if (random == null) {
            initializeRandom();
        }
        return random.nextInt();
    }

    private void testLoadFactors(boolean warmup) {
        for (int i = 1; i <= 10; i++) {
            testLoadFactor((float)(0.1 * i), warmup);
        }
    }

    public void testLoadFactor(float loadFactor, boolean warmup) {
        Payload.enter(LoadFactorProbe.class);
        try {
            testLoadFactorsUnmeasured(loadFactor);
        } finally {
            Payload.exit(warmup ? null : "Load factor: " + loadFactor);
        }
    }

    private void testLoadFactorsUnmeasured(float loadFactor) {
        HashMap<TestObject, Object> testMap = new HashMap<>(16, loadFactor);
        for (int i = 0; i < TEST_OBJECT_COUNT; i++) {
            testMap.put(testObjects[i], null);
        }
    }

    private void initializeRandom() {
        random = new Random(System.currentTimeMillis());
    }

    // This class is used to get an identity for the heap statistics
    private static class TestObject {

        private int integer;

        public TestObject(int integer) {
            this.integer = integer;
        }

        @Override
        public boolean equals(Object o) {
            return integer == ((TestObject)o).integer;
        }

        @Override
        public int hashCode() {
            return integer;
        }
    }
}
