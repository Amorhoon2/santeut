package com.santeut.ui.guild

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.santeut.ui.community.party.JoinPartyScreen
import com.santeut.ui.navigation.top.GuildTopBar
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun GuildScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val pages = listOf("정보", "게시판", "소모임", "랭킹")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val guild by guildViewModel.guild.observeAsState()

    LaunchedEffect(key1 = guildId) {
        guildViewModel.getGuild(guildId)
    }

    Scaffold {
        Column(modifier = Modifier.fillMaxWidth()) {
            guild?.let { it1 -> GuildTopBar(navController, it1) }
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                pages.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> GuildInfoScreen(guild)
//                    0 -> CameraUI ()
//                    1 -> GuildCommunityScreen()
//                    2 -> PartyListScreen()
                    1 -> GuildCommunityScreen(guildId, navController)
                    2 -> JoinPartyScreen(guildId)
                    3 -> GuildRankingScreen()
                    else -> Text("Unknown page")
                }
            }
        }
    }

}

