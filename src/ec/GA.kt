package ec

import java.util.*

class GA(val dataSet: IntArray) {
    private val maxGenerations = 50
    private var generationCounter = 0
    private var objectiveValue = 0
    private var population: Population = Population(0,0, 0, 0)

    fun init(randSeed: Long, nItems: Int, bCapacity: Int, objectiveNumBins: Int) {
        this.objectiveValue = objectiveNumBins
        this.population = Population(numItems = nItems, binCapacity = bCapacity, randomSeed = randSeed, objectiveNumBins = objectiveNumBins)
        (0 until 20).forEach { i ->
            var numBins: Int = (Random().nextDouble() * nItems).toInt()
            if (numBins <= 0) {
                numBins = this.objectiveValue
            }
            val newIndividual = Individual()
            newIndividual.genotype = MutableList(numBins, { 0 })
            for (j in 0 until numBins) {
                val randomIdx = (Random().nextDouble() * this.dataSet.size).toInt()
                newIndividual.genotype[j] = this.dataSet[j]
            }
            this.population.individuals.add(i, newIndividual)
        }
    }

    fun run() {
        while (this.generationCounter < this.maxGenerations) {
            val fittest = this.population.getFittest()
            println("Generation ${this.generationCounter}")
            println("Fittest genotype: ${fittest.genotype}")
            println("Fittest allele: ${fittest.fitness(this.population.binCapacity)}")
            this.generationCounter += 1
            val fitParentX = this.population.tournamentSelect()
            val fitParentY = this.population.tournamentSelect()
            val offspring = this.population.crossover(fitParentX, fitParentY)
            this.population.individuals.add(offspring[0])
            this.population.individuals.add(offspring[1])
            val randomIdx = (Random().nextDouble() * this.population.individuals.size).toInt()
            val individual = this.population.individuals[randomIdx]
            val mutatedIndividual = this.population.mutate(individual)
            this.population.individuals[randomIdx] = mutatedIndividual
        }
    }
}