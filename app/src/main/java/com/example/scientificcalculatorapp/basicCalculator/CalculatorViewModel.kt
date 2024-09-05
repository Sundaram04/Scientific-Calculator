package com.example.scientificcalculatorapp.basicCalculator

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class CalculatorViewModel(private val model: CalculatorModel = CalculatorModel()) : ViewModel() {
    var displayText by mutableStateOf("0")
    var isResultDisplayed by mutableStateOf(false)
    var isDegreeMode by mutableStateOf(true)

    fun onButtonClick(buttonText: String) {
        when (buttonText) {
            "AC" -> clearDisplay()
            "DEL" -> deleteLastCharacter()
            "=" -> calculateResult()
            "deg" -> isDegreeMode = true
            "rad" -> isDegreeMode = false
            "sin" -> handleTrigFunction { value -> model.calculateSin(value) }
            "cos" -> handleTrigFunction { value -> model.calculateCos(value) }
            "tan" -> handleTrigFunction { value -> model.calculateTan(value) }
            "sin⁻¹x" -> null
            "cos⁻¹x" -> null
            "tan⁻¹x" -> null
            "sin hx" -> null
            "cos hx" -> null
            "tan hx" -> null
            "log x" -> handleLogInput()
            "ln x" -> handleLnInput()
            "x^y" -> displayText += "^"
            "e^x" -> null
            "nCr" -> handleCombinationInput()
            "nPr" -> handlePermutationInput()
            "x⁻¹" -> handleInverseInput()
            "√x" -> handleSqrtInput()
            "x√y" -> handleRootInput()
            "x²" -> handleSquareInput()
            "x!" -> handleFactorialInput()
            "|x|" -> handleModulusInput()
            "π" -> handlePiInput()
            "." -> handleDecimalInput()
            else -> handleInput(buttonText)
        }
    }

    private fun clearDisplay() {
        displayText = "0"
        isResultDisplayed = false
        isDegreeMode = true
    }

    private fun deleteLastCharacter() {
        displayText = if (displayText.length > 1) {
            displayText.dropLast(1)
        } else {
            "0"
        }
        isResultDisplayed = false
    }

    private fun calculateResult() {
        if (displayText.contains("C")) {
            handleCombinationInput()
        }
        else if(displayText.contains("P")){
            handlePermutationInput()
        }else {
            displayText = model.evaluateExpression(displayText)
        }
        isResultDisplayed = true
    }


    private fun handleInput(buttonText: String) {
        if (buttonText == "%") {
            if (displayText.isNotEmpty()) {
                // Find the last operator's position
                val lastOperatorIndex = displayText.indexOfLast { it.isOperator() }

                // Extract the substring before and after the last operator
                val beforeLastNumber = if (lastOperatorIndex == -1) "" else displayText.substring(0, lastOperatorIndex + 1)
                val lastNumber = if (lastOperatorIndex == -1) displayText else displayText.substring(lastOperatorIndex + 1)

                // Calculate percentage
                val updatedNumber = lastNumber.toDoubleOrNull()?.let { String.format("%.2f", it / 100) } ?: "Error"
                displayText = beforeLastNumber + updatedNumber
            }
            isResultDisplayed = false
        } else if (buttonText == "√") {
            // Handle square root operation
            handleSqrtInput()
            isResultDisplayed = false
        } else if (buttonText == "x√y") {
            // Handle root operation
            handleRootInput()
            isResultDisplayed = false
        } else {
            // existing code for other buttons
            if (buttonText == "-") {
                handleNegativeInput(buttonText)
            } else if (isResultDisplayed) {
                displayText = if (buttonText in listOf("+", "-", "×", "÷", "%", "^")) {
                    "$displayText $buttonText "
                } else {
                    buttonText
                }
                isResultDisplayed = false
            } else {
                displayText = if (displayText == "0" && buttonText != "." && !buttonText.isOperator()) {
                    buttonText
                } else {
                    displayText + buttonText
                }
            }
            isResultDisplayed = false
        }
    }

    fun handleRootInput() {
        try {
            // Split the displayText by the '√' operator
            val parts = displayText.split("√")

            if (parts.size == 2) {
                // Extract root and number values
                val root = parts[0].toDoubleOrNull()  // This should be the root value
                val number = parts[1].toDoubleOrNull()  // This should be the number

                // Check if both values are valid
                if (root != null && number != null) {
                    // Calculate the x-th root of y
                    val result = model.calculateRoot(root, number)
                    displayText = result.toString()
                } else {
                    displayText = "Error"
                }
            } else {
                displayText = "Error"
            }
        } catch (e: Exception) {
            displayText = "Error"
        }
        isResultDisplayed = true
    }
    // Add a flag to track if we are waiting for the number after "√"
    private var waitingForNumberAfterRoot = false


    private fun handleSqrtInput() {
        try {
            val number = displayText.toDoubleOrNull()
            if (number != null) {
                val result = Math.sqrt(number)
                displayText = result.toString()
            } else {
                displayText = "Error"
            }
        } catch (e: Exception) {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun Char.isOperator(): Boolean {
        return this in listOf('+', '-', '×', '÷', '%', '^')
    }


    private fun handlePercentageInput() {
        try{
            val number = displayText.toDoubleOrNull()
            if(number != null) {
                val result = number / 100
                displayText = result.toString()
            } else {
                displayText = "Error"
            }
        } catch(e: Exception) {
            displayText = "Error"
        }
    }

    private fun handleNegativeInput(buttonText: String) {
        if (isResultDisplayed) {
            displayText = buttonText
            isResultDisplayed = false
        } else if (displayText == "0" && buttonText == "-") {
            displayText = buttonText
        } else {
            displayText += buttonText
        }
    }

    private fun String.isOperator(): Boolean {
        return this in listOf("+", "-", "×", "÷", "%", "^")
    }

    private fun handleTrigFunction(operation: (Double) -> Double) {
        val value = displayText.toDoubleOrNull()
        if (value != null) {
            displayText = operation(value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handleLogInput() {
        val value = displayText.toDoubleOrNull()
        if (value != null) {
            displayText = model.calculateLog(value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handleLnInput() {
        val value = displayText.toDoubleOrNull()
        if (value != null) {
            displayText = model.calculateLn(value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handleCombinationInput() {
        // Assuming displayText is in format "nCr" where n and r are integers
        val parts = displayText.split("C")
        if (parts.size == 2) {
            val n = parts[0].toIntOrNull()
            val r = parts[1].toIntOrNull()
            if (n != null && r != null) {
                displayText = model.combination(n, r).toString()
            } else {
                displayText = "Error"
            }
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handlePermutationInput() {
        // Assuming displayText is in format "nPr" where n and r are integers
        val parts = displayText.split("P")
        if (parts.size == 2) {
            val n = parts[0].toIntOrNull()
            val r = parts[1].toIntOrNull()
            if (n != null && r != null) {
                displayText = model.permutation(n, r).toString()
            } else {
                displayText = "Error"
            }
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handleInverseInput() {
        val value = displayText.toDoubleOrNull()
        if (value != null && value != 0.0) {
            displayText = (1 / value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }



    private fun handleSquareInput() {
        val value = displayText.toDoubleOrNull()
        if (value != null) {
            displayText = (value * value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handleFactorialInput() {
        val value = displayText.toIntOrNull()
        if (value != null) {
            displayText = model.calculateFactorial(value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handleModulusInput() {
        val value = displayText.toDoubleOrNull()
        if (value != null) {
            displayText = model.calculateAbs(value).toString()
        } else {
            displayText = "Error"
        }
        isResultDisplayed = true
    }

    private fun handlePiInput() {
        displayText = Math.PI.toString()
        isResultDisplayed = true
    }

    private fun handleDecimalInput() {
        if (isResultDisplayed) {
            displayText = "0."
            isResultDisplayed = false
        } else {
            if (!displayText.contains(".")) {
                displayText += "."
            }
        }
        isResultDisplayed = false
    }

}
