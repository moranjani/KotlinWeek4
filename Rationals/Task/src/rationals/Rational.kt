package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

data class Rational
private constructor(val num: BigInteger, val denom: BigInteger) : Comparable<Rational> {
    companion object {
        fun create(num: BigInteger, denom: BigInteger): Rational {
            return normalize(num, denom)
        }

        private fun normalize(num: BigInteger, denom: BigInteger): Rational {
            require(denom != 0.toBigInteger()) { "Denom cannot be zero" }
            val gcd = num.gcd(denom).abs()
            val denomSign = denom.signum().toBigInteger()
            val newNum = num / gcd * denomSign
            val newDenom = denom / gcd * denomSign
            return Rational(newNum, newDenom)
        }
    }


    override fun compareTo(other: Rational): Int {
        return (this.num * other.denom).compareTo(other.num * this.denom)
    }

//    override fun equals(other: Any?): Boolean {
//        return (other is Rational && this.num * other.denom == other.num * this.denom)
//    }

    override fun toString(): String {
        if (num == ZERO) return "0"
        var res: String = num.toString() + "/" + denom.toString()
        if (denom == ONE) res = num.toString()
        return res
    }


    operator fun plus(other: Rational): Rational {
        var retDenom = denom * other.denom
        val retNum: BigInteger = retDenom / denom * num + retDenom / other.denom * other.num
        return create(retNum, retDenom)
    }

    operator fun minus(other: Rational): Rational {
        var retDenom = denom * other.denom
        val retNum: BigInteger = retDenom / this.denom * this.num - retDenom / other.denom * other.num
        return create(retNum, retDenom)
    }

    operator fun times(other: Rational): Rational {
        return create(this.num * other.num, this.denom * other.denom)
    }

    operator fun div(other: Rational): Rational {
        return create(this.num * other.denom, this.denom * other.num)
    }

    operator fun unaryMinus(): Rational {
        return create(-num, denom)
    }
}


infix fun Int.divBy(other: Int): Rational {
    return Rational.create(this.toBigInteger(), other.toBigInteger())
}

infix fun Long.divBy(other: Long): Rational {
    return Rational.create(this.toBigInteger(), other.toBigInteger())
}

infix fun BigInteger.divBy(other: BigInteger): Rational {
    return Rational.create(this, other)
}

fun String.toRational(): Rational {
    fun String.toBigIntegerOrThrow() :BigInteger {
        val errMessage = "we are expecting a string of format int/int. We got $this"
        return this.toBigIntegerOrNull() ?: throw IllegalArgumentException(errMessage)
    }

    if ("/" !in this) {
        return Rational.create(this.toBigIntegerOrThrow(), ONE)
    }
    val (numText, denomText) = this.split("/")
    return Rational.create(numText.toBigIntegerOrThrow(), denomText.toBigIntegerOrThrow())
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