import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {

    static final String TARGET_PASSWORD = "ChatGPT and GPT-4";   // the password we're trying to find, chromosome will have as many genes as its length
    static final int GENERATION_CHROMOSOME_COUNT = 100;
    static final double CROSSOVER_PROBABILITY = 0.65;  // we keep the crossover probability high
    static final double MUTATION_PROBABILITY = 0.01;  // we keep the mutation probability low

    public static void main(String[] args) {

        int totalGenerations = 0;  // We will solve it 3 times. will hold how many generations it found in total
        for(int k = 1; k < 4; k++){ // We are solving it 3 times

            long startTime = System.currentTimeMillis();

            // Creating the First Generation
            ArrayList<String> population = createFirstGeneration(GENERATION_CHROMOSOME_COUNT, TARGET_PASSWORD.length()); // 1st generation
            int generationCount = 0;  // will hold how many generations a password finding process took
            boolean passwordFound = false; // control flag of the while

            // Sorted the population by fitness values (in ascending order).
            // The first index holds the value closest to our password.
            sortPopulation(population);

            if(fitness(population.get(0)) == 0){   // Checks if the password was found in the first randomly generated generation.
                System.out.print("Password found on first attempt " + population.get(0));
            }

            while(!passwordFound){ // continue until the password is found
                ArrayList<String> nextPopulation = new ArrayList<>();    // we will keep the next population in this arraylist

                // Elitism (Best chromosome transferred to the next generation)
                String bestChromosome = population.get(0); // the value closest to our password (stored at index 0 because it is sorted)
                nextPopulation.add(bestChromosome);

                for(int i = 0; i < GENERATION_CHROMOSOME_COUNT / 2; i++){  // we create the new generation through crossover and mutation processes

                    // select two chromosomes for selection, now we will perform crossover and mutation.
                    String parent1 = selection(population);
                    String parent2 = selection(population);
                    while (parent1.equals(parent2)) {   // to avoid crossover on the same chromosome
                        // if it selects the same, let it differ.
                        parent2 = selection(population);
                    }

                    // Before adding the values obtained by selection to the new population, we apply mutation.
                    String[] children = crossover(parent1, parent2);
                    children[0] = mutation(children[0]);
                    children[1] = mutation(children[1]);

                    nextPopulation.add(children[0]);
                    nextPopulation.add(children[1]);
                }

                // our new population becomes equal to the next population,
                population = nextPopulation;
                sortPopulation(population);

                bestChromosome = population.get(0);  // closest password and
                // to print the fitness value (the number of different characters from our actual password)
                double closestFitnessValue = fitness(bestChromosome);
                System.out.println("Generation: " + generationCount + " Closest password: " + bestChromosome +
                        " Fitness Value: " + (int)closestFitnessValue);
                if(closestFitnessValue == 0){  // if fitness value is 0, then the password has been found.
                    System.out.println(k+ ". attempt Password " + GENERATION_CHROMOSOME_COUNT + " chromosomes used " +
                            + generationCount + ". generation found.");
                    totalGenerations += generationCount;
                    passwordFound = true;    // loop ends
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
                    System.out.println(GENERATION_CHROMOSOME_COUNT + " chromosomes used " +
                            + k + ". password cracking process took " + totalTime + " milliseconds.");
                }
                else{
                    generationCount++;
                }
            }

        }
        System.out.printf("Average generations over 3 password solving: %.2f%n", (double)totalGenerations / 3.0);

    }

    public static ArrayList<String> createFirstGeneration(int chromosome_count, int gene_count){
        ArrayList<String> population = new ArrayList<>();
        Random random = new Random();

        // creation of chromosomes and adding them to the population.
        for(int i = 0; i < chromosome_count; i++){
            String chromosome = "";
            for(int j = 0; j < gene_count; j++){
                chromosome = chromosome + (char)(random.nextInt(95) + 32);  // ASCII 32-126 for password characters
            }
            population.add(chromosome);
        }
        return population;
    }

    public static double fitness(String chromosome){
        // The fitness value of the chromosome is the number of characters different from the password.
        // So, a chromosome with a low fitness value is closer to our password because it has more character similarities.

        int characterDifference = 0;
        for(int i = 0; i < chromosome.length(); i++){
            if(chromosome.charAt(i) != TARGET_PASSWORD.charAt(i)){    // comparing characters
                characterDifference++;
            }
        }
        return characterDifference;  // returns the number of characters different from our password for the given chromosome
    }

    public static String[] crossover(String chromosome1, String chromosome2){
        // Performs crossover between the string values held by the given two chromosomes.

        Random random = new Random();
        String[] newChromosomes = new String[2];  // two new chromosomes will be generated for the lower generation

        if(random.nextDouble() < CROSSOVER_PROBABILITY){ // crossover occurs
            int crossoverIndex = random.nextInt(chromosome1.length());  // where to crossover is randomly determined
            // the beginning of the first chromosome and the end of the second chromosome are concatenated.
            newChromosomes[0] = chromosome1.substring(0, crossoverIndex) + chromosome2.substring(crossoverIndex);

            // the beginning of the second chromosome and the end of the first chromosome are concatenated.
            newChromosomes[1] = chromosome2.substring(0, crossoverIndex) + chromosome1.substring(crossoverIndex);
        }
        else{ // if no crossover occurs
            newChromosomes[0] = chromosome1;
            newChromosomes[1] = chromosome2;
        }
        return newChromosomes;
    }

    public static String mutation(String chromosome){
        // Since the mutation probability is very low, there is a chance for each gene. For each gene,
        // a random value is assigned if there is to be a mutation,
        // and if there is no mutation, the value at the relevant index in the gene remains unchanged.

        Random random = new Random();
        String newChromosome = "";
        for(int i = 0; i < chromosome.length(); i++){

            if(random.nextDouble() < MUTATION_PROBABILITY){
                newChromosome = newChromosome + (char)(random.nextInt(95) + 32);
            }
            else{
                newChromosome += chromosome.charAt(i);
            }
        }
        return newChromosome;
    }

    public static void sortPopulation(ArrayList<String> population){
        // We are sorting using the SELECTION SORT algorithm. It sorts the given population arraylist
        // (which holds chromosomes as string type) by fitness values (in ascending order).
        // So, the chromosome with the lowest fitness value, i.e., closest to our password, is found at the first index.

        for (int i = 0; i < population.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < population.size(); j++) {
                if (fitness(population.get(j)) < fitness(population.get(minIndex))) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                String temp = population.get(i);
                population.set(i, population.get(minIndex));
                population.set(minIndex, temp);
            }
        }
    }

    static String selection(ArrayList<String> population) {
        Random random = new Random();

        double totalFitness = 0;
        for (String chromosome : population) {
            totalFitness += 1 / fitness(chromosome);  // adding the inverses of fitness values to increase the selection chance of chromosomes with low fitness values (closest to our password)
        }

        double spin = random.nextDouble() * totalFitness;  // spinning the wheel

        // Find the chromosome corresponding to the turn value
        double partialSum = 0;
        for (int i = 0; i < population.size(); i++) {
            partialSum += 1 / fitness(population.get(i));
            if (spin < partialSum) {
                return population.get(i);
            }
        }
        // to prevent errors
        return population.get(population.size() - 1);
    }
}
