package com.example.data.repository

import com.example.model.AppLanguage
import com.example.model.GeneratedQuiz
import com.example.model.GrammarAnalysis
import com.example.model.GrammarMistake
import com.example.model.HomeworkHelpResult
import com.example.model.MathSolution
import com.example.model.QuizQuestion
import com.example.model.Subject
import kotlinx.coroutines.delay

/**
 * Single AI abstraction interface for Mystic AI.
 * Currently backed by [PlaceholderAiRepository] because Firebase setup was declined.
 * Connect Firebase AI Logic (`Firebase.ai`) here once Firebase is enabled.
 */
interface AiRepository {
    suspend fun sendStudyAssistantMessage(
        prompt: String,
        classLevel: Int,
        subject: Subject?,
        language: AppLanguage,
        hasImage: Boolean
    ): Result<String>

    suspend fun solveMathProblem(
        problem: String,
        classLevel: Int,
        language: AppLanguage,
        hasImage: Boolean
    ): Result<MathSolution>

    suspend fun analyzeEnglishGrammar(
        text: String,
        classLevel: Int,
        language: AppLanguage
    ): Result<GrammarAnalysis>

    suspend fun generateQuiz(
        classLevel: Int,
        subject: Subject,
        topic: String,
        questionCount: Int,
        difficulty: String,
        language: AppLanguage
    ): Result<GeneratedQuiz>

    suspend fun getHomeworkHelp(
        question: String,
        classLevel: Int,
        subject: Subject,
        hintOnly: Boolean,
        language: AppLanguage,
        hasImage: Boolean
    ): Result<HomeworkHelpResult>
}

class PlaceholderAiRepository : AiRepository {

    override suspend fun sendStudyAssistantMessage(
        prompt: String,
        classLevel: Int,
        subject: Subject?,
        language: AppLanguage,
        hasImage: Boolean
    ): Result<String> = runCatching {
        val trimmed = prompt.trim()
        require(trimmed.isNotEmpty() || hasImage) {
            if (language == AppLanguage.BN) "অনুগ্রহ করে একটি প্রশ্ন লিখুন।" else "Please enter a study question."
        }
        delay(450)
        val subjectLabel = subject?.label(language)
            ?: if (language == AppLanguage.BN) "সাধারণ পড়াশোনা" else "General Study"
        val imageNoteEn = if (hasImage) " (with your attached worksheet image)" else ""
        val imageNoteBn = if (hasImage) " (আপনার যুক্ত করা ছবিসহ)" else ""

        if (language == AppLanguage.BN) {
            """
            [নমুনা প্রিভিউ উত্তর — লাইভ জেমিনি এআই-এর জন্য Firebase প্রয়োজন]
            
            শ্রেণি: ${classLevel} | বিষয়: $subjectLabel
            আপনার প্রশ্ন: "${trimmed.ifEmpty { "ছবিভিত্তিক প্রশ্ন" }}"$imageNoteBn
            
            মূল ব্যাখ্যা:
            ১. মূল ধারণা: প্রথমে বিষয়টির সংজ্ঞা ও মূল সূত্রটি ভালোভাবে বুঝে নাও।
            ২. সহজ উদাহরণ: বাস্তব জীবনের একটি উদাহরণ দিয়ে ধাপে ধাপে সমাধান অনুশীলন করো।
            ৩. পরীক্ষার টিপস: নোটস সেকশন থেকে সংশ্লিষ্ট অধ্যায়ের সারসংক্ষেপ একবার পড়ে কুইজ দাও।
            """.trimIndent()
        } else {
            """
            [Sample Preview Response — Enable Firebase for live Gemini AI]
            
            Class $classLevel • $subjectLabel
            Topic: "${trimmed.ifEmpty { "Uploaded image question" }}"$imageNoteEn
            
            Key Study Breakdown:
            1. Core Concept: Break the question into definitions, given facts, and what is being asked.
            2. Step-by-Step Method: Connect the topic to your Class $classLevel $subjectLabel fundamentals and verify each step carefully.
            3. Quick Practice Tip: Review the matching chapter in Mystic AI Notes and test yourself with a 5-question practice quiz.
            """.trimIndent()
        }
    }

    override suspend fun solveMathProblem(
        problem: String,
        classLevel: Int,
        language: AppLanguage,
        hasImage: Boolean
    ): Result<MathSolution> = runCatching {
        val trimmed = problem.trim()
        require(trimmed.isNotEmpty() || hasImage) {
            if (language == AppLanguage.BN) "অনুগ্রহ করে একটি অঙ্ক লিখুন।" else "Please enter a math problem."
        }
        delay(450)

        // Helpful deterministic sample parser for simple "ax + b = c" or "a + b" expressions
        val linearMatch = Regex("""^\s*(-?\d+)\s*x\s*([+-]\s*\d+)\s*=\s*(-?\d+)\s*$""").find(trimmed)
        if (linearMatch != null) {
            val a = linearMatch.groupValues[1].toInt()
            val b = linearMatch.groupValues[2].replace(" ", "").toInt()
            val c = linearMatch.groupValues[3].toInt()
            val rhs = c - b
            val xVal = if (a != 0) rhs.toDouble() / a.toDouble() else 0.0
            val xFormatted = if (xVal % 1.0 == 0.0) xVal.toInt().toString() else "%.2f".format(xVal)
            val steps = if (language == AppLanguage.BN) {
                listOf(
                    "ধাপ ১ (নমুনা প্রিভিউ): প্রদত্ত সমীকরণটি লিখি: $trimmed",
                    "ধাপ ২: ধ্রুবক পদ $b কে ডান পাশে নিয়ে যাই: ${a}x = $c - ($b) = $rhs",
                    "ধাপ ৩: উভয় পক্ষকে $a দিয়ে ভাগ করি: x = $rhs / $a = $xFormatted"
                )
            } else {
                listOf(
                    "Step 1 (Sample Preview): Start with the given linear equation: $trimmed",
                    "Step 2: Isolate the variable term by moving $b to the right side: ${a}x = $c - ($b) = $rhs",
                    "Step 3: Divide both sides by $a: x = $rhs / $a = $xFormatted"
                )
            }
            return@runCatching MathSolution(
                problem = trimmed,
                steps = steps,
                finalAnswer = "x = $xFormatted",
                conceptTip = if (language == AppLanguage.BN) {
                    "সমীকরণের এক পাশ থেকে অন্য পাশে সংখ্যা নিলে চিহ্ন (+ থেকে -, বা - থেকে +) বদলে যায়।"
                } else {
                    "Always perform the same operation on both sides of an equation to keep it balanced."
                },
                isSamplePreview = true
            )
        }

        val steps = if (language == AppLanguage.BN) {
            listOf(
                "ধাপ ১ (নমুনা প্রিভিউ): প্রদত্ত তথ্যগুলো আলাদা করে লিখি — \"${trimmed.ifEmpty { "ছবি থেকে অঙ্ক" }}\" (শ্রেণি $classLevel)।",
                "ধাপ ২: সমস্যাটির ধরন (পাটিগণিত, বীজগণিত বা জ্যামিতি) অনুযায়ী সঠিক সূত্র নির্বাচন করি।",
                "ধাপ ৩: প্রদত্ত মানগুলো সূত্রে বসিয়ে ক্রমানুসারে হিসাব করি এবং একক যাচাই করি।"
            )
        } else {
            listOf(
                "Step 1 (Sample Preview): Identify the given values and target unknown for Class $classLevel: \"${trimmed.ifEmpty { "Attached math image" }}\".",
                "Step 2: Select the governing formula or algebraic identity that connects the given quantities.",
                "Step 3: Substitute values carefully, simplify arithmetic order of operations (PEMDAS/BODMAS), and check units."
            )
        }

        MathSolution(
            problem = trimmed.ifEmpty { if (language == AppLanguage.BN) "ছবিভিত্তিক অঙ্ক" else "Uploaded Math Problem" },
            steps = steps,
            finalAnswer = if (language == AppLanguage.BN) {
                "নমুনা সমাধান প্রস্তুত (লাইভ গণনার জন্য Firebase চালু করুন)"
            } else {
                "Sample walkthrough ready (Enable Firebase for live Gemini solving)"
            },
            conceptTip = if (language == AppLanguage.BN) {
                "উত্তরটি মূল সমীকরণে বসিয়ে যাচাই করলে ভুল হওয়ার সম্ভাবনা কমে যায়।"
            } else {
                "Substitute your answer back into the original problem to double-check accuracy."
            },
            isSamplePreview = true
        )
    }

    override suspend fun analyzeEnglishGrammar(
        text: String,
        classLevel: Int,
        language: AppLanguage
    ): Result<GrammarAnalysis> = runCatching {
        val trimmed = text.trim()
        require(trimmed.isNotEmpty()) {
            if (language == AppLanguage.BN) "অনুগ্রহ করে একটি ইংরেজি বাক্য লিখুন।" else "Please enter an English sentence or paragraph."
        }
        delay(420)

        var corrected = trimmed
        val mistakes = mutableListOf<GrammarMistake>()

        if (corrected.contains("he go ", ignoreCase = true) || corrected.endsWith("he go", ignoreCase = true)) {
            corrected = corrected.replace(Regex("\\bhe go\\b", RegexOption.IGNORE_CASE), "he goes")
            mistakes += GrammarMistake(
                issue = "he go",
                correction = "he goes",
                rule = if (language == AppLanguage.BN) "Third-person singular (he/she/it)-এর পরে Present Simple-এ verb-এর সাথে s/es যোগ হয়।" else "Use '-s/-es' with verbs after third-person singular subjects (he/she/it) in Simple Present."
            )
        }
        if (corrected.contains("she don't", ignoreCase = true)) {
            corrected = corrected.replace(Regex("\\bshe don't\\b", RegexOption.IGNORE_CASE), "she doesn't")
            mistakes += GrammarMistake(
                issue = "she don't",
                correction = "she doesn't",
                rule = if (language == AppLanguage.BN) "He/She/It-এর সাথে 'don't' নয়, 'doesn't' বসে।" else "Singular third-person subjects take 'doesn't' instead of 'don't'."
            )
        }
        if (Regex("\\bi\\b").containsMatchIn(corrected)) {
            corrected = corrected.replace(Regex("\\bi\\b"), "I")
            mistakes += GrammarMistake(
                issue = "lowercase 'i'",
                correction = "I",
                rule = if (language == AppLanguage.BN) "ইংরেজিতে 'I' (আমি) সর্বদা বড় হাতের অক্ষরে লিখতে হয়।" else "The pronoun 'I' is always capitalized in English."
            )
        }
        if (corrected.firstOrNull()?.isLowerCase() == true) {
            val fixedFirst = corrected.replaceFirstChar { it.uppercaseChar() }
            mistakes += GrammarMistake(
                issue = "lowercase start",
                correction = fixedFirst.take(12) + if (fixedFirst.length > 12) "..." else "",
                rule = if (language == AppLanguage.BN) "প্রতিটি বাক্যের প্রথম অক্ষর বড় হাতের (Capital letter) হয়।" else "Always begin a sentence with a capital letter."
            )
            corrected = fixedFirst
        }
        if (corrected.lastOrNull() !in listOf('.', '!', '?')) {
            corrected = "$corrected."
            mistakes += GrammarMistake(
                issue = "missing ending punctuation",
                correction = "Added '.'",
                rule = if (language == AppLanguage.BN) "বাক্যের শেষে যথাযথ বিরামচিহ্ন (., ?, !) ব্যবহার করতে হয়।" else "End every complete sentence with a period, question mark, or exclamation point."
            )
        }

        val score = (100 - mistakes.size * 12).coerceIn(55, 100)
        GrammarAnalysis(
            originalText = trimmed,
            correctedText = corrected,
            scorePercent = score,
            mistakes = mistakes,
            learningTip = if (language == AppLanguage.BN) {
                "[নমুনা প্রিভিউ] Class $classLevel-এর জন্য Subject-Verb Agreement ও Tense নিয়মিত অনুশীলন করো। লাইভ জেমিনি গ্রামার বিশ্লেষণের জন্য Firebase সক্রিয় করুন।"
            } else {
                "[Sample Preview] Keep practicing Subject-Verb Agreement and punctuation for Class $classLevel. Enable Firebase for deep AI grammar coaching."
            },
            isSamplePreview = true
        )
    }

    override suspend fun generateQuiz(
        classLevel: Int,
        subject: Subject,
        topic: String,
        questionCount: Int,
        difficulty: String,
        language: AppLanguage
    ): Result<GeneratedQuiz> = runCatching {
        delay(500)
        val cleanTopic = topic.trim().ifEmpty { subject.label(language) }
        val bank = sampleQuestionsFor(subject, classLevel, cleanTopic, language)
        val count = questionCount.coerceIn(1, bank.size)
        GeneratedQuiz(
            classLevel = classLevel,
            subject = subject,
            topic = cleanTopic,
            questions = bank.take(count),
            isSamplePreview = true
        )
    }

    override suspend fun getHomeworkHelp(
        question: String,
        classLevel: Int,
        subject: Subject,
        hintOnly: Boolean,
        language: AppLanguage,
        hasImage: Boolean
    ): Result<HomeworkHelpResult> = runCatching {
        val trimmed = question.trim()
        require(trimmed.isNotEmpty() || hasImage) {
            if (language == AppLanguage.BN) "অনুগ্রহ করে হোমওয়ার্কের প্রশ্ন লিখুন।" else "Please enter your homework question."
        }
        delay(450)
        val subj = subject.label(language)
        if (language == AppLanguage.BN) {
            val steps = if (hintOnly) {
                listOf(
                    "ইঙ্গিত ১ (নমুনা প্রিভিউ): $subj-এর এই প্রশ্নে প্রদত্ত মূল শব্দ বা সংখ্যাগুলো চিহ্নিত করো।",
                    "ইঙ্গিত ২: Class $classLevel-এর মূল নিয়ম বা সূত্রটি মনে করার চেষ্টা করো।",
                    "ইঙ্গিত ৩: নিজের খাতায় প্রথম ধাপটি লিখে দেখো কী উত্তর আসে।"
                )
            } else {
                listOf(
                    "ধাপ ১ (নমুনা প্রিভিউ): প্রশ্নটি ভালোভাবে পড়ে কী চাওয়া হয়েছে তা নির্ণয় করো।",
                    "ধাপ ২: $subj-এর প্রাসঙ্গিক ধারণা বা সূত্র প্রয়োগ করো।",
                    "ধাপ ৩: প্রতিটি ধাপ গুছিয়ে লিখে চূড়ান্ত উত্তর যাচাই করো।"
                )
            }
            HomeworkHelpResult(
                question = trimmed.ifEmpty { "ছবিভিত্তিক হোমওয়ার্ক প্রশ্ন" },
                explanationSteps = steps,
                keyTakeaway = "শ্রেণি $classLevel ($subj): ধাপে ধাপে চিন্তা করলে যেকোনো জটিল হোমওয়ার্ক সহজ হয়ে যায়।",
                practiceQuestion = "$subj-এর এই অধ্যায় থেকে নিজের ভাষায় ২ লাইনের একটি সারাংশ লিখে অনুশীলন করো।",
                isSamplePreview = true
            )
        } else {
            val steps = if (hintOnly) {
                listOf(
                    "Hint 1 (Sample Preview): Underline the key terms or numbers in this $subj question.",
                    "Hint 2: Recall the Class $classLevel rule or formula that connects those terms.",
                    "Hint 3: Draft your first step on paper before checking the final result."
                )
            } else {
                listOf(
                    "Step 1 (Sample Preview): Break down what the prompt is asking in $subj (Class $classLevel).",
                    "Step 2: Apply the relevant definition, grammar rule, or formula clearly.",
                    "Step 3: Write a concise conclusion and verify your reasoning."
                )
            }
            HomeworkHelpResult(
                question = trimmed.ifEmpty { "Uploaded worksheet question" },
                explanationSteps = steps,
                keyTakeaway = "Class $classLevel $subj: Breaking questions into clear steps builds lasting mastery.",
                practiceQuestion = "Try explaining the main rule of \"$subj\" in two sentences in your own words.",
                isSamplePreview = true
            )
        }
    }

    private fun sampleQuestionsFor(
        subject: Subject,
        classLevel: Int,
        topic: String,
        language: AppLanguage
    ): List<QuizQuestion> {
        if (language == AppLanguage.BN) {
            return when (subject) {
                Subject.MATH -> listOf(
                    QuizQuestion(
                        question = "[$topic • নমুনা] যদি 2x + 4 = 14 হয়, তবে x-এর মান কত?",
                        options = listOf("3", "5", "7", "9"),
                        correctIndex = 1,
                        explanation = "2x = 14 - 4 = 10, তাই x = 10 / 2 = 5।"
                    ),
                    QuizQuestion(
                        question = "একটি সমকোণী ত্রিভুজের বাহু দুটি ৩ সেমি ও ৪ সেমি হলে অতিভুজ কত?",
                        options = listOf("৫ সেমি", "৬ সেমি", "৭ সেমি", "১২ সেমি"),
                        correctIndex = 0,
                        explanation = "পিথাগোরাসের সূত্র: √(৩² + ৪²) = √(৯ + ১৬) = √২৫ = ৫ সেমি।"
                    ),
                    QuizQuestion(
                        question = "৩/৪ ভগ্নাংশটির শতকরা মান কত?",
                        options = listOf("২৫%", "৫০%", "৭৫%", "৮০%"),
                        correctIndex = 2,
                        explanation = "(৩/৪) × ১০০% = ৭৫%।"
                    ),
                    QuizQuestion(
                        question = "কোনো সংখ্যাকে ০ দিয়ে গুণ করলে গুণফল কত হয়?",
                        options = listOf("১", "০", "সেই সংখ্যা", "অসীম"),
                        correctIndex = 1,
                        explanation = "যেকোনো সংখ্যা × ০ = ০।"
                    ),
                    QuizQuestion(
                        question = "একটি বর্গের প্রতি বাহু ৬ সেমি হলে পরিসীমা কত?",
                        options = listOf("১২ সেমি", "১৮ সেমি", "২৪ সেমি", "৩৬ সেমি"),
                        correctIndex = 2,
                        explanation = "বর্গের পরিসীমা = ৪ × বাহুর দৈর্ঘ্য = ৪ × ৬ = ২৪ সেমি।"
                    )
                )
                Subject.ENGLISH -> listOf(
                    QuizQuestion(
                        question = "[$topic • নমুনা] Choose the correct sentence:",
                        options = listOf("She go to school.", "She goes to school.", "She going to school.", "She gone to school."),
                        correctIndex = 1,
                        explanation = "Simple Present-এ She (3rd person singular)-এর সাথে verb-এ 'es' যুক্ত হয়।"
                    ),
                    QuizQuestion(
                        question = "Which word is a Pronoun?",
                        options = listOf("Dhaka", "Quickly", "They", "Happy"),
                        correctIndex = 2,
                        explanation = "'They' একটি Pronoun যা Noun-এর পরিবর্তে বসে।"
                    ),
                    QuizQuestion(
                        question = "Passive voice of 'He writes a letter':",
                        options = listOf("A letter was written by him.", "A letter is written by him.", "A letter wrote by him.", "He is writing a letter."),
                        correctIndex = 1,
                        explanation = "Simple Present active বাক্যের passive রূপ: is/am/are + V3।"
                    ),
                    QuizQuestion(
                        question = "Select the correct article: 'He ate ___ apple.'",
                        options = listOf("a", "an", "the", "no article"),
                        correctIndex = 1,
                        explanation = "Vowel sound ('a') দিয়ে শুরু হওয়ায় 'an apple' হবে।"
                    ),
                    QuizQuestion(
                        question = "Past tense of 'Teach' is:",
                        options = listOf("Teached", "Taught", "Touch", "Teaching"),
                        correctIndex = 1,
                        explanation = "'Teach'-এর Past এবং Past Participle রূপ হলো 'Taught'।"
                    )
                )
                Subject.BANGLA -> listOf(
                    QuizQuestion(
                        question = "[$topic • নমুনা] বাংলা বর্ণমালায় স্বরবর্ণ কয়টি?",
                        options = listOf("৯টি", "১১টি", "৩৯টি", "৫০টি"),
                        correctIndex = 1,
                        explanation = "বাংলা বর্ণমালায় অ থেকে ঔ পর্যন্ত মোট ১১টি স্বরবর্ণ রয়েছে।"
                    ),
                    QuizQuestion(
                        question = "'বিদ্যা + আলয়' সন্ধিবিচ্ছেদে কোন শব্দ গঠিত হয়?",
                        options = listOf("বিদ্যলয়", "বিদ্যালয়", "বিদ্যআলয়", "বিদ্যোলয়"),
                        correctIndex = 1,
                        explanation = "আ + আ = আ স্বরসন্ধির নিয়মে 'বিদ্যালয়' গঠিত হয়।"
                    ),
                    QuizQuestion(
                        question = "নিচের কোনটি বিশেষ্য পদ?",
                        options = listOf("নীল", "পড়ছে", "পদ্মা", "এবং"),
                        correctIndex = 2,
                        explanation = "'পদ্মা' একটি নদীর নাম, তাই এটি বিশেষ্য পদ।"
                    ),
                    QuizQuestion(
                        question = "বাক্যে ব্যবহৃত অর্থবোধক শব্দকে কী বলে?",
                        options = listOf("ধ্বনি", "বর্ণ", "পদ", "বাক্যাংশ"),
                        correctIndex = 2,
                        explanation = "বাক্যে ব্যবহৃত বিভক্তিযুক্ত প্রতিটি শব্দকে পদ বলে।"
                    ),
                    QuizQuestion(
                        question = "'সূর্য' শব্দের সমার্থক শব্দ কোনটি?",
                        options = listOf("শশী", "রবি", "বারি", "পবন"),
                        correctIndex = 1,
                        explanation = "'রবি', 'দিবাকর', 'ভাস্কর' হলো সূর্যের সমার্থক শব্দ।"
                    )
                )
                Subject.SCIENCE -> listOf(
                    QuizQuestion(
                        question = "[$topic • নমুনা] উদ্ভিদ কোন প্রক্রিয়ায় নিজের খাদ্য তৈরি করে?",
                        options = listOf("শ্বসন", "সালোকসংশ্লেষণ", "প্রস্বেদন", "ব্যাপন"),
                        correctIndex = 1,
                        explanation = "সবুজ উদ্ভিদ সূর্যালোক ও ক্লোরোফিলের সাহায্যে সালোকসংশ্লেষণ প্রক্রিয়ায় খাদ্য তৈরি করে।"
                    ),
                    QuizQuestion(
                        question = "নিউটনের দ্বিতীয় সূত্র অনুযায়ী বল (F) সমান কী?",
                        options = listOf("m / a", "m + a", "m × a", "a / m"),
                        correctIndex = 2,
                        explanation = "নিউটনের গতির ২য় সূত্র: F = m × a।"
                    ),
                    QuizQuestion(
                        question = "পানির রাসায়নিক সংকেত কোনটি?",
                        options = listOf("CO₂", "H₂O", "O₂", "NaCl"),
                        correctIndex = 1,
                        explanation = "দুটি হাইড্রোজেন ও একটি অক্সিজেন পরমাণু মিলে H₂O বা পানি গঠিত হয়।"
                    ),
                    QuizQuestion(
                        question = "পদার্থের কোন অবস্থার নির্দিষ্ট আকার ও আয়তন আছে?",
                        options = listOf("কঠিন", "তরল", "বায়বীয়", "প্লাজমা"),
                        correctIndex = 0,
                        explanation = "কঠিন পদার্থের নির্দিষ্ট আকার এবং নির্দিষ্ট আয়তন থাকে।"
                    ),
                    QuizQuestion(
                        question = "মানুষের শ্বাস-প্রশ্বাসে কোন গ্যাস অপরিহার্য?",
                        options = listOf("নাইট্রোজেন", "অক্সিজেন", "মিথেন", "হিলিয়াম"),
                        correctIndex = 1,
                        explanation = "শ্বসন প্রক্রিয়ায় অক্সিজেন (O₂) অপরিহার্য।"
                    )
                )
                Subject.ICT -> listOf(
                    QuizQuestion(
                        question = "[$topic • নমুনা] নিচের কোনটি একটি ইনপুট ডিভাইস?",
                        options = listOf("মনিটর", "প্রিন্টার", "কীবোর্ড", "স্পিকার"),
                        correctIndex = 2,
                        explanation = "কীবোর্ডের মাধ্যমে কম্পিউটারে তথ্য প্রবেশ করানো হয়।"
                    ),
                    QuizQuestion(
                        question = "বাইনারি সংখ্যা পদ্ধতিতে কয়টি অঙ্ক ব্যবহৃত হয়?",
                        options = listOf("২টি (০ ও ১)", "৮টি", "১০টি", "১৬টি"),
                        correctIndex = 0,
                        explanation = "বাইনারি পদ্ধতির ভিত্তি ২, এবং এর অঙ্ক দুটি হলো ০ ও ১।"
                    ),
                    QuizQuestion(
                        question = "CPU-এর পূর্ণরূপ কী?",
                        options = listOf("Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Core Program Unit"),
                        correctIndex = 1,
                        explanation = "CPU মানে Central Processing Unit।"
                    ),
                    QuizQuestion(
                        question = "কোনটি কম্পিউটারের অস্থায়ী মেমোরি?",
                        options = listOf("SSD", "Hard Disk", "RAM", "Pen Drive"),
                        correctIndex = 2,
                        explanation = "RAM একটি ভোলাটাইল বা অস্থায়ী মেমোরি।"
                    ),
                    QuizQuestion(
                        question = "ওয়েবপেজ তৈরিতে প্রধানত কোন মার্কআপ ভাষা ব্যবহৃত হয়?",
                        options = listOf("HTML", "SQL", "PNG", "MP3"),
                        correctIndex = 0,
                        explanation = "HTML (HyperText Markup Language) ওয়েবপেজের কাঠামো তৈরি করে।"
                    )
                )
            }
        }

        return when (subject) {
            Subject.MATH -> listOf(
                QuizQuestion(
                    question = "[Sample • Class $classLevel $topic] Solve for x: 2x + 4 = 14",
                    options = listOf("x = 3", "x = 5", "x = 7", "x = 9"),
                    correctIndex = 1,
                    explanation = "Subtract 4 from both sides: 2x = 10, then divide by 2: x = 5."
                ),
                QuizQuestion(
                    question = "In a right triangle with legs 3 cm and 4 cm, what is the hypotenuse?",
                    options = listOf("5 cm", "6 cm", "7 cm", "12 cm"),
                    correctIndex = 0,
                    explanation = "By Pythagoras theorem: √(3² + 4²) = √(9 + 16) = √25 = 5 cm."
                ),
                QuizQuestion(
                    question = "What is 3/4 expressed as a percentage?",
                    options = listOf("25%", "50%", "75%", "80%"),
                    correctIndex = 2,
                    explanation = "(3/4) × 100% = 75%."
                ),
                QuizQuestion(
                    question = "What is the perimeter of a square with side length 6 cm?",
                    options = listOf("12 cm", "18 cm", "24 cm", "36 cm"),
                    correctIndex = 2,
                    explanation = "Perimeter of a square = 4 × side = 4 × 6 = 24 cm."
                ),
                QuizQuestion(
                    question = "What is the product of any integer and 0?",
                    options = listOf("1", "0", "The same integer", "Undefined"),
                    correctIndex = 1,
                    explanation = "Multiplying any real number by 0 always equals 0."
                )
            )
            Subject.ENGLISH -> listOf(
                QuizQuestion(
                    question = "[Sample • Class $classLevel $topic] Choose the grammatically correct sentence:",
                    options = listOf("She go to school.", "She goes to school.", "She going to school.", "She gone to school."),
                    correctIndex = 1,
                    explanation = "Third-person singular subjects (he/she/it) add '-es' in the Simple Present tense."
                ),
                QuizQuestion(
                    question = "Which word below is a Pronoun?",
                    options = listOf("Dhaka", "Quickly", "They", "Bright"),
                    correctIndex = 2,
                    explanation = "'They' replaces a plural noun, making it a pronoun."
                ),
                QuizQuestion(
                    question = "Convert to Passive Voice: 'He writes a letter.'",
                    options = listOf("A letter was written by him.", "A letter is written by him.", "A letter wrote by him.", "He is writing a letter."),
                    correctIndex = 1,
                    explanation = "Simple Present passive uses 'is/am/are + past participle (written)'."
                ),
                QuizQuestion(
                    question = "Fill in the blank: 'He ate ___ apple.'",
                    options = listOf("a", "an", "the", "no article"),
                    correctIndex = 1,
                    explanation = "Use 'an' before words starting with a vowel sound."
                ),
                QuizQuestion(
                    question = "What is the past tense of 'teach'?",
                    options = listOf("teached", "taught", "touch", "teaching"),
                    correctIndex = 1,
                    explanation = "'Teach' is an irregular verb whose past form is 'taught'."
                )
            )
            Subject.BANGLA -> listOf(
                QuizQuestion(
                    question = "[Sample • Class $classLevel $topic] How many Swaraborno (vowels) are in the Bangla alphabet?",
                    options = listOf("9", "11", "39", "50"),
                    correctIndex = 1,
                    explanation = "There are 11 Bangla vowels from অ to ঔ."
                ),
                QuizQuestion(
                    question = "What is the joined Sandhi form of 'বিদ্যা + আলয়'?",
                    options = listOf("বিদ্যলয়", "বিদ্যালয়", "বিদ্যআলয়", "বিদ্যোলয়"),
                    correctIndex = 1,
                    explanation = "আ + আ merges into আ to form বিদ্যালয়."
                ),
                QuizQuestion(
                    question = "Which Bangla Pad (part of speech) represents a naming word?",
                    options = listOf("বিশেষণ", "বিশেষ্য (Bisheshyo)", "অব্যয়", "ক্রিয়া"),
                    correctIndex = 1,
                    explanation = "বিশেষ্য (Bisheshyo) is a noun — the name of a person, place, or thing."
                ),
                QuizQuestion(
                    question = "How many main types of Pad exist in Bangla grammar?",
                    options = listOf("3", "4", "5", "8"),
                    correctIndex = 2,
                    explanation = "Bangla grammar categorizes words into 5 Pads: বিশেষ্য, বিশেষণ, সর্বনাম, অব্যয়, ক্রিয়া."
                ),
                QuizQuestion(
                    question = "Which word means 'Sun' in Bangla?",
                    options = listOf("নদী", "সূর্য", "আকাশ", "পবন"),
                    correctIndex = 1,
                    explanation = "সূর্য (Surjo) means Sun."
                )
            )
            Subject.SCIENCE -> listOf(
                QuizQuestion(
                    question = "[Sample • Class $classLevel $topic] By which process do green plants make their own food?",
                    options = listOf("Respiration", "Photosynthesis", "Evaporation", "Diffusion"),
                    correctIndex = 1,
                    explanation = "Plants use sunlight, water, and CO₂ in photosynthesis to produce glucose and oxygen."
                ),
                QuizQuestion(
                    question = "According to Newton's Second Law, Force (F) equals:",
                    options = listOf("m / a", "m + a", "m × a", "a / m"),
                    correctIndex = 2,
                    explanation = "Newton's Second Law states F = mass × acceleration."
                ),
                QuizQuestion(
                    question = "What is the chemical formula for water?",
                    options = listOf("CO₂", "H₂O", "O₂", "NaCl"),
                    correctIndex = 1,
                    explanation = "Water consists of two hydrogen atoms and one oxygen atom (H₂O)."
                ),
                QuizQuestion(
                    question = "Which state of matter has both a fixed shape and fixed volume?",
                    options = listOf("Solid", "Liquid", "Gas", "Plasma"),
                    correctIndex = 0,
                    explanation = "Particles in a solid are tightly packed, giving it a definite shape and volume."
                ),
                QuizQuestion(
                    question = "Which gas do humans inhale for cellular respiration?",
                    options = listOf("Nitrogen", "Oxygen", "Carbon Monoxide", "Helium"),
                    correctIndex = 1,
                    explanation = "Oxygen (O₂) is essential for aerobic respiration."
                )
            )
            Subject.ICT -> listOf(
                QuizQuestion(
                    question = "[Sample • Class $classLevel $topic] Which of the following is an Input device?",
                    options = listOf("Monitor", "Printer", "Keyboard", "Speaker"),
                    correctIndex = 2,
                    explanation = "A keyboard sends user keystrokes into the computer."
                ),
                QuizQuestion(
                    question = "How many digits are used in the Binary number system?",
                    options = listOf("2 (0 and 1)", "8", "10", "16"),
                    correctIndex = 0,
                    explanation = "Binary is base-2 and uses only 0 and 1."
                ),
                QuizQuestion(
                    question = "What does CPU stand for?",
                    options = listOf("Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Core Program Unit"),
                    correctIndex = 1,
                    explanation = "CPU stands for Central Processing Unit."
                ),
                QuizQuestion(
                    question = "Which computer memory is temporary (volatile)?",
                    options = listOf("SSD", "Hard Disk", "RAM", "USB Drive"),
                    correctIndex = 2,
                    explanation = "RAM loses its stored data when power is turned off."
                ),
                QuizQuestion(
                    question = "Which language structures basic web pages?",
                    options = listOf("HTML", "MP4", "JPEG", "BIOS"),
                    correctIndex = 0,
                    explanation = "HTML (HyperText Markup Language) defines webpage structure."
                )
            )
        }
    }
}
