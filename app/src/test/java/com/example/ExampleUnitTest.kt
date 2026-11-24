package com.example

import com.example.data.local.SeedNotesProvider
import com.example.data.repository.PlaceholderAiRepository
import com.example.model.AppLanguage
import com.example.model.Subject
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun seedNotes_coverClasses1To10AndAllSubjects() {
    val notes = SeedNotesProvider.defaultNotes()
    val coveredClasses = notes.map { it.classLevel }.toSet()
    val coveredSubjects = notes.map { it.subjectId }.toSet()
    assertEquals((1..10).toSet(), coveredClasses)
    assertEquals(Subject.entries.map { it.id }.toSet(), coveredSubjects)
  }

  @Test
  fun placeholderMathSolver_solvesLinearEquationSample() = runTest {
    val repo = PlaceholderAiRepository()
    val result = repo.solveMathProblem("2x + 6 = 18", classLevel = 7, language = AppLanguage.EN, hasImage = false)
    assertTrue(result.isSuccess)
    assertEquals("x = 6", result.getOrThrow().finalAnswer)
  }

  @Test
  fun placeholderQuizGenerator_returnsRequestedQuestionCount() = runTest {
    val repo = PlaceholderAiRepository()
    val result = repo.generateQuiz(
      classLevel = 8,
      subject = Subject.SCIENCE,
      topic = "Motion",
      questionCount = 5,
      difficulty = "Medium",
      language = AppLanguage.BN
    )
    assertTrue(result.isSuccess)
    assertEquals(5, result.getOrThrow().questions.size)
  }
}
