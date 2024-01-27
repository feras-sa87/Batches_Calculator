package com.example.batchescalculator

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.batchescalculator.components.dismissKeyboardOnTap
import com.example.batchescalculator.ui.theme.BatchesCalculatorTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatchesCalculatorTheme {
                var showSplashScreen by remember {
                    mutableStateOf(true)
                }
                LaunchedEffect(key1 = true) {
                    delay(3000)
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SplashScreen()
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .dismissKeyboardOnTap(),
                        tonalElevation = 15.dp
                    ) {
                        BatchesCalculatorScreen()
                    }
                }

            }
        }
    }
}

@Composable
fun SplashScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Card(
            shape = MaterialTheme.shapes.extraLarge,
            elevation = cardElevation(defaultElevation = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Crop, modifier = Modifier.size(180.dp),
                alpha = 0.8f
                )
        }
        Text(
            text = "Made with ❤️ by Feras",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        CircularProgressIndicator(modifier = Modifier.padding(top = 36.dp))
    }

}

@Composable
fun BatchesCalculatorScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        EditTextFieldsAndResult()
    }
}


@Composable
fun EditTextFieldsAndResult() {
    var batchesRequiredInput by rememberSaveable {
        mutableStateOf("")
    }

    var portionWeightInput by rememberSaveable {
        mutableStateOf("")
    }

    val totalBatchesWeight =
        (120 * (batchesRequiredInput.toDoubleOrNull() ?: 0.0) * (portionWeightInput.toIntOrNull()
            ?: 0)) / 1000.0

    val batchWeight = (120 * (portionWeightInput.toIntOrNull() ?: 0)) / 1000.0


    var unitWeightInput by rememberSaveable {
        mutableStateOf("")
    }

    val ingredientRequired = (totalBatchesWeight / (unitWeightInput.toDoubleOrNull() ?: 0.0))

    var availableIngredientInput by rememberSaveable {
        mutableStateOf("")
    }

    val availableIngredient =
        (availableIngredientInput.toIntOrNull() ?: 0) * (unitWeightInput.toDoubleOrNull() ?: 0.0)

    var isErrorBatches by rememberSaveable {
        mutableStateOf(false)
    }

    var isErrorPortionWeight by rememberSaveable {
        mutableStateOf(false)
    }

    var isErrorUnitWeight by rememberSaveable {
        mutableStateOf(false)
    }

    var isErrorAvailableIngredients by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    @Composable
    fun isError(isError: Boolean, isUnitWeight: Boolean = false, isBatches: Boolean = false) {
        if (isError) {
            if (isUnitWeight || isBatches) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Limit 5 digits",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Limit 3 digits",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    fun validate() {
        isErrorBatches = batchesRequiredInput.length > 5
        isErrorPortionWeight = portionWeightInput.length > 3
        isErrorUnitWeight = unitWeightInput.length > 5
        isErrorAvailableIngredients = availableIngredientInput.length > 3
    }

    OutlinedTextField(isError = isErrorBatches, value = batchesRequiredInput, onValueChange = {
        batchesRequiredInput = it
        validate()
        if (isErrorBatches) {
            batchesRequiredInput = ""
        }
    }, supportingText = { isError(isErrorBatches, isBatches = true) }, trailingIcon = {
        if (isErrorBatches) {
            Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
        }
    }, label = { Text("Batches") }, singleLine = true, keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
    ), modifier = Modifier.width(280.dp)
    )

    OutlinedTextField(
        isError = isErrorPortionWeight,
        value = portionWeightInput,
        onValueChange = {
            portionWeightInput = it
            validate()
            if (isErrorPortionWeight) {
                portionWeightInput = ""
            }
        },
        supportingText = { isError(isErrorPortionWeight) },
        trailingIcon = {
            if (isErrorPortionWeight) {
                Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
            }
        },
        label = { Text("Portion weight (g)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
        ),
        modifier = Modifier.width(280.dp)
    )

    OutlinedTextField(
        isError = isErrorUnitWeight,
        value = unitWeightInput,
        onValueChange = {
            unitWeightInput = it
            validate()
            if (isErrorUnitWeight) {
                unitWeightInput = ""
            }
        },
        supportingText = {
            if (isErrorUnitWeight) isError(
                isError = true, isUnitWeight = true
            ) else Text(
                text = "Press the kg icon to convert the entered lb value to kg", fontSize = 10.sp
            )
        },
        trailingIcon = {
            if (isErrorUnitWeight) {
                Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
            } else {
                Icon(painter = painterResource(id = R.drawable.baseline_calculate_24),
                    contentDescription = "Calculator Icon",
                    modifier = Modifier.clickable {
                        val calculatorIntent = Intent(Intent.ACTION_MAIN).apply {
                            addCategory(Intent.CATEGORY_APP_CALCULATOR)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        try {
                            startActivity(context, calculatorIntent, null)
                        } catch (e: ActivityNotFoundException) {
                            // Calculator app not found; handle exception
                            Toast.makeText(
                                context, "Calculator app not found", Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            }
        },
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.kg),
                contentDescription = "Calculator Icon",
                modifier = Modifier
                    .clickable {
                        if (unitWeightInput.isEmpty()) {
                            return@clickable
                        } else {
                            val unitWeightInKg = ((unitWeightInput.toDoubleOrNull() ?: 0.0) * 0.45)
                            val formattedNumber = String
                                .format("%.2f", unitWeightInKg)
                                .toDouble()
                            Toast
                                .makeText(
                                    context,
                                    "Unit weight is $formattedNumber kg",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            unitWeightInput = formattedNumber.toString()
                        }
                    }
                    .size(24.dp))
        },
        label = { Text("Unit weight (kg)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
        ),
        modifier = Modifier.width(280.dp)
    )

    Spacer(modifier = Modifier.height(18.dp))

    OutlinedTextField(
        isError = isErrorAvailableIngredients,
        value = availableIngredientInput,
        onValueChange = {
            availableIngredientInput = it
            validate()
            if (isErrorAvailableIngredients) {
                availableIngredientInput = ""
            }
        },
        supportingText = { isError(isErrorAvailableIngredients) },
        trailingIcon = {
            if (isErrorAvailableIngredients) {
                Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
            }
        },
        label = { Text("Ingredient (units)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ),
        modifier = Modifier.width(280.dp)
    )

    fun numberFormatter(number: Double): String {
        return String.format("%.2f", number)
    }

    val requiredAmount = if (totalBatchesWeight != 0.0 && unitWeightInput.isNotEmpty()) "You need ${
        numberFormatter(
            ingredientRequired
        )
    } units or ${numberFormatter(totalBatchesWeight)} kg"
    else if (totalBatchesWeight != 0.0 && unitWeightInput.isEmpty()) "You need ${
        numberFormatter(
            totalBatchesWeight
        )
    } kg"
    else ""

    val batchesCanBeDone = if (availableIngredient != 0.0 && batchWeight != 0.0) "You can make ${
        numberFormatter(
            availableIngredient / batchWeight
        )
    } batches"
    else ""

    Text(
        requiredAmount,
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp),
        textAlign = TextAlign.Center
    )
    Text(
        batchesCanBeDone,
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreviewLight() {
    BatchesCalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(), tonalElevation = 15.dp
        ) {
            BatchesCalculatorScreen()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreviewNight() {
    BatchesCalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(), tonalElevation = 15.dp
        ) {
            BatchesCalculatorScreen()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun SplashScreenPreviewLight() {
    BatchesCalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(), tonalElevation = 15.dp
        ) {
            SplashScreen()
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SplashScreenPreviewNight() {
    BatchesCalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(), tonalElevation = 15.dp
        ) {
            SplashScreen()
        }
    }
}