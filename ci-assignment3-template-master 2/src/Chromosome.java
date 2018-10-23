public class Chromosome {
    private int[] data;

    public Chromosome(int[] data){
        this.data = data;
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
}
