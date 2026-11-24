package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.LibraryBooks
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.i18n.AppStrings
import com.example.i18n.MysticStrings
import com.example.model.AiToolTab
import com.example.model.AppLanguage
import com.example.model.QuizAttempt
import com.example.model.StudyNote
import com.example.model.Subject
import com.example.ui.components.ClassSelectorRow
import com.example.ui.components.SubjectPill
import com.example.ui.components.subjectIcon

@Composable
fun HomeScreen(
    strings: MysticStrings,
    language: AppLanguage,
    preferredClass: Int,
    notes: List<StudyNote>,
    recentQuizzes: List<QuizAttempt>,
    onSelectPreferredClass: (Int) -> Unit,
    onSubmitHomeQuestion: (String) -> Unit,
    onOpenSubjectNotes: (Subject) -> Unit,
    onOpenNotesTab: () -> Unit,
    onOpenClassLessons: () -> Unit,
    onOpenAiTool: (AiToolTab) -> Unit,
    onOpenQuizTab: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookmarkedCount = notes.count { it.isBookmarked }
    var homeQuestion by remember { mutableStateOf("") }

    fun submitQuestion() {
        val trimmed = homeQuestion.trim()
        if (trimmed.isNotEmpty()) {
            homeQuestion = ""
            onSubmitHomeQuestion(trimmed)
        } else {
            onOpenAiTool(AiToolTab.ASSISTANT)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Mockup-Matched Hero Card + Interactive Ask Box + 2x3 6-Tile Grid
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0A1730),
                                Color(0xFF071124)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header: Brand Logo + Name on left, "AI STUDY PLATFORM" badge on right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_mystic_logo),
                                contentDescription = stringResource(id = R.string.mystic_logo_desc),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.brandName,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            color = Color(0xFF064E3B),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = strings.platformBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF34D399),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    // Bold Hero Title & Tagline
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = strings.heroTitle,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 26.sp,
                                lineHeight = 32.sp
                            ),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.heroSubtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // Interactive "What do you want to learn today?" Box with White Pill Input
                    Surface(
                        color = Color(0xFF102242),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, Color(0xFF1E3A6A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = strings.promptCardTitle,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = strings.promptCardSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                            OutlinedTextField(
                                value = homeQuestion,
                                onValueChange = { homeQuestion = it },
                                placeholder = {
                                    Text(
                                        text = strings.askInputPlaceholder,
                                        color = Color(0xFF64748B),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = { submitQuestion() },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color(0xFF00E5FF),
                                            contentColor = Color(0xFF071124)
                                        ),
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .size(36.dp)
                                            .testTag("home_ask_submit_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.Send,
                                            contentDescription = strings.send,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { submitQuestion() }),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color(0xFF071124),
                                    unfocusedTextColor = Color(0xFF071124),
                                    focusedBorderColor = Color(0xFF00E5FF),
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color(0xFF071124)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("home_ask_input")
                            )
                        }
                    }

                    // 2x3 Quick-Launch Feature Grid (6 Tiles)
                    val tiles = listOf(
                        HomeGridTile(
                            title = strings.tileStudyNotes,
                            icon = Icons.Rounded.LibraryBooks,
                            iconTint = Color(0xFF38BDF8),
                            tag = "tile_study_notes",
                            onClick = onOpenNotesTab
                        ),
                        HomeGridTile(
                            title = strings.tileAiAssistant,
                            icon = Icons.Rounded.SmartToy,
                            iconTint = Color(0xFF00E5FF),
                            tag = "tile_ai_assistant",
                            onClick = { onOpenAiTool(AiToolTab.ASSISTANT) }
                        ),
                        HomeGridTile(
                            title = strings.tileMathSolver,
                            icon = Icons.Rounded.Calculate,
                            iconTint = Color(0xFFFB7185),
                            tag = "tile_math_solver",
                            onClick = { onOpenAiTool(AiToolTab.MATH_SOLVER) }
                        ),
                        HomeGridTile(
                            title = strings.tileQuizMaker,
                            icon = Icons.Rounded.Quiz,
                            iconTint = Color(0xFFFBBF24),
                            tag = "tile_quiz_maker",
                            onClick = onOpenQuizTab
                        ),
                        HomeGridTile(
                            title = strings.tileEnglishTutor,
                            icon = Icons.Rounded.Translate,
                            iconTint = Color(0xFFA78BFA),
                            tag = "tile_english_tutor",
                            onClick = { onOpenAiTool(AiToolTab.GRAMMAR_TUTOR) }
                        ),
                        HomeGridTile(
                            title = strings.tileClassLessons,
                            icon = Icons.Rounded.AutoStories,
                            iconTint = Color(0xFF34D399),
                            tag = "tile_class_lessons",
                            onClick = onOpenClassLessons
                        )
                    )

                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val columns = if (maxWidth >= 560.dp) 3 else 2
                        val rows = tiles.chunked(columns)
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            rows.forEach { rowTiles ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowTiles.forEach { tile ->
                                        MockupGridTileCard(
                                            tile = tile,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowTiles.size < columns) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Active Class Selector (1 - 10)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${strings.selectYourClass} (${AppStrings.classLabel(preferredClass, language)})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ClassSelectorRow(
                    selectedClass = preferredClass,
                    includeAllOption = false,
                    allLabel = strings.allClasses,
                    language = language,
                    onSelectClass = { cls -> if (cls != null) onSelectPreferredClass(cls) }
                )
            }
        }

        // 3. Subject Categories (Math, English, Bangla, Science, ICT)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = strings.subjectsHeader,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(Subject.entries) { subject ->
                        val count = notes.count { it.subject == subject }
                        SubjectDashboardCard(
                            subject = subject,
                            language = language,
                            noteCount = count,
                            notesCountLabel = strings.notesCountLabel,
                            onClick = { onOpenSubjectNotes(subject) }
                        )
                    }
                }
            }
        }

        // 4. Homework Helper Shortcut
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.45f)),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onOpenAiTool(AiToolTab.HOMEWORK_HELPER) }
                    .testTag("tool_card_homework")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFBBF24).copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EditNote,
                            contentDescription = strings.toolHomework,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.toolHomework,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = strings.toolHomeworkDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = strings.openTool,
                        tint = Color(0xFFFBBF24)
                    )
                }
            }
        }

        // 5. Study Snapshot & Recent Quiz Attempts
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = strings.recentProgressHeader,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SnapshotStatCard(
                        value = notes.size.toString(),
                        label = strings.totalNotesStat,
                        accent = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    SnapshotStatCard(
                        value = bookmarkedCount.toString(),
                        label = strings.bookmarksStat,
                        accent = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    SnapshotStatCard(
                        value = recentQuizzes.size.toString(),
                        label = strings.quizzesStat,
                        accent = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }

                recentQuizzes.take(3).forEach { attempt ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                SubjectPill(subject = attempt.subject, language = language)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = attempt.topic,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "${attempt.score}/${attempt.totalQuestions}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class HomeGridTile(
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val tag: String,
    val onClick: () -> Unit
)

@Composable
private fun MockupGridTileCard(
    tile: HomeGridTile,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF0C1C38),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF1D345D)),
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = tile.onClick)
            .testTag(tile.tag)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tile.iconTint.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tile.icon,
                    contentDescription = tile.title,
                    tint = tile.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = tile.title,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SubjectDashboardCard(
    subject: Subject,
    language: AppLanguage,
    noteCount: Int,
    notesCountLabel: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, subject.accentColor.copy(alpha = 0.45f)),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(154.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .testTag("subject_card_${subject.id.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(subject.accentColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = subjectIcon(subject),
                    contentDescription = subject.label(language),
                    tint = subject.accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = subject.label(language),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$noteCount $notesCountLabel",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SnapshotStatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = accent,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
