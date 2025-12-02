package com.juno.colorteachingaids.ui.launch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juno.colorteachingaids.R
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import com.juno.colorteachingaids.ui.theme.PrimaryAccentLight
import com.juno.colorteachingaids.ui.theme.SecondaryAccentLight
import com.juno.colorteachingaids.ui.theme.SecondaryTextLight

@Composable
fun LaunchScreen(
    onNavigateToNewTeacher: () -> Unit,
    onNavigateToExistingTeacher: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Color Teaching Aids",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "For Special Learners",
            fontSize = 16.sp,
            color = SecondaryTextLight
        )
        Spacer(modifier = Modifier.height(72.dp))
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "App Icon",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNavigateToNewTeacher,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccentLight)
        ) {
            Text(text = "I'm a New Teacher", fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onNavigateToExistingTeacher,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, SecondaryAccentLight)
        ) {
            Text(
                text = "I've Used This App Before",
                color = SecondaryAccentLight,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LaunchScreenPreview() {
    ColorTeachingAidsTheme {
        LaunchScreen(onNavigateToNewTeacher = {}, onNavigateToExistingTeacher = {})
    }
}
