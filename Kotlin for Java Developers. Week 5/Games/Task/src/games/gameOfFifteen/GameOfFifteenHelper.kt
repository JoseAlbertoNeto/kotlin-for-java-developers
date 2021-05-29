package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */

fun isEven(permutation: List<Int>): Boolean {
    val expected = permutation.sorted()
    val result = permutation.toMutableList()
    var count = 0

    while(result != expected){
        for(i in result.indices){
            for(j in result.indices){
                if(i+(if(expected.first() == 0) 0 else 1) == result[j] && i != j && result != expected) {
                    val temp = result[i]
                    result[i] = result[j]
                    result[j] = temp
                    count++
                    break
                }
            }
        }
    }

    return count % 2 == 0
}