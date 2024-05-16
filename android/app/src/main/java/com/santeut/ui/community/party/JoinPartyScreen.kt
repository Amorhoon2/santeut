package com.santeut.ui.community.party

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.santeut.data.model.response.PartyResponse
import com.santeut.ui.party.PartyViewModel

@Composable
fun JoinPartyScreen(
    guildId: Int?,
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    val partyList by partyViewModel.partyList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        partyViewModel.getPartyList(guildId = guildId, name = null, start = null, end = null)
    }

    Column {
        PartySearchBar(
            partyViewModel,
            onSearchTextChanged = {},
            onClickSearch = {},
            onClickFilter = {}
        )

        if (partyList.isEmpty()) {
            Text(text = "소모임이 없습니다")
        } else {
            LazyColumn(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                items(partyList) { party ->
                    PartyCard(party)
                }
            }
        }

    }
}

@Composable
fun PartyCard(party: PartyResponse, partyViewModel: PartyViewModel = hiltViewModel()) {
    Card {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = party.partyName, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "일정"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = party.schedule)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "위치"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = party.place)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardDoubleArrowUp,
                        contentDescription = "산"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = party.mountainName)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "인원 수"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${party.curPeople} / ${party.maxPeople} 명")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // isMember= true 면 버튼 색 변경
            Button(
                onClick = { partyViewModel.joinParty(party.partyId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("참가하기")
            }
        }
    }
}


@Composable
fun PartySearchBar(
    partyViewModel: PartyViewModel,
    onSearchTextChanged: (String) -> Unit,
    onClickSearch: () -> Unit,
    onClickFilter: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            textStyle = TextStyle(fontSize = 12.sp),
            value = name,
            onValueChange = { text ->
                onSearchTextChanged(text)
            },
            placeholder = {
                androidx.compose.material.Text(
                    text = "어느 소모임을 찾으시나요?"
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done // 완료 액션 지정
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF678C40),
                unfocusedContainerColor = Color(0xFFFBF9ED),
                focusedBorderColor = Color(0xFF678C40),
                focusedContainerColor = Color(0xFFFBF9ED),
            ),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        partyViewModel.getPartyList(null, name, null, null)
                    }
                )
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            imageVector = Icons.Outlined.FilterAlt,
            contentDescription = "필터",
            modifier = Modifier.clickable { onClickFilter() }
        )
        Spacer(modifier = Modifier.width(6.dp))
    }
}
