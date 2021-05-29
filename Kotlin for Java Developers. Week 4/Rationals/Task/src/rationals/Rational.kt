package rationals

import java.math.BigInteger

class Rational(private val numerator:BigInteger, private val denominator: BigInteger): Comparable<Rational>{
    constructor(numerator: Int, denominator: Int): this(numerator.toBigInteger(), denominator.toBigInteger())
    constructor(numerator: Long, denominator: Long): this(numerator.toBigInteger(), denominator.toBigInteger())

    init {
        //require(denominator != BigInteger.ZERO)
    }

    operator fun plus(other: Rational) = Rational(this.numerator * other.denominator + this.denominator * other.numerator, this.denominator * other.denominator)
    operator fun minus(other: Rational) = Rational(this.numerator * other.denominator - this.denominator * other.numerator, this.denominator * other.denominator)
    operator fun times(other: Rational) = Rational(this.numerator * other.numerator, this.denominator * other.denominator)
    operator fun div(other: Rational) = Rational(this.numerator * other.denominator, this.denominator * other.numerator)
    operator fun div(other: BigInteger) = Rational(this.numerator / other, this.denominator / other)
    operator fun unaryMinus() = Rational(if(this.denominator > BigInteger.ZERO) this.numerator.unaryMinus() else this.numerator, this.denominator.abs())

    fun normalized(): Rational{
        val gcd = numerator.gcd(denominator)
        return this.div(gcd)
    }

    override fun toString(): String {
        val normalized = normalized()
        return "${if(normalized.denominator > BigInteger.ZERO) normalized.numerator else normalized.numerator.unaryMinus()}${if(normalized.denominator.abs() > BigInteger.ONE) "/${normalized.denominator.abs()}"  else ""}"
    }

    override fun compareTo(other: Rational): Int {
        val result = this.normalized() / other.normalized()

        return when{
            result.numerator == result.denominator -> 0
            result.numerator > result.denominator -> 1
            else -> -1
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Rational && (other === this || this.compareTo(other) == 0)
    }
}

infix fun Int.divBy(denominator: Int) = Rational(this, denominator)
infix fun Long.divBy(denominator: Long) = Rational(this, denominator)
infix fun BigInteger.divBy(denominator: BigInteger) = Rational(this, denominator)
fun String.toRational(): Rational{
    val numberSplitted = this.split('/')
    return Rational(numberSplitted[0].toBigInteger(), if(numberSplitted.size > 1) numberSplitted[1].toBigInteger() else BigInteger.ONE)
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}