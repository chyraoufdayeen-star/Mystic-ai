package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.repository.NotesRepository
import com.example.i18n.AppStrings
import com.example.i18n.MysticStrings
import com.example.model.AppLanguage
import com.example.model.GeneratedQuiz
import com.example.model.QuizAttempt
import com.example.model.Subject
import com.example.model.UiState
import com.example.ui.components.ClassSelectorRow
import com.example.ui.components.ErrorStateCard
import com.example.ui.components.FirebaseNoticeBanner
import com.example.ui.components.LoadingPulseCard
import com.example.ui.components.SamplePreviewTag
import com.example.ui.components.SubjectPill
import com.example.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    strings: MysticStrings,
    language: AppLanguage,
    quizViewModel: QuizViewModel,
    notesRepository: NotesRepository,
    recentQuizzes: List<QuizAttempt>,
    modifier: Modifier = Modifier
) {
    val selectedClass by quizViewModel.selectedClass.collectAsStateWithLifecycle()
    val selectedSubject by quizViewModel.selectedSubject.collectAsStateWithLifecycle()
    val topic by quizViewModel.topic.collectAsStateWithLifecycle()
    val questionCount by quizViewModel.questionCount.collectAsStateWithLifecycle()
    val difficulty by quizViewModel.difficulty.collectAsStateWithLifecycle()
    val quizState by quizViewModel.quizState.collectAsStateWithLifecycle()
    val runner by quizViewModel.runnerState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            FirebaseNoticeBanner(strings = strings)
        }

        when (val state = quizState) {
            is UiState.Idle, is UiState.Error -> {
                if (state is UiState.Error) {
                    item {
                        ErrorStateCard(
                            message = state.message,
                            retryLabel = strings.retry,
                            onRetry = { quizViewModel.generateQuiz(language) }
                        )
                    }
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = strings.quizSetupTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = strings.selectYourClass,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            ClassSelectorRow(
                                selectedClass = selectedClass,
                                includeAllOption = false,
                                allLabel = strings.allClasses,
                                language = language,
                                onSelectClass = { cls -> if (cls != null) quizViewModel.setClassLevel(cls) }
                            )

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(Subject.entries) { subj ->
                                    FilterChip(
                                        selected = selectedSubject == subj,
                                        onClick = { quizViewModel.setSubject(subj) },
                                        label = { Text(subj.label(language)) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = subj.accentColor,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        modifier = Modifier.testTag("quiz_subject_${subj.id.lowercase()}")
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = topic,
                                onValueChange = quizViewModel::setTopic,
                                placeholder = { Text(strings.topicPlaceholder) },
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_topic_input")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = strings.questionCountLabel,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                listOf(3, 5).forEach { cnt ->
                                    FilterChip(
                                        selected = questionCount == cnt,
                                        onClick = { quizViewModel.setQuestionCount(cnt) },
                                        label = { Text("$cnt") },
                                        modifier = Modifier.testTag("quiz_count_$cnt")
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = strings.difficultyLabel,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                listOf("Easy", "Medium", "Hard").forEach { diff ->
                                    FilterChip(
                                        selected = difficulty == diff,
                                        onClick = { quizViewModel.setDifficulty(diff) },
                                        label = { Text(diff) },
                                        modifier = Modifier.testTag("quiz_diff_${diff.lowercase()}")
                                    )
                                }
                            }

                            Button(
                                onClick = { quizViewModel.generateQuiz(language) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .minimumInteractiveComponentSize()
                                    .testTag("generate_quiz_button")
                            ) {
                                Icon(Icons.Rounded.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(strings.generateQuizBtn)
                            }
                        }
                    }
                }
            }

            is UiState.Loading -> {
                item { LoadingPulseCard(message = strings.loadingAi) }
            }

            is UiState.Success<GeneratedQuiz> -> {
                val quiz = state.data
                if (runner.isFinished) {
                    item {
                        val percent = ((runner.correctCount.toFloat() / quiz.questions.size.coerceAtLeast(1)) * 100).toInt()
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quiz_summary_card")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.EmojiEvents,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(56.dp)
                                )
                                Text(
                                    text = strings.quizCompletedTitle,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${runner.correctCount} / ${quiz.questions.size} ($percent%)",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    SubjectPill(subject = quiz.subject, language = language)
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = AppStrings.classLabel(quiz.classLevel, language),
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = { quizViewModel.resetQuiz() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .minimumInteractiveComponentSize()
                                        .testTag("retake_quiz_button")
                                ) {
                                    Text(strings.retakeQuiz)
                                }
                            }
                        }
                    }
                } else {
                    val q = quiz.questions.getOrNull(runner.currentIndex)
                    if (q != null) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${runner.currentIndex + 1} / ${quiz.questions.size}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (quiz.isSamplePreview) {
                                            SamplePreviewTag(label = strings.samplePreviewBadge)
                                        }
                                    }

                                    LinearProgressIndicator(
                                        progress = { (runner.currentIndex + 1).toFloat() / quiz.questions.size.coerceAtLeast(1) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                    )

                                    Text(
                                        text = q.question,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    q.options.forEachIndexed { idx, optText ->
                                        val isSelected = runner.selectedOptionIndex == idx
                                        val isCorrectOption = idx == q.correctIndex
                                        val borderColor = when {
                                            runner.isAnswerSubmitted && isCorrectOption -> MaterialTheme.colorScheme.tertiary
                                            runner.isAnswerSubmitted && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.error
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            else -> MaterialTheme.colorScheme.outline
                                        }
                                        val bgColor = when {
                                            runner.isAnswerSubmitted && isCorrectOption -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                                            runner.isAnswerSubmitted && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
                                            isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                        Surface(
                                            color = bgColor,
                                            shape = MaterialTheme.shapes.small,
                                            border = BorderStroke(1.5.dp, borderColor),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .minimumInteractiveComponentSize()
                                                .clip(MaterialTheme.shapes.small)
                                                .clickable(enabled = !runner.isAnswerSubmitted) {
                                                    quizViewModel.selectOption(idx)
                                                }
                                                .testTag("quiz_option_$idx")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "${'A' + idx}.",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = borderColor,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = optText,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                if (runner.isAnswerSubmitted && isCorrectOption) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.CheckCircle,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.tertiary,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (runner.isAnswerSubmitted) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                            shape = MaterialTheme.shapes.small,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = q.explanation,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(12.dp)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { quizViewModel.resetQuiz() },
                                            modifier = Modifier.minimumInteractiveComponentSize()
                                        ) {
                                            Text(strings.cancel)
                                        }
                                        Button(
                                            onClick = {
                                                if (!runner.isAnswerSubmitted) {
                                                    quizViewModel.submitCurrentAnswer(quiz)
                                                } else {
                                                    quizViewModel.nextOrFinish(quiz, notesRepository)
                                                }
                                            },
                                            enabled = runner.selectedOptionIndex != null,
                                            modifier = Modifier
                                                .weight(1f)
                                                .minimumInteractiveComponentSize()
                                                .testTag("quiz_action_button")
                                        ) {
                                            val label = when {
                                                !runner.isAnswerSubmitted -> strings.submitAnswer
                                                runner.currentIndex + 1 < quiz.questions.size -> strings.nextQuestion
                                                else -> strings.finishQuiz
                                            }
                                            Text(label)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recent Quiz History
        item {
            Text(
                text = strings.recentQuizzesTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (recentQuizzes.isEmpty()) {
            item {
                Text(
                    text = strings.noQuizzesYet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(recentQuizzes.take(6)) { attempt ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            SubjectPill(subject = attempt.subject, language = language)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${AppStrings.classLabel(attempt.classLevel, language)} • ${attempt.topic}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${attempt.score}/${attempt.totalQuestions}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
