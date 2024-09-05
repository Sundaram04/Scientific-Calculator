package com.example.scientificcalculatorapp.basicCalculator

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorDisplay(
    displayText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.LightGray)
            .padding(16.dp)
            .height(80.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = displayText,
            fontSize = 32.sp,
            color = Color.Black
        )
    }
}

@Composable
fun CalculatorScreen(viewmodel: CalculatorViewModel = viewModel()) {
    val displayText = viewmodel.displayText
    val initialButtons = listOf(
        "AC", "DEL", "sin", "cos", "tan",
        "sin⁻¹", "cos⁻¹", "tan⁻¹", "cosh x", "tanh x", "sinh x",
        "x^y", "log x", "ln x", "C", "deg",
        "rad", "e^x", "√", "P", "x⁻¹", "√x",
        "x²", "x!", "|x|", "7", "8",
        "9", "%", "π", "4", "5",
        "6", "÷", "-", "1", "2",
        "3", "×", "+", ".", "0", "(", ")", "="
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CalculatorDisplay(
            displayText = displayText,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .weight(1f)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed( 5),
            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .height(450.dp)
                .animateContentSize()
        ) {
            items(initialButtons) { buttonText ->
                CalculatorText(
                    text = buttonText,
                    onClick = { viewmodel.onButtonClick(buttonText) },
                    modifier = Modifier.padding(4.dp)
                        .background(Color.Gray)
                )
                }
            }
        }
    }
@Composable
fun CalculatorText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        val parts = text.split("°")
        parts.forEachIndexed { index, part ->
            withStyle(style = SpanStyle(fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color.Black)) {
                append(part)
            }
            if (index < parts.size - 1) {
                withStyle(style = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)) {
                    append("°")  // Superscript degree symbol
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(2.dp)
            .background(Color.Gray)
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}