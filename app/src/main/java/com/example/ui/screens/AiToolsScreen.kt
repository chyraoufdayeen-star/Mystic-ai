package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Spellcheck
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.example.i18n.MysticStrings
import com.example.model.AiToolTab
import com.example.model.AppLanguage
import com.example.model.GrammarAnalysis
import com.example.model.HomeworkHelpResult
import com.example.model.MathSolution
import com.example.model.Subject
import com.example.model.UiState
import com.example.ui.components.ErrorStateCard
import com.example.ui.components.FirebaseNoticeBanner
import com.example.ui.components.LoadingPulseCard
import com.example.ui.components.PhotoAttachmentRow
import com.example.ui.components.SamplePreviewTag
import com.example.viewmodel.AiToolsViewModel

@Composable
fun AiToolsScreen(
    strings: MysticStrings,
    language: AppLanguage,
    preferredClass: Int,
    viewModel: AiToolsViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Horizontal AI Tool Switcher
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ToolTabChip(
                    selected = activeTab == AiToolTab.ASSISTANT,
                    label = strings.toolAssistant,
                    icon = Icons.Rounded.Psychology,
                    tag = "ai_tab_assistant",
                    onClick = { viewModel.selectTab(AiToolTab.ASSISTANT) }
                )
            }
            item {
                ToolTabChip(
                    selected = activeTab == AiToolTab.MATH_SOLVER,
                    label = strings.toolMath,
                    icon = Icons.Rounded.Calculate,
                    tag = "ai_tab_math",
                    onClick = { viewModel.selectTab(AiToolTab.MATH_SOLVER) }
                )
            }
            item {
                ToolTabChip(
                    selected = activeTab == AiToolTab.GRAMMAR_TUTOR,
                    label = strings.toolGrammar,
                    icon = Icons.Rounded.Spellcheck,
                    tag = "ai_tab_grammar",
                    onClick = { viewModel.selectTab(AiToolTab.GRAMMAR_TUTOR) }
                )
            }
            item {
                ToolTabChip(
                    selected = activeTab == AiToolTab.HOMEWORK_HELPER,
                    label = strings.toolHomework,
                    icon = Icons.Rounded.EditNote,
                    tag = "ai_tab_homework",
                    onClick = { viewModel.selectTab(AiToolTab.HOMEWORK_HELPER) }
                )
            }
        }

        FirebaseNoticeBanner(
            strings = strings,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (activeTab) {
            AiToolTab.ASSISTANT -> StudyAssistantPane(
                strings = strings,
                language = language,
                preferredClass = preferredClass,
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
            AiToolTab.MATH_SOLVER -> MathSolverPane(
                strings = strings,
                language = language,
                preferredClass = preferredClass,
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
            AiToolTab.GRAMMAR_TUTOR -> GrammarTutorPane(
                strings = strings,
                language = language,
                preferredClass = preferredClass,
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
            AiToolTab.HOMEWORK_HELPER -> HomeworkHelperPane(
                strings = strings,
                language = language,
                preferredClass = preferredClass,
                viewModel = viewModel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToolTabChip(
    selected: Boolean,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tag: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(17.dp))
        },
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.testTag(tag)
    )
}

@Composable
private fun StudyAssistantPane(
    strings: MysticStrings,
    language: AppLanguage,
    preferredClass: Int,
    viewModel: AiToolsViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val input by viewModel.assistantInput.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.assistantSubject.collectAsStateWithLifecycle()
    val photoUri by viewModel.assistantPhotoUri.collectAsStateWithLifecycle()
    val state by viewModel.assistantState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        // Optional Subject Context Chips + Clear Chat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedSubject == null,
                        onClick = { viewModel.selectAssistantSubject(null) },
                        label = { Text(strings.allSubjects) }
                    )
                }
                items(Subject.entries) { subj ->
                    FilterChip(
                        selected = selectedSubject == subj,
                        onClick = { viewModel.selectAssistantSubject(subj) },
                        label = { Text(subj.label(language)) }
                    )
                }
            }
            if (messages.isNotEmpty()) {
                IconButton(
                    onClick = { viewModel.clearChat() },
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteSweep,
                        contentDescription = strings.clearChat,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Chat Stream
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    val starterPrompts = if (language == AppLanguage.BN) {
                        listOf(
                            "সালোকসংশ্লেষণ প্রক্রিয়াটি সহজভাবে বুঝিয়ে দাও",
                            "পিথাগোরাসের উপপাদ্যের একটি উদাহরণ দাও",
                            "বাইনারি সংখ্যা কী এবং কোথায় ব্যবহৃত হয়?"
                        )
                    } else {
                        listOf(
                            "Explain photosynthesis in simple steps",
                            "Give me an example of Pythagoras theorem",
                            "How do binary numbers work in computers?"
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = strings.toolAssistantDesc,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        starterPrompts.forEach { prompt ->
                            AssistChip(
                                onClick = {
                                    viewModel.updateAssistantInput(prompt)
                                    viewModel.sendAssistantMessage(preferredClass, language)
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                label = { Text(prompt) }
                            )
                        }
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (msg.isUser) Alignment.End else Alignment.Start
                ) {
                    Surface(
                        color = if (msg.isUser) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            1.dp,
                            if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            if (!msg.isUser && msg.isSamplePreview) {
                                SamplePreviewTag(label = strings.samplePreviewBadge)
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            if (state is UiState.Loading) {
                item { LoadingPulseCard(message = strings.loadingAi) }
            }

            if (state is UiState.Error) {
                item {
                    ErrorStateCard(
                        message = (state as UiState.Error).message,
                        retryLabel = strings.retry,
                        onRetry = { viewModel.sendAssistantMessage(preferredClass, language) }
                    )
                }
            }
        }

        // Bottom Composer
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                PhotoAttachmentRow(
                    photoUri = photoUri,
                    attachLabel = strings.attachPhoto,
                    attachedLabel = strings.photoAttached,
                    removeLabel = strings.removePhoto,
                    onPhotoSelected = viewModel::setAssistantPhoto
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = viewModel::updateAssistantInput,
                        placeholder = { Text(strings.assistantPlaceholder, maxLines = 1) },
                        maxLines = 3,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("assistant_input_field")
                    )
                    Button(
                        onClick = { viewModel.sendAssistantMessage(preferredClass, language) },
                        enabled = state !is UiState.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("assistant_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.Send,
                            contentDescription = strings.send
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MathSolverPane(
    strings: MysticStrings,
    language: AppLanguage,
    preferredClass: Int,
    viewModel: AiToolsViewModel,
    modifier: Modifier = Modifier
) {
    val input by viewModel.mathInput.collectAsStateWithLifecycle()
    val photoUri by viewModel.mathPhotoUri.collectAsStateWithLifecycle()
    val state by viewModel.mathState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = viewModel::updateMathInput,
            label = { Text(strings.toolMath) },
            placeholder = { Text(strings.mathPlaceholder) },
            minLines = 3,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("math_problem_input")
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val samples = listOf("2x + 6 = 18", "3x - 5 = 16", "Area of triangle base 8, height 5")
            items(samples) { sample ->
                AssistChip(
                    onClick = { viewModel.updateMathInput(sample) },
                    label = { Text(sample) }
                )
            }
        }

        PhotoAttachmentRow(
            photoUri = photoUri,
            attachLabel = strings.attachPhoto,
            attachedLabel = strings.photoAttached,
            removeLabel = strings.removePhoto,
            onPhotoSelected = viewModel::setMathPhoto
        )

        Button(
            onClick = { viewModel.solveMath(preferredClass, language) },
            enabled = state !is UiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("solve_math_button")
        ) {
            Icon(Icons.Rounded.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.solveStepByStep)
        }

        when (val s = state) {
            is UiState.Idle -> Unit
            is UiState.Loading -> LoadingPulseCard(message = strings.loadingAi)
            is UiState.Error -> ErrorStateCard(
                message = s.message,
                retryLabel = strings.retry,
                onRetry = { viewModel.solveMath(preferredClass, language) }
            )
            is UiState.Success<MathSolution> -> {
                val sol = s.data
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (sol.isSamplePreview) {
                            SamplePreviewTag(label = strings.samplePreviewBadge)
                        }
                        Text(
                            text = strings.stepByStepTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        sol.steps.forEachIndexed { idx, step ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${idx + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = step,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = strings.finalAnswerLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = sol.finalAnswer,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Rounded.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${strings.conceptTipLabel}: ${sol.conceptTip}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GrammarTutorPane(
    strings: MysticStrings,
    language: AppLanguage,
    preferredClass: Int,
    viewModel: AiToolsViewModel,
    modifier: Modifier = Modifier
) {
    val input by viewModel.grammarInput.collectAsStateWithLifecycle()
    val state by viewModel.grammarState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = viewModel::updateGrammarInput,
            label = { Text(strings.toolGrammar) },
            placeholder = { Text(strings.grammarPlaceholder) },
            minLines = 3,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("grammar_input_field")
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val samples = listOf(
                "he go to school and i study science",
                "she don't like waking up early"
            )
            items(samples) { sample ->
                AssistChip(
                    onClick = { viewModel.updateGrammarInput(sample) },
                    label = { Text(sample) }
                )
            }
        }

        Button(
            onClick = { viewModel.analyzeGrammar(preferredClass, language) },
            enabled = state !is UiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("analyze_grammar_button")
        ) {
            Icon(Icons.Rounded.Spellcheck, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.analyzeGrammar)
        }

        when (val s = state) {
            is UiState.Idle -> Unit
            is UiState.Loading -> LoadingPulseCard(message = strings.loadingAi)
            is UiState.Error -> ErrorStateCard(
                message = s.message,
                retryLabel = strings.retry,
                onRetry = { viewModel.analyzeGrammar(preferredClass, language) }
            )
            is UiState.Success<GrammarAnalysis> -> {
                val res = s.data
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)),
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
                            SamplePreviewTag(label = strings.samplePreviewBadge)
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "${strings.grammarScoreLabel}: ${res.scorePercent}%",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = strings.correctedTextLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = res.correctedText,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (res.mistakes.isEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = strings.noMistakesLabel,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        } else {
                            Text(
                                text = strings.mistakesFoundLabel,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            res.mistakes.forEach { m ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.small,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "\"${m.issue}\" → \"${m.correction}\"",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = m.rule,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = "${strings.learningTipLabel}: ${res.learningTip}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeworkHelperPane(
    strings: MysticStrings,
    language: AppLanguage,
    preferredClass: Int,
    viewModel: AiToolsViewModel,
    modifier: Modifier = Modifier
) {
    val input by viewModel.homeworkInput.collectAsStateWithLifecycle()
    val subject by viewModel.homeworkSubject.collectAsStateWithLifecycle()
    val hintOnly by viewModel.homeworkHintOnly.collectAsStateWithLifecycle()
    val photoUri by viewModel.homeworkPhotoUri.collectAsStateWithLifecycle()
    val state by viewModel.homeworkState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Subject.entries) { subj ->
                FilterChip(
                    selected = subject == subj,
                    onClick = { viewModel.selectHomeworkSubject(subj) },
                    label = { Text(subj.label(language)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = subj.accentColor,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        OutlinedTextField(
            value = input,
            onValueChange = viewModel::updateHomeworkInput,
            label = { Text(strings.toolHomework) },
            placeholder = { Text(strings.homeworkPlaceholder) },
            minLines = 3,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("homework_input_field")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = strings.hintFirstMode,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Switch(
                checked = hintOnly,
                onCheckedChange = viewModel::setHomeworkHintOnly,
                modifier = Modifier.testTag("homework_hint_switch")
            )
        }

        PhotoAttachmentRow(
            photoUri = photoUri,
            attachLabel = strings.attachPhoto,
            attachedLabel = strings.photoAttached,
            removeLabel = strings.removePhoto,
            onPhotoSelected = viewModel::setHomeworkPhoto
        )

        Button(
            onClick = { viewModel.requestHomeworkHelp(preferredClass, language) },
            enabled = state !is UiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .minimumInteractiveComponentSize()
                .testTag("homework_help_button")
        ) {
            Icon(Icons.Rounded.EditNote, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.getHomeworkHelp)
        }

        when (val s = state) {
            is UiState.Idle -> Unit
            is UiState.Loading -> LoadingPulseCard(message = strings.loadingAi)
            is UiState.Error -> ErrorStateCard(
                message = s.message,
                retryLabel = strings.retry,
                onRetry = { viewModel.requestHomeworkHelp(preferredClass, language) }
            )
            is UiState.Success<HomeworkHelpResult> -> {
                val res = s.data
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (res.isSamplePreview) {
                            SamplePreviewTag(label = strings.samplePreviewBadge)
                        }
                        res.explanationSteps.forEach { step ->
                            Text(
                                text = "• $step",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = strings.keyTakeawayLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = res.keyTakeaway,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        Text(
                            text = "${strings.practicePromptLabel}: ${res.practiceQuestion}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
