/*
 This class serves the purpose of encoding a possible solution, i.e., the chromosome.
*/
package ec

class Individual {
    var genotype: MutableList<Int> = mutableListOf()

    fun fitness(binCapacity: Int): Double {
        var strength = 0.0
        genotype.forEach {
            strength += binCapacity / it.toDouble()
        }
        return strength
    }
}