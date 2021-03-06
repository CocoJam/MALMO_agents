package GA_Testing_Ground;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;


public class Basic_GA {

    //Evaluation function for selection
    private static int eval (final Genotype<IntegerGene> genotype){
        return summationOfGene(genotype);
    }

    //Badly written summation function.
    private static int summationOfGene (Genotype<IntegerGene> genotype){
        int sum = 0;
        for (Chromosome<IntegerGene> integerGenes : genotype) {
            for (IntegerGene integerGene : integerGenes) {
                sum+= integerGene.getAllele();
            }
        }
        return  sum;
    }
    public static void main(String[] args) {
        //Please view the Gene structure.png to get better idea what is Genotype and chromosome.
        Factory< Genotype<IntegerGene> > factory = Genotype.of(
                IntegerChromosome.of(1,10, 8),
                IntegerChromosome.of(5,20,5),
                IntegerChromosome.of(3,6,10),
                IntegerChromosome.of(1,5,20),
                IntegerChromosome.of(5,5,5));

        //The evolution environment and executioner
        final Engine<IntegerGene, Integer> engine = Engine.builder(Basic_GA::eval, factory).offspringFraction(0.7)
                .survivorsSelector(new RouletteWheelSelector<>()).populationSize(500)
                .alterers(new Mutator<>(0.05),new SinglePointCrossover<>(0.125))
                .offspringSelector(new TournamentSelector<>())
                .build();

        //Result output based on the engine: Limit is number of generation.
        //                                   collection instances of Evolution results
        final Phenotype<IntegerGene, Integer> results = engine.stream().limit(1000)
                .peek(r -> System.out.println(r.getTotalGenerations() + ": " + r.getBestPhenotype()))
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(engine.getAlterer());
        System.out.println(engine.getFitnessFunction().getClass());
        System.out.println(results);
    }
}
