import com.sun.xml.internal.fastinfoset.util.StringArray
import kotlin.system.exitProcess
import java.io.FileNotFoundException
import java.io.IOException
import ec.*
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    when {
        args.isNotEmpty() && args[0] == "-help" -> {
            usage()
            exitProcess(0)
        }
        else -> {
            val filenames: Array<String> = arrayOf(
                    "data/bin1data/N1C1W1_A.BPP", "data/bin1data/N4C3W2_T.BPP",
                    "data/bin2data/N1W1B1R1.BPP", "data/bin2data/N3W2B2R0.BPP",
                    "data/bin3data/HARD2.BPP", "data/bin3data/HARD9.BPP"
            )
            val bins: Array<Int> = arrayOf(25, 195, 18, 40, 56, 56)
            info()
            try {
                for (f in 0 until filenames.size) {
                    var dataSet = readDataFrom(filenames[f])
                    var objectiveNumBins = bins[f]
                    val numItems = dataSet[0]
                    val binCapacity = dataSet[1]
                    dataSet = dataSet.sliceArray(2 until dataSet.size)// remove numItems && binCapacity
                    when (f) {
                        0, 1 -> println("-Easy Problem Instance ${f + 1}")
                        2, 3 -> println("-Medium Problem Instance ${f + 1}")
                        4, 5 -> println("-Hard Problem Instance ${f + 1}")
                    }
                    val geneticAlgorithm = GA(dataSet)
                    for (i in 1 until 10) {
                        println("Simulation $i")
                        val epsilon = Random().nextLong() // get random seed
                        geneticAlgorithm.init(epsilon, numItems, binCapacity, objectiveNumBins)
                        geneticAlgorithm.run()
                    }
                }
            } catch (e: FileNotFoundException) {
                println("ERROR => File ${e.message}")
            } catch (e: IOException) {
                println("ERROR => IO ${e.message}")
            }
        }
    }
}

fun readDataFrom(filename: String): IntArray {
    var fileData: List<String> = emptyList()
    println("---Reading File---")
    val bufReader = File(filename).bufferedReader()
    fileData = bufReader.readLines()
    val dataSet: IntArray = IntArray(fileData.size, { 0 })
    (0 until fileData.size).forEach {
        dataSet[it] = fileData[it].toInt()
    }
    return dataSet
}

fun info() {
    println("---Welcome to Le-Tsatsi---")
    println("-Genetic Algorithm for 1D BinPacking (offline)")
    println("-Version: 0.1.0")
    println("-Developed by Keoagile M. Dinake, 2018")
}

fun usage() {
    println("---Guidelines to using Le-Tsatsi---")
    println("")
    println("$ ./letsatsi <option>")
    println("where <option> is: \n")
    println("-help -1\tFor this exact guideline.")
    println("")
    println("---Description of Program---")
    println("")
    println("The Le-Tsatsi Genetic Algorithm is designed to solve the offline one-dimensional bin packing problem, ")
    println("which essentially aims to pack a collection of objects into well defined regions called bins, such ")
    println("that they don’t overlap. \n\nMathematically this can be defined as:\n\nGiven a bin capacity C and a ")
    println("list of objects {p(1), p(2), .., p(N)}, allocate all the items to bins such that the all the items are ")
    println("packed using the smallest number of bins. Each p(i) has a size s(j), such that 0 <= s(j) <= C, i.e., ")
    println("none of the objects are too big to fit in a bin.\n")
}

