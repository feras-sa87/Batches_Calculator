package com.example.batchescalculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.batchescalculator.ui.theme.BatchesCalculatorTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatchesCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    BatchesCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun BatchesCalculatorScreen() {
    Column(
        modifier = Modifier
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp, top = 40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        EditTextFieldsAndResult()
        Text(
            text = "Made with ❤️ by FERAS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextFieldsAndResult() {
    var batchesRequiredInput by remember {
        mutableStateOf("")
    }

    var portionWeightInput by remember {
        mutableStateOf("")
    }

    val totalBatchesWeight =
        (120 * (batchesRequiredInput.toIntOrNull() ?: 0) * (portionWeightInput.toIntOrNull()
            ?: 0)) / 1000.0

    val batchWeight = (120 * (portionWeightInput.toIntOrNull() ?: 0)) / 1000.0


    var unitWeightInput by remember {
        mutableStateOf("")
    }

    val ingredientRequired = (totalBatchesWeight / (unitWeightInput.toDoubleOrNull() ?: 0.0))

    var availableIngredientInput by remember {
        mutableStateOf("")
    }

    val availableIngredient =
        (availableIngredientInput.toIntOrNull() ?: 0) * (unitWeightInput.toDoubleOrNull() ?: 0.0)

    var isErrorBatches by remember {
        mutableStateOf(false)
    }

    var isErrorPortionWeight by remember {
        mutableStateOf(false)
    }

    var isErrorUnitWeight by remember {
        mutableStateOf(false)
    }

    var isErrorAvailableIngredients by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    @Composable
    fun isError(isError: Boolean, isUnitWeight: Boolean = false) {
        if (isError) {
            if (isUnitWeight) {
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
        isErrorBatches = batchesRequiredInput.length > 3
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
    }, supportingText = { isError(isErrorBatches) }, trailingIcon = {
        if (isErrorBatches) {
            Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
        }
    }, label = { Text("Batches") }, singleLine = true, keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
    ), modifier = Modifier
        .width(280.dp)
        .padding(bottom = 4.dp)
    )

    OutlinedTextField(isError = isErrorPortionWeight,
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
        modifier = Modifier
            .width(280.dp)
            .padding(bottom = 4.dp)
    )

    OutlinedTextField(isError = isErrorUnitWeight,
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
                text = "Press the calculator icon to convert the entered lb value to kg",
                fontSize = 10.sp
            )
        },
        trailingIcon = {
            if (isErrorUnitWeight) {
                Icon(Icons.Filled.Info, "error", tint = MaterialTheme.colorScheme.error)
            }
        },
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.baseline_calculate_24),
                contentDescription = "Calculator Icon",
                modifier = Modifier.clickable {
                        if (unitWeightInput.isEmpty()) {
                            return@clickable
                        } else {
                            val unitWeightInKg = ((unitWeightInput.toDoubleOrNull() ?: 0.0) * 0.45)
                            val formattedNumber = String.format("%.2f", unitWeightInKg).toDouble()
                            Toast.makeText(
                                    context,
                                    "Unit weight is $formattedNumber kg",
                                    Toast.LENGTH_SHORT
                                ).show()
                            unitWeightInput = formattedNumber.toString()
                        }
                    })
        },
        label = { Text("Unit weight (kg)") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .width(280.dp)
            .padding(bottom = 4.dp)
    )

    Spacer(modifier = Modifier.height(18.dp))

    OutlinedTextField(isError = isErrorAvailableIngredients,
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
        modifier = Modifier
            .width(280.dp)
            .padding(bottom = 4.dp)
    )

    val requiredAmount =
        if (totalBatchesWeight != 0.0 && unitWeightInput.isNotEmpty()) "You need ${ingredientRequired.roundToInt()} units or ${totalBatchesWeight.roundToInt()} kg"
        else if (totalBatchesWeight != 0.0 && unitWeightInput.isEmpty()) "You need ${totalBatchesWeight.roundToInt()} kg"
        else ""

    val batchesCanBeDone =
        if (availableIngredient != 0.0 && batchWeight != 0.0) "You can make ${(availableIngredient / batchWeight).roundToInt()} batches"
        else ""

    Text(
        requiredAmount,
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 12.dp),
        textAlign = TextAlign.Center
    )
    Text(
        batchesCanBeDone,
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 12.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BatchesCalculatorScreen()
}