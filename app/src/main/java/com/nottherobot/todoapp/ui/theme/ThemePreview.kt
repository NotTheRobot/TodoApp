package com.nottherobot.todoapp.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 2000
)
@Composable
fun ColorsPreviewLight(){
    AppTheme{
        val fields = AppTheme.colors.javaClass.declaredFields

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(AppTheme.colors.backPrimary)
        ) {
            for (f in fields) {
                f.isAccessible = true
                if (f.type.simpleName != "List") {
                    val colorLong: Any? = f.get(AppTheme.colors)
                    val myColor = Color(colorLong.toString().toLong().toULong())

                    ColorItem(
                        name = f.name,
                        modifier = Modifier
                            .background(color = myColor),
                    )
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 2000
)
@Composable
fun ColorsPreviewDark() {
    AppTheme {
        val fields = AppTheme.colors.javaClass.declaredFields

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(AppTheme.colors.backPrimary)
        ) {
            for (f in fields) {
                f.isAccessible = true
                if (f.type.simpleName != "List") {
                    val colorLong: Any? = f.get(AppTheme.colors)
                    val myColor = Color(colorLong.toString().toLong().toULong())

                    ColorItem(
                        name = f.name,
                        modifier = Modifier
                            .background(color = myColor),
                    )
                }
            }
        }
    }
}

@Composable
fun ColorItem(
    name: String,
    modifier: Modifier = Modifier,
){
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier
            .height(100.dp)
            .width(320.dp)
    ) {
        Box(
            Modifier.background(Color.White)
        ) {
            Text(
                fontFamily = FontFamily.Monospace,
                text = name,
                fontSize = 18.sp,
                letterSpacing = 3.sp
            )
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Composable
fun TypographyPreview() {
    AppTheme {
        val fields = AppTheme.type.javaClass.declaredFields

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            for (f in fields) {
                f.isAccessible = true
                val typeRaw: Any? = f.get(AppTheme.type)
                if (typeRaw?.javaClass?.isInstance(TextStyle.Default) == true) {
                    val textStyle = TextStyle.Default.javaClass.cast(typeRaw)
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .width(320.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = "f.name - " + textStyle!!.fontSize.value + "/" + textStyle.lineHeight.value,
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}
