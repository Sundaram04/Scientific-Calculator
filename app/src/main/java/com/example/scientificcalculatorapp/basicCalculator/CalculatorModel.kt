package com.example.scientificcalculatorapp.basicCalculator

import java.util.Stack
import kotlin.math.*

class CalculatorModel {

    // Existing evaluateExpression and evaluate functions...

    fun evaluateExpression(expression: String): String {
        return try {
            val result = evaluate(expression)
            if (result % 1 == 0.0) {
                return result.toInt().toString()
            } else {
                return result.toString()
            }
        } catch (e: Exception) {
            return "Error"
        }
    }

    private fun evaluate(expression: String): Double {
        val operatorStack = Stack<Char>()
        val operandStack = Stack<Double>()
        var i = 0

        while (i < expression.length) {
            when {
                expression[i] == '√' -> {
                    operatorStack.push(expression[i])
                    i++
                }
                expression[i] == '(' -> {
                    operatorStack.push(expression[i])
                    i++
                }
                expression[i] == ')' -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() != '(') {
                        applyOperator(operatorStack.pop().toString(), operandStack)
                    }
                    operatorStack.pop() // Remove '('
                    i++
                }
                expression[i].isDigit() || expression[i] == '.' -> {
                    val sb = StringBuilder()
                    while (i < expression.length && (expression[i].isDigit() || expression[i] == '.')) {
                        sb.append(expression[i])
                        i++
                    }
                    operandStack.push(sb.toString().toDouble())
                }
                expression[i].isOperator() -> {
                    while (operatorStack.isNotEmpty() && precedence(operatorStack.peek()) >= precedence(expression[i])) {
                        applyOperator(operatorStack.pop().toString(), operandStack)
                    }
                    operatorStack.push(expression[i])
                    i++
                }
                else -> i++ // Skip invalid characters
            }
        }

        while (operatorStack.isNotEmpty()) {
            applyOperator(operatorStack.pop().toString(), operandStack)
        }

        return operandStack.pop()
    }

    private fun applyOperator(op: String, operands: Stack<Double>) {
        when (op) {
            "√" -> {
                val value = operands.pop()
                operands.push(sqrt(value))
            }
            "^" -> {
                val exponent = operands.pop()
                val base = operands.pop()
                operands.push(base.pow(exponent))
            }
            "x√y" -> {
                val number = operands.pop()
                val root = operands.pop()
                operands.push(calculateRoot(root, number))
            }
            "+", "-", "*", "/" -> {
                val right = operands.pop()
                val left = operands.pop()
                operands.push(
                    when (op) {
                        "+" -> left + right
                        "-" -> left - right
                        "*" -> left * right
                        "/" -> left / right
                        else -> throw UnsupportedOperationException("Unsupported operator: $op")
                    }
                )
            }
            "%" -> {
                val right = operands.pop()
                val left = operands.pop()
                operands.push(left + right / 100)
            }
            else -> throw UnsupportedOperationException("Unsupported operator: $op")
        }
    }


    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            '√' -> 3
            else -> 0
        }
    }

    private fun Char.isOperator() = this in listOf('+', '-', '*', '/', '√')

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') {
            return false
        }
        if ((op1 == '×' || op1 == '÷' || op1 == '%' || op1 == '^') && (op2 == '+' || op2 == '-')) {
            return false
        }
        return true
    }

    private fun applyOperation(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '×' -> a * b
            '÷' -> if (b == 0.0) throw ArithmeticException("Cannot divide by zero") else a / b
            '%' -> a % b
            '^' -> a.pow(b)
            'C' -> combination(a.toInt(), b.toInt()).toDouble()
            'P' -> permutation(a.toInt(), b.toInt()).toDouble()
            else -> throw UnsupportedOperationException("Unknown operation")
        }
    }

    // Individual Functions for Buttons

    fun calculateSin(value: Double): Double {
        return sin(value)
    }

    fun calculateCos(value: Double): Double {
        return cos(value)
    }

    fun calculateTan(value: Double): Double {
        return tan(value)
    }

    fun calculateLog(value: Double): Double {
        return log10(value)
    }

    fun calculateLn(value: Double): Double {
        return ln(value)
    }

    fun calculateSqrt(value: Double): Double {
        return sqrt(value)
    }

    fun calculateFactorial(value: Int): Double {
        return factorial(value).toDouble()
    }

    fun calculateAbs(value: Double): Double {
        return abs(value)
    }

    fun calculateExp(value: Double): Double {
        return exp(value)
    }

    private fun factorial(n: Int): Int {
        return if (n == 0 || n == 1) 1 else n * factorial(n - 1)
    }

    fun combination(n: Int, r: Int): Int {
        if (r > n) return 0
        return factorial(n) / (factorial(r) * factorial(n - r))
    }

    fun permutation(n: Int, r: Int): Int {
        return factorial(n).toInt() / factorial(n - r).toInt()
    }
    fun calculateRoot(root: Double, number: Double): Double {
        return number.pow(1.0 / root)
    }
}
