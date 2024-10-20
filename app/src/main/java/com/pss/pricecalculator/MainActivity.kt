package com.pss.pricecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pss.pricecalculator.ui.theme.PriceCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            PriceCalculatorTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    var averagePriceInput by remember {
                        mutableStateOf("")
                    }
                    var percentageInput by remember { mutableStateOf("") }

                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))

                        Text(text = "주식 가격 계산기", fontSize = 30.sp, fontWeight = FontWeight.Black)

                        Spacer(modifier = Modifier.height(20.dp))

                        TradingTutorial()

                        Spacer(modifier = Modifier.height(30.dp))

                        TextField(
                            value = averagePriceInput,
                            onValueChange = {
                                averagePriceInput = it
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        ImportantPriceInfo(averagePrice = averagePriceInput)


                        if (averagePriceInput.isNotBlank()){
                            val calculatedAmount = if (percentageInput.isNotEmpty()) {
                                val percentage = percentageInput.toFloatOrNull() ?: 0f
                                averagePriceInput.toInt() * (percentage / 100)
                            } else {
                                0f
                            }

                            TextField(
                                value = percentageInput,
                                onValueChange = { newText ->
                                    // 입력값이 숫자인지 확인
                                    if (newText.all { it.isDigit() || it == '.' }) {
                                        percentageInput = newText
                                    }
                                },
                                label = { Text("Enter percentage") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                )
                            )

                            Text(text = "+${percentageInput}% 가격 : ${"%.2f".format(averagePriceInput.toInt() + calculatedAmount)} 원")
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ImportantPriceInfo(modifier: Modifier = Modifier, averagePrice: String) {
    Text(text = "+4% 가격 : ${changePercentPrice(averagePrice, true, 0.04)}")

    Text(text = "+7% 가격 : ${changePercentPrice(averagePrice, true, 0.07)}")

    Text(text = "-4% 가격 : ${changePercentPrice(averagePrice, false, 0.04)}")

    Text(text = "-8% 가격 : ${changePercentPrice(averagePrice, false,0.08)}")
}

private fun changePercentPrice(
    price: String,
    isPlus: Boolean,
    percent: Double
): String{
    return if (price.isBlank()) "평단가를 입력해 주세요"
    else {
        if(isPlus) (price.toInt() * (1 + percent)).toString()
        else (price.toInt() * (1 - percent)).toString()
    }
}

@Composable
fun TradingTutorial(modifier: Modifier = Modifier) {
    Text(text = "사용 방법", fontSize = 20.sp, fontWeight = FontWeight.Bold)

    Spacer(modifier = Modifier.height(8.dp))

    Text(text = "매수 방법 : 첫 매수 시 50%, 그 다음 추매 25%, 25% = 총 합 100%")

    Spacer(modifier = Modifier.height(8.dp))

    Text(text = "추매 기준 : 등락률 4%씩 (예 : -4%, -8%)")

    Spacer(modifier = Modifier.height(8.dp))

    Text(text = "매도 기준 : 4% 올랐을 시 50%, 7% 올랐을 시 50%")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PriceCalculatorTheme {

    }
}