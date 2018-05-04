package ec

import java.util.Random
import kotlin.math.roundToInt

data class Population(val numItems: Int, val binCapacity: Int, val randomSeed: Long, val objectiveNumBins: Int) {
    var individuals: MutableList<Individual> = mutableListOf()
    private val rand = Random(randomSeed)
    private var probCrossOver = rand.nextDouble()
    private var probMutate = rand.nextDouble()
    private val tournamentSize = 5

    fun crossover(parentX: Individual, parentY: Individual): Array<Individual> {
        // Uniform Crossover
        val childAlpha = Individual()
        val childBeta = Individual()
        val size = if (parentX.genotype.size < parentY.genotype.size) {
            parentX.genotype.size
        } else {
            parentY.genotype.size
        }
        for (i in 0 until size) {
            val epsilon = this.rand.nextDouble()
            if (epsilon <= this.probCrossOver) {
                childAlpha.genotype.add(i, parentX.genotype[i])
                childBeta.genotype.add(i, parentY.genotype[i])
            } else {
                childAlpha.genotype.add(i, parentY.genotype[i])
                childBeta.genotype.add(i, parentX.genotype[i])
            }
        }
        return arrayOf(childAlpha, childBeta)
    }

    fun mutate(individual: Individual): Individual {
        // Perform a spinoff the Gaussian Mutation
        (0 until individual.genotype.size).forEach { i ->
            val epsilon = this.rand.nextDouble()
            if (epsilon <= this.probMutate) {
                individual.genotype[i] += this.rand.nextGaussian().roundToInt()
            }
        }
        return individual
    }

    fun tournamentSelect(): Individual {
        val tournament = Population(this.numItems, this.binCapacity, this.randomSeed, 0)
        (0 until this.tournamentSize).forEach { i ->
            val randomIdx = (this.rand.nextDouble() * this.individuals.size).toInt()
            tournament.individuals.add(i, this.individuals[randomIdx])
        }
        val fittest = tournament.getFittest()
        return fittest
    }

    fun getFittest(): Individual {
        var fittestIndividual = this.individuals[0]
        var fittestAllele = fittestIndividual.fitness(this.binCapacity)
        (1 until this.individuals.size).forEach { i ->
            val allele = this.individuals[i].fitness(this.binCapacity)
            if (allele < fittestAllele && this.individuals[i].genotype.size == this.objectiveNumBins) {
                fittestIndividual = this.individuals[i]
                fittestAllele = allele
            }
        }
        return fittestIndividual
    }
}