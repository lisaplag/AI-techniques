public class Chromosome {
    private int[] data;
    private double chanceCrossOver;
    private double chanceMutation;

    public Chromosome(int[] data){
        this.data = data;
        this.chanceCrossOver = 0.7;
        this.chanceMutation = 0.01;
    }
    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public double getFitness() {
        int fitness = 0;
        //TODO calculate the actual fitness: 1/distance
        //TODO set very low fitness if same product occurs twice.
        return fitness;
    }

    public Chromosome crossOver(Chromosome partner){
        //TODO implement cross-over function
        return this;
    }

    public Chromosome mutate() {
        //TODO implement mutation function
        return this;
    }
}
