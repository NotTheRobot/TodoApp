package com.nottherobot.todoapp.ui.theme

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MyColor(var myField: Long = 0)

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ColorsPreviewLight(){
    AppTheme{
        val fields = AppTheme.colors.javaClass.declaredFields

        for(f in fields){
            f.isAccessible = true
            val destColor: Color = Color(0L)
            val destColorValue = destColor.javaClass.getDeclaredField("value")
            destColorValue.isAccessible = true

            f.get(destColor)

            ColorItem(
                name = f.name,
                color = destColor
            )
        }
    }
}

@Composable
fun ColorItem(
    name: String,
    color: Color
){
    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .height(72.dp)
            .width(148.dp)
            .background(color)
    ){
        Text(
            text = name,
            style = AppTheme.type.title
        )
    }
}
