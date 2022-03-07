public abstract class MonteCarlo {

    private int iteration = 1;
    private boolean run = true;

    public MonteCarlo() {

    }

    public abstract void beforeReplications();

    public abstract void beforeReplication();

    public void simulate(int iterationCount) {
        beforeReplications();
        for (iteration = 1; iteration <= iterationCount; iteration++) {
            beforeReplication();
            replication();
            afterReplication();
            if (!run) break;
        }
        afterReplications();
    }

    public abstract void replication();

    public abstract void afterReplication();

    public abstract void afterReplications();

    public int getIteration() {
        return iteration;
    }

    public void stop() {
        this.run = false;
    }

}
