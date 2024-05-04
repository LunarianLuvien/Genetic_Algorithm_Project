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
