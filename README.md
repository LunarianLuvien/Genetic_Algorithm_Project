# Genetic Algorithm for Password Cracking

This Java program demonstrates a genetic algorithm designed to guess a predefined password, "ChatGPT and GPT-4". It simulates the evolutionary process to generate a sequence of characters that match the target password.

## Features

- **Initial Generation**: Creates an initial population of random character sequences, or 'chromosomes', each representing a potential solution.
- **Fitness Calculation**: Evaluates how close each chromosome in the population is to the target password.
- **Selection**: Chooses pairs of chromosomes for reproduction based on their fitness scores, using a selection process that ensures diversity.
- **Crossover and Mutation**: Applies crossover to generate new chromosomes and randomly mutates them with a low probability to introduce variation.
- **Elitism**: Ensures the best solution from each generation is carried over to the next, preserving good traits.
- **Termination**: The process repeats until the algorithm successfully guesses the password or reaches a specified number of generations.

## Execution

The program runs the genetic algorithm three times to demonstrate its effectiveness and consistency. It tracks:
- The number of generations needed to guess the password correctly.
- The time taken for each attempt.

## Results

Outputs include:
- The closest guessed password and its fitness score per generation.
- The total number of generations and time elapsed for each complete run.
