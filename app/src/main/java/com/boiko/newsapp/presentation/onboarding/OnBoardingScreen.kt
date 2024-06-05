package com.boiko.newsapp.presentation.onboarding

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.boiko.newsapp.data.remote.auth.AuthResult
import com.boiko.newsapp.presentation.Dimens.MediumPadding2
import com.boiko.newsapp.presentation.Dimens.PageIndicatorWidth
import com.boiko.newsapp.presentation.common.NewsButton
import com.boiko.newsapp.presentation.common.NewsTextButton
import com.boiko.newsapp.presentation.navgraph.Route
import com.boiko.newsapp.presentation.onboarding.components.OnBoardingPage
import com.boiko.newsapp.presentation.onboarding.components.PageIndicator
import com.boiko.newsapp.presentation.onboarding.components.SignInBottomSheet
import com.boiko.newsapp.presentation.onboarding.components.SignUpBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel,
    navigate: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
    val isClickOnSignIn = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect {result ->
            when(result) {
                is AuthResult.Authorized -> {
                    sheetState.hide()
                    viewModel.onEvent(OnBoardingEvent.SaveAppEntry)
                    navigate(Route.NewsNavigatorScreen.route)
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context, "You're not authorized", Toast.LENGTH_LONG).show()
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(context, "An unknown error occurred", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetDragHandle = { BottomSheetDefaults.DragHandle(color = Color.Transparent)},
        sheetContent = {
            if (isClickOnSignIn.value) {
                SignInBottomSheet(
                    sheetState = sheetState,
                    isClickOnSignIn = isClickOnSignIn,
                    viewModel = viewModel
                )
            }
            else {
                SignUpBottomSheet(
                    sheetState = sheetState,
                    isClickOnSignIn = isClickOnSignIn,
                    viewModel = viewModel
                )
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState(initialPage = 0) {
                pages.size
            }

            val buttonState = remember {
                derivedStateOf {
                    when(pagerState.currentPage) {
                        0 -> listOf("", "Next")
                        1 -> listOf("Back", "Next")
                        2 -> listOf("Back", "Get Started")
                        else -> listOf("", "")
                    }
                }
            }

            HorizontalPager(state = pagerState) { index ->
                OnBoardingPage(page = pages[index])
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MediumPadding2)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PageIndicator(
                    pageSize = pages.size,
                    selectedPage = pagerState.currentPage,
                    modifier = Modifier.width(PageIndicatorWidth)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {

                    val scope = rememberCoroutineScope()

                    if (buttonState.value[0].isNotEmpty()) {
                        NewsTextButton(text = buttonState.value[0]) {
                            scope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                            }
                        }
                    }
                    NewsButton(text = buttonState.value[1]) {
                        scope.launch {
                            if (pagerState.currentPage == 2) {
                                scope.launch { sheetState.expand() }
                                //event(OnBoardingEvent.SaveAppEntry)
                            } else {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + 1
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}