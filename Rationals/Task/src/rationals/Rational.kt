package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

data class Rational(val num: BigInteger, val denom: BigInteger) : Comparable<Rational> {
    init {
       require(denom != 0.toBigInteger()) { "Denom cannot be zero" }
    }

    constructor(x: Int, y: Int): this(x.toBigInteger(), y.toBigInteger())

    constructor(x: Long, y: Long): this(x.toBigInteger(), y.toBigInteger()) {
        println("Now I'm using the third constructor")
    }

    override fun compareTo(other: Rational): Int {
        return (this.num * other.denom).compareTo(other.num * this.denom)
    }

    override fun equals(other: Any?): Boolean {
        return (other is Rational && this.num * other.denom == other.num * this.denom)
    }

    override fun toString(): String {
        if (num == 0.toBigInteger()) return "0"

        val gcd = num.gcd(denom).abs()
        val newNum = num / gcd
        val newDenom = denom / gcd
        var res :String = newNum.abs().toString() + "/" + newDenom.abs().toString()
        when {
            newDenom == 1.toBigInteger() -> res = newNum.toString()
            (num * denom) < 0.toBigInteger() -> res = "-$res"
        }
        return res
    }


    operator fun plus(other: Rational) : Rational {
        var retDenom = denom * other.denom
        val retNum: BigInteger = retDenom / denom * num + retDenom / other.denom * other.num
        return Rational(retNum, retDenom)
    }

    operator fun minus(other: Rational) : Rational {
        var retDenom = denom * other.denom
        val retNum: BigInteger = retDenom / this.denom * this.num - retDenom / other.denom * other.num
        return Rational(retNum, retDenom)
    }

    operator fun times(other: Rational) : Rational {
        return Rational(this.num * other.num, this.denom * other.denom)
    }

    operator fun div(other: Rational) : Rational {
        return Rational(this.num * other.denom, this.denom * other.num)
    }

    operator fun unaryMinus() : Rational {
        var newNum = num.abs()
        if (this.num * this.denom > 0.toBigInteger()) {
            newNum = -newNum
        }
        return Rational(newNum, denom.abs())
    }

}

infix fun Int.divBy(other: Int): Rational {
    return Rational(this, other)
}

infix fun Long.divBy(other: Long): Rational {
    return Rational(this, other)
}

infix fun BigInteger.divBy(other: BigInteger): Rational {
    return Rational(this, other)
}

fun String.toRational() : Rational {
    if ("/" !in this) return Rational(this.toBigInteger(), 1.toBigInteger())
    val split = this.split("/")
    return Rational(split[0].toBigInteger(), split[1].toBigInteger())
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