package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.i18n.MysticStrings
import com.example.model.AppLanguage
import com.example.model.StudyNote
import com.example.model.Subject
import com.example.ui.components.ClassPill
import com.example.ui.components.ClassSelectorRow
import com.example.ui.components.SubjectPill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    strings: MysticStrings,
    language: AppLanguage,
    isAdminUnlocked: Boolean,
    isDarkTheme: Boolean,
    adminPinError: String?,
    notes: List<StudyNote>,
    onToggleDarkTheme: (Boolean) -> Unit,
    onUnlockAdmin: (String) -> Unit,
    onLockAdmin: () -> Unit,
    onSaveNote: (StudyNote, () -> Unit) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onRestoreStarterNotes: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pinInput by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<StudyNote?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }
    var pendingDeleteNote by remember { mutableStateOf<StudyNote?>(null) }

    if (!isAdminUnlocked) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = strings.adminLockedTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = strings.adminLockedSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 6) pinInput = it },
                        label = { Text(strings.adminPinPlaceholder) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        isError = adminPinError != null,
                        supportingText = adminPinError?.let { err -> { Text(err) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_pin_input")
                    )
                    Button(
                        onClick = { onUnlockAdmin(pinInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .minimumInteractiveComponentSize()
                            .testTag("unlock_admin_button")
                    ) {
                        Icon(Icons.Rounded.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(strings.unlockAdmin)
                    }
                }
            }

            // Theme setting card accessible even when locked
            ThemePreferenceCard(
                label = strings.darkModeLabel,
                isDarkTheme = isDarkTheme,
                onToggle = onToggleDarkTheme
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.adminDashboardTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                pinInput = ""
                                onLockAdmin()
                            },
                            modifier = Modifier.testTag("lock_admin_button")
                        ) {
                            Icon(Icons.Rounded.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.lockAdmin)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                isCreatingNew = true
                                editingNote = StudyNote(
                                    titleEn = "",
                                    titleBn = "",
                                    summaryEn = "",
                                    summaryBn = "",
                                    contentEn = "",
                                    contentBn = "",
                                    classLevel = 8,
                                    subject = Subject.MATH
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("admin_add_note_button")
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.addNote, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        OutlinedButton(
                            onClick = onRestoreStarterNotes,
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("admin_reseed_button")
                        ) {
                            Icon(Icons.Rounded.Restore, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.reseedNotes, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }

        item {
            ThemePreferenceCard(
                label = strings.darkModeLabel,
                isDarkTheme = isDarkTheme,
                onToggle = onToggleDarkTheme
            )
        }

        items(notes, key = { it.id }) { note ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_note_row_${note.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ClassPill(classLevel = note.classLevel, language = language)
                            SubjectPill(subject = note.subject, language = language)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = note.localizedTitle(language),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick = {
                            isCreatingNew = false
                            editingNote = note
                        },
                        modifier = Modifier.testTag("admin_edit_note_${note.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = strings.editNote,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = { pendingDeleteNote = note },
                        modifier = Modifier.testTag("admin_delete_note_${note.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = strings.deleteNote,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    val deleteTarget = pendingDeleteNote
    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteNote = null },
            title = { Text(strings.deleteConfirmTitle) },
            text = { Text("${strings.deleteConfirmMessage}\n\n• ${deleteTarget.localizedTitle(language)}") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteNote(deleteTarget.id)
                        pendingDeleteNote = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.testTag("confirm_delete_note_button")
                ) {
                    Text(strings.deleteNote)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteNote = null }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    val noteToEdit = editingNote
    if (noteToEdit != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var titleEn by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.titleEn) }
        var titleBn by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.titleBn) }
        var summaryEn by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.summaryEn) }
        var summaryBn by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.summaryBn) }
        var contentEn by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.contentEn) }
        var contentBn by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.contentBn) }
        var classLevel by remember(noteToEdit.id, isCreatingNew) { mutableIntStateOf(noteToEdit.classLevel) }
        var subject by remember(noteToEdit.id, isCreatingNew) { mutableStateOf(noteToEdit.subject) }
        var tagsText by remember(noteToEdit.id, isCreatingNew) {
            mutableStateOf(noteToEdit.tags.joinToString(", "))
        }
        var formError by remember { mutableStateOf<String?>(null) }

        ModalBottomSheet(
            onDismissRequest = { editingNote = null },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isCreatingNew) strings.addNote else strings.editNote,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ClassSelectorRow(
                    selectedClass = classLevel,
                    includeAllOption = false,
                    allLabel = strings.allClasses,
                    language = language,
                    onSelectClass = { cls -> if (cls != null) classLevel = cls }
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Subject.entries) { subj ->
                        FilterChip(
                            selected = subject == subj,
                            onClick = { subject = subj },
                            label = { Text(subj.label(language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = subj.accentColor,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text(strings.titleEnLabel) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("editor_title_en")
                )
                OutlinedTextField(
                    value = titleBn,
                    onValueChange = { titleBn = it },
                    label = { Text(strings.titleBnLabel) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("editor_title_bn")
                )
                OutlinedTextField(
                    value = summaryEn,
                    onValueChange = { summaryEn = it },
                    label = { Text(strings.summaryEnLabel) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = summaryBn,
                    onValueChange = { summaryBn = it },
                    label = { Text(strings.summaryBnLabel) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contentEn,
                    onValueChange = { contentEn = it },
                    label = { Text(strings.contentEnLabel) },
                    minLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("editor_content_en")
                )
                OutlinedTextField(
                    value = contentBn,
                    onValueChange = { contentBn = it },
                    label = { Text(strings.contentBnLabel) },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tagsText,
                    onValueChange = { tagsText = it },
                    label = { Text(strings.tagsLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (formError != null) {
                    Text(
                        text = formError!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { editingNote = null },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                    ) {
                        Text(strings.cancel)
                    }
                    Button(
                        onClick = {
                            if (titleEn.isBlank() && titleBn.isBlank()) {
                                formError = "Please enter at least one title."
                                return@Button
                            }
                            if (contentEn.isBlank() && contentBn.isBlank()) {
                                formError = "Please enter study note content."
                                return@Button
                            }
                            val updated = noteToEdit.copy(
                                titleEn = titleEn.trim().ifEmpty { titleBn.trim() },
                                titleBn = titleBn.trim().ifEmpty { titleEn.trim() },
                                summaryEn = summaryEn.trim().ifEmpty { summaryBn.trim().ifEmpty { titleEn.trim() } },
                                summaryBn = summaryBn.trim().ifEmpty { summaryEn.trim().ifEmpty { titleBn.trim() } },
                                contentEn = contentEn.trim().ifEmpty { contentBn.trim() },
                                contentBn = contentBn.trim().ifEmpty { contentEn.trim() },
                                classLevel = classLevel,
                                subject = subject,
                                tags = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            )
                            onSaveNote(updated) { editingNote = null }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .testTag("editor_save_button")
                    ) {
                        Text(strings.save)
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemePreferenceCard(
    label: String,
    isDarkTheme: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = onToggle,
                modifier = Modifier.testTag("dark_theme_switch")
            )
        }
    }
}
