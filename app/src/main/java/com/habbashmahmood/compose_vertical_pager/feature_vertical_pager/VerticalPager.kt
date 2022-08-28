package com.habbashmahmood.compose_vertical_pager.feature_vertical_pager

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.habbashmahmood.compose_vertical_pager.R
import com.habbashmahmood.compose_vertical_pager.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
class VerticalPager : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mContext = LocalContext.current
            val scope = rememberCoroutineScope()

            ComposeVerticalPagerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = rememberPagerState()
                    Column {
                        VerticalPager(
                            state = state,
                            count = detailsList.size, modifier = Modifier
                                .fillMaxSize()
                        ) { page ->
                            PagerLayout(page, mContext, state, scope)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PagerLayout(
        page: Int,
        mContext: Context,
        state: PagerState,
        scope: CoroutineScope,
    ) {
        // Parent Layout to draw Pager Slides
        Box {

            // Background image for each slide
            Image(
                painter = painterResource(detailsList[page].image),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Row to display 'SKIP' on the top right corner of screen
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ){
                Text(
                    text = "SKIP",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            Toast
                                .makeText(mContext, "Skip", Toast.LENGTH_SHORT)
                                .show()
                        },
                )
            }

            // Box Layout for Slide Content
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {

                // Row to display textual content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 120.dp)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Text(
                                text = detailsList[page].title1,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,

                                )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = detailsList[page].title2,
                                fontSize = 30.sp,
                                color = black54,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .width(260.dp)
                                .padding(top = 20.dp)
                        ) {
                            Text(
                                text = detailsList[page].description,
                                fontSize = 14.sp,
                                color = textColor2,
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 40.dp),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(60.dp)
                                    .background(
                                        color = mainColor,
                                        shape = MaterialTheme.shapes.medium
                                    ),
                                onClick = {
                                    if (page < detailsList.size - 1) {
                                        scope.launch {
                                            state.scrollToPage(state.currentPage + 1)
                                        }
                                    } else {
                                        Toast
                                            .makeText(mContext, "Done", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
                            ) {
                                Text(
                                    text = if (page < detailsList.size - 1) "NEXT" else "DONE",
                                    style = MaterialTheme.typography.subtitle2,
                                    color = Color.White,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Light,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(15.dp))

                                Icon(
                                    imageVector = Icons.Default.DoubleArrow,
                                    contentDescription = "Sort",
                                    tint = Color.White,
                                )
                            }

                        }
                    }

                    // Function to display vertical dots
                    VerticalDotsIndicator(
                        totalDots = detailsList.size,
                        selectedIndex = page,
                        selectedColor = mainColor,
                        unSelectedColor = mainColor.copy(alpha = 0.3f),
                    )
                }
            }
        }
    }

    @Composable
    fun VerticalDotsIndicator(
        totalDots: Int,
        selectedIndex: Int,
        selectedColor: Color,
        unSelectedColor: Color,
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            items(totalDots) { index ->
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(if (index == selectedIndex) 25.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (index == selectedIndex) selectedColor else unSelectedColor)
                )
                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
        }
    }
}


data class Details(
    val image: Int,
    val title1: String,
    val title2: String,
    val description: String
)

private val detailsList = listOf(
    Details(
        image = R.drawable.ic_onboarding2,
        title1 = "Get",
        title2 = "Burn",
        description = "Let’s keep burning, to achieve yours goals, it hurts only temporarily, if you give up now you will be in pain forever"
    ),
    Details(
        image = R.drawable.ic_onboarding3,
        title1 = "Eat",
        title2 = "Well",
        description = "Let's start a healthy lifestyle with us, we can determine your diet every day. healthy eating is fun"
    ),
    Details(
        image = R.drawable.ic_onboarding1,
        title1 = "Track",
        title2 = "Goal",
        description = "Don't worry if you have trouble determining your goals, We can help you determine your goals and track your goals"
    ),
    Details(
        image = R.drawable.ic_onboarding4,
        title1 = "Improve",
        title2 = "Sleep",
        description = "Improve the quality of your sleep with us, good quality sleep can bring a good mood in the morning"
    ),
)

