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

    public double getFitness(TSPData pd) {
        // get distance information    
        int[][] distances = pd.getDistances();
        int[] startDistances = pd.getStartDistances();
        int[] endDistances = pd.getEndDistances();
        
        // initialize length of the route as distance between start and first product
        int firstProduct = data[0];
        int length = startDistances[firstProduct];
        // add the lengths of all paths between products to the total length
        for (int i = 0; i < data.length - 1; i++) {
        	length += distances[i][i+1];
        }
        // finally, add the length of the route between the last product and the end
        int lastProduct = data[data.length];
        length += endDistances[lastProduct];
        
        //TODO calculate the actual fitness: 1/length of route
        return 1.0 / length;
    }

    public Chromosome crossOver(Chromosome partner){
        //TODO implement cross-over function
        return this;
    }

    public Chromosome mutate() {
        //TODO implement mutation function, making sure every product occurs exactly once
    	Chromosome mutation = new Chromosome(data);
    	
    	
    	
        return this;
    }
}
