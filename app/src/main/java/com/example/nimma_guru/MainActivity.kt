package com.example.nimmaguru

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NimmaGuruApp()
        }
    }
}

data class Guru(
    val id: String = "",
    val name: String = "",
    val skill: String = "",
    val village: String = "",
    val freeHours: String = "",
    val experience: String = ""
)
@Composable
fun AppDrawerContent(userName: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F8F2))
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = userName.take(1).uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(
                            text = userName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "ಜ್ಞಾನ ದಾನ | Knowledge Sharing",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text =
                        "Connecting Retired Professionals with Village Students for Free Mentorship.",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "📌 Community Overview",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )

        Spacer(modifier = Modifier.height(16.dp))

        DrawerInfoCard("🎓 Total Mentors", "24 Active Gurus")
        DrawerInfoCard("🏆 Top Mentor", "Ramesh Sir | ರಮೇಶ್ ಸರ್")
        DrawerInfoCard("📍 Learning Centers", "Samudaya Bhavana")
        DrawerInfoCard("📅 Weekend Sessions", "Saturday & Sunday")
        DrawerInfoCard("🌐 Languages", "Kannada + English")
        DrawerInfoCard("❤️ Mission", "Free Education & Knowledge Sharing")

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E9)
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "📖 About Nimma Guru",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text =
                        "Nimma Guru connects retired teachers, engineers, and skilled professionals with village students for free mentorship and weekend learning.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text =
                        "ನಿಮ್ಮ ಗುರು ಗ್ರಾಮೀಣ ವಿದ್ಯಾರ್ಥಿಗಳಿಗೆ ಉಚಿತ ಮಾರ್ಗದರ್ಶನ ಮತ್ತು ಜ್ಞಾನ ಹಂಚಿಕೆಯನ್ನು ಒದಗಿಸುವ ಸಮುದಾಯ ಶಿಕ್ಷಣ ವೇದಿಕೆ.",
                    fontSize = 14.sp,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DrawerInfoCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NimmaGuruApp() {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    val dbRef = FirebaseDatabase
        .getInstance()
        .reference
        .child("gurus")

    var userName by remember {
        mutableStateOf("")
    }

    var entered by remember {
        mutableStateOf(false)
    }

    var guruName by remember {
        mutableStateOf("")
    }

    var skill by remember {
        mutableStateOf("")
    }

    var village by remember {
        mutableStateOf("")
    }

    var freeHours by remember {
        mutableStateOf("")
    }

    var experience by remember {
        mutableStateOf("")
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedSkill by remember {
        mutableStateOf("All")
    }

    val gurus = remember {
        mutableStateListOf<Guru>()
    }

    val filteredList = gurus.filter {

        (selectedSkill == "All" ||
                it.skill.contains(selectedSkill, true))

                &&

                (
                        it.name.contains(searchText, true)
                                ||
                                it.skill.contains(searchText, true)
                        )
    }

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(
                        "👤 $userName",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("📚 Total Mentors : 24")
                    Text("🏆 Top Guru : Ramesh Sir")
                    Text("📍 Village Learning Centers")
                    Text("🕒 Weekend Classes")
                    Text("🌱 Knowledge Sharing Mission")
                    Text("📖 Kannada + English Support")
                    Text("❤️ Community Education")

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Nimma Guru connects retired professionals with village students for free mentorship and learning support.",
                        fontSize = 14.sp
                    )
                }
            }
        }

    ) {

        if (!entered) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.88f),
                    shape = RoundedCornerShape(28.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "🌱 Nimma Guru",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "ಜ್ಞಾನ ದಾನ | Knowledge Sharing",
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = userName,
                            onValueChange = {
                                userName = it
                            },
                            label = {
                                Text("Enter Username")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {

                                if (userName.isNotEmpty()) {
                                    entered = true
                                }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32)
                            )
                        ) {

                            Text(
                                "ENTER APP",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        else {

            Scaffold(

                topBar = {

                    TopAppBar(

                        title = {
                            Text("Nimma-Guru")
                        },

                        navigationIcon = {

                            IconButton(
                                onClick = {

                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {

                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }

            ) { padding ->

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Color(0xFFE8F5E9))
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E7D32)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Text(
                                "🌱 Nimma Guru",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Knowledge Sharing Platform",
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "Connecting retired professionals with village students for free learning support.",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Welcome $userName 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF3E5F5)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Text(
                                "Guru Profile",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = guruName,
                                onValueChange = {
                                    guruName = it
                                },
                                label = {
                                    Text("Guru Name")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = skill,
                                onValueChange = {
                                    skill = it
                                },
                                label = {
                                    Text("Skill")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = village,
                                onValueChange = {
                                    village = it
                                },
                                label = {
                                    Text("Village")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = freeHours,
                                onValueChange = {
                                    freeHours = it
                                },
                                label = {
                                    Text("Free Hours")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = experience,
                                onValueChange = {
                                    experience = it
                                },
                                label = {
                                    Text("Experience")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Button(

                                onClick = {

                                    if (
                                        guruName.isNotEmpty()
                                    ) {

                                        val id =
                                            dbRef.push().key ?: ""

                                        val guru = Guru(
                                            id,
                                            guruName,
                                            skill,
                                            village,
                                            freeHours,
                                            experience
                                        )

                                        dbRef.child(id)
                                            .setValue(guru)

                                        gurus.add(guru)

                                        Toast.makeText(
                                            context,
                                            "Guru Saved",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        guruName = ""
                                        skill = ""
                                        village = ""
                                        freeHours = ""
                                        experience = ""
                                    }
                                },

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),

                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2E7D32)
                                )
                            ) {

                                Text(
                                    "Save Guru",
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        },
                        label = {
                            Text("Search Mentor")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Skill Filter",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        listOf(
                            "All",
                            "Math",
                            "Science",
                            "English",
                            "Kannada"
                        ).forEach {

                            FilterChip(
                                selected = selectedSkill == it,
                                onClick = {
                                    selectedSkill = it
                                },
                                label = {
                                    Text(it)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3E0)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Text(
                                "🏆 Guru Wall Of Fame",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(
                                "⭐ Ramesh Sir - Mathematics Expert"
                            )

                            Text(
                                "⭐ Kavya Teacher - Science Mentor"
                            )

                            Text(
                                "⭐ Basappa Anna - Carpentry Skills"
                            )

                            Text(
                                "⭐ Lakshmi Madam - English Grammar"
                            )

                            Text(
                                "⭐ Veeresh Sir - Kannada Literature"
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                "Community mentors helping village students grow through free education and weekend mentorship sessions.",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE3F2FD)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                "📅 Weekend Classes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                "Saturday - Math Workshop"
                            )

                            Text(
                                "Sunday - Science Class"
                            )

                            Text(
                                "Sunday - English Speaking"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        "Available Gurus",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.height(600.dp)
                    ) {

                        items(filteredList) { guru ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),

                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(18.dp)
                                ) {

                                    Text(
                                        guru.name,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Text(
                                        "📘 Skill : ${guru.skill}"
                                    )

                                    Text(
                                        "📍 Village : ${guru.village}"
                                    )

                                    Text(
                                        "🕒 Free Hours : ${guru.freeHours}"
                                    )

                                    Text(
                                        "🏅 Experience : ${guru.experience}"
                                    )

                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )

                                    Button(
                                        onClick = {

                                            Toast.makeText(
                                                context,
                                                "Thank You Sent ❤️",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },

                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFF6A1B9A
                                            )
                                        )
                                    ) {

                                        Text(
                                            "Send Appreciation"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}