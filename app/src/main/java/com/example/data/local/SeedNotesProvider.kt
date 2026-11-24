package com.example.data.local

import com.example.model.Subject

object SeedNotesProvider {
    fun defaultNotes(): List<StudyNoteEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            // Class 1 - Math & Bangla
            StudyNoteEntity(
                titleEn = "Numbers, Counting & Simple Addition (1–20)",
                titleBn = "সংখ্যা গণনা ও সহজ যোগ (১–২০)",
                summaryEn = "Learn counting objects from 1 to 20 and adding single-digit numbers using visual groups.",
                summaryBn = "ছবি ও বস্তুর সাহায্যে ১ থেকে ২০ পর্যন্ত গণনা এবং এক অঙ্কের যোগ শেখা।",
                contentEn = "Key Concepts:\n• Counting forward from 1 to 20.\n• Addition means combining two groups together (e.g., 4 apples + 3 apples = 7 apples).\n• Adding 0 to any number keeps the number unchanged (5 + 0 = 5).\n\nPractice Tip:\nUse fingers or blocks to verify 6 + 2 = 8 and 9 + 1 = 10.",
                contentBn = "মূল ধারণা:\n• ১ থেকে ২০ পর্যন্ত ক্রমানুসারে গণনা করা।\n• যোগ মানে দুটি দলকে একত্রিত করা (যেমন: ৪টি আপেল + ৩টি আপেল = ৭টি আপেল)।\n• কোনো সংখ্যার সাথে ০ যোগ করলে সংখ্যাটি অপরিবর্তিত থাকে (৫ + ০ = ৫)।\n\nঅনুশীলন টিপস:\nহাতের আঙুল বা কাঠি গুনে ৬ + ২ = ৮ এবং ৯ + ১ = ১০ যাচাই করো।",
                classLevel = 1,
                subjectId = Subject.MATH.id,
                tagsCsv = "counting, addition, basics",
                updatedAt = now - 1000L
            ),
            StudyNoteEntity(
                titleEn = "Bangla Swaraborno (Vowels) & Word Formation",
                titleBn = "স্বরবর্ণ পরিচিতি ও শব্দ গঠন",
                summaryEn = "Recognize all 11 Bangla vowels (অ to ঔ) and form simple everyday words.",
                summaryBn = "অ থেকে ঔ পর্যন্ত ১১টি স্বরবর্ণ চেনা ও সহজ শব্দ তৈরি করা।",
                contentEn = "Swaraborno (১১টি স্বরবর্ণ):\nঅ, আ, ই, ঈ, উ, ঊ, ঋ, এ, ঐ, ও, ঔ\n\nWord Examples:\n• অ — অজ (Goat)\n• আ — আম (Mango)\n• ই — ইলিশ (Hilsa)\n• ঈ — ঈগল (Eagle)\n• উ — উট (Camel)\n\nPractice reading each vowel aloud three times with proper pronunciation.",
                contentBn = "স্বরবর্ণ (১১টি):\nঅ, আ, ই, ঈ, উ, ঊ, ঋ, এ, ঐ, ও, ঔ\n\nশব্দ গঠন:\n• অ — অলি, অজ\n• আ — আম, আকাশ\n• ই — ইলিশ, ইট\n• ঈ — ঈগল, ঈদ\n• উ — উট, উঠান\n\nপ্রতিটি বর্ণ স্পষ্ট উচ্চারণে ৩ বার পড়ে খাতায় লেখো।",
                classLevel = 1,
                subjectId = Subject.BANGLA.id,
                tagsCsv = "স্বরবর্ণ, বর্ণমালা, শব্দ",
                updatedAt = now - 2000L
            ),
            // Class 2 - English & Science
            StudyNoteEntity(
                titleEn = "Nouns, Pronouns & Everyday Sentences",
                titleBn = "Noun, Pronoun ও দৈনন্দিন ইংরেজি বাক্য",
                summaryEn = "Identify naming words (nouns) and replacing words (pronouns) in short English sentences.",
                summaryBn = "ছোট ইংরেজি বাক্যে Noun (নাম) ও Pronoun (সর্বনাম) চেনা ও ব্যবহার করা।",
                contentEn = "1. Noun: A naming word for a person, place, animal, or thing (e.g., Rina, school, cat, book).\n2. Pronoun: A word used instead of a noun (I, you, he, she, it, we, they).\n\nExamples:\n• Rafi is a student. -> He is a student.\n• The book is on the table. -> It is on the table.",
                contentBn = "১. Noun (বিশেষ্য): কোনো ব্যক্তি, স্থান, প্রাণী বা বস্তুর নাম (যেমন: রিনা, স্কুল, বিড়াল, বই)।\n২. Pronoun (সর্বনাম): Noun-এর পরিবর্তে ব্যবহৃত শব্দ (I, you, he, she, it, we, they)।\n\nউদাহরণ:\n• Rafi is a student. -> He is a student.\n• The book is on the table. -> It is on the table.",
                classLevel = 2,
                subjectId = Subject.ENGLISH.id,
                tagsCsv = "noun, pronoun, grammar",
                updatedAt = now - 3000L
            ),
            StudyNoteEntity(
                titleEn = "Living vs. Non-Living Things Around Us",
                titleBn = "আমাদের চারপাশের জীব ও জড় বস্তু",
                summaryEn = "Compare how living things grow, breathe, and need food while non-living things do not.",
                summaryBn = "জীব কীভাবে বাড়ে, শ্বাস নেয় ও খাবার খায় এবং জড় বস্তুর সাথে পার্থক্য।",
                contentEn = "Living Things (জীব):\n• Grow over time (seed -> tree).\n• Breathe air and need food & water.\n• Move and reproduce (humans, birds, plants).\n\nNon-Living Things (জড়):\n• Do not grow, breathe, or eat.\n• Examples: chair, rock, pencil, water bottle.",
                contentBn = "জীবের বৈশিষ্ট্য:\n• বৃদ্ধি পায় (বীজ থেকে চারা গাছ)।\n• শ্বাস নেয় এবং খাদ্য ও পানির প্রয়োজন হয়।\n• উদাহরণ: মানুষ, পাখি, গাছপালা।\n\nজড় বস্তুর বৈশিষ্ট্য:\n• বৃদ্ধি পায় না, শ্বাস নেয় না ও খাবার খায় না।\n• উদাহরণ: চেয়ার, পাথর, কলম, টেবিল।",
                classLevel = 2,
                subjectId = Subject.SCIENCE.id,
                tagsCsv = "living, nature, environment",
                updatedAt = now - 4000L
            ),
            // Class 3 - Math & ICT
            StudyNoteEntity(
                titleEn = "Multiplication Tables & Word Problems",
                titleBn = "গুণের নামতা ও কথার অঙ্ক",
                summaryEn = "Understand multiplication as repeated addition and solve daily-life word problems.",
                summaryBn = "গুণকে বারবার যোগের সংক্ষিপ্ত রূপ হিসেবে বোঝা ও সহজ কথার অঙ্ক সমাধান।",
                contentEn = "What is Multiplication?\nMultiplication is repeated addition: 4 × 3 = 4 + 4 + 4 = 12.\n\nImportant Rules:\n• Any number × 0 = 0.\n• Any number × 1 = that same number.\n• Order property: 3 × 5 = 5 × 3 = 15.\n\nExample Problem:\nOne notebook costs 15 taka. How much do 4 notebooks cost?\nAnswer: 15 × 4 = 60 taka.",
                contentBn = "গুণ কী?\nগুণ হলো একই সংখ্যার বারবার যোগের সংক্ষিপ্ত রূপ: ৪ × ৩ = ৪ + ৪ + ৪ = ১২।\n\nমনে রাখার নিয়ম:\n• যেকোনো সংখ্যা × ০ = ০।\n• যেকোনো সংখ্যা × ১ = সেই সংখ্যাটিই।\n\nউদাহরণ:\n১টি খাতার দাম ১৫ টাকা হলে ৪টি খাতার দাম কত?\nউত্তর: ১৫ × ৪ = ৬০ টাকা।",
                classLevel = 3,
                subjectId = Subject.MATH.id,
                tagsCsv = "multiplication, tables, word-problems",
                updatedAt = now - 5000L
            ),
            StudyNoteEntity(
                titleEn = "Introduction to Computers: Input & Output Devices",
                titleBn = "কম্পিউটার পরিচিতি: ইনপুট ও আউটপুট ডিভাইস",
                summaryEn = "Explore how keyboard, mouse, monitor, and printer work together in a computer.",
                summaryBn = "কীবোর্ড, মাউস, মনিটর ও প্রিন্টার কীভাবে কম্পিউটারে কাজ করে।",
                contentEn = "Input Devices (Send data INTO the computer):\n• Keyboard — typing letters and numbers.\n• Mouse — pointing, clicking, and selecting.\n• Microphone — recording voice.\n\nOutput Devices (Show results FROM the computer):\n• Monitor — displays text, images, and videos.\n• Printer — prints pages on paper.\n• Speaker — plays sound.",
                contentBn = "ইনপুট ডিভাইস (কম্পিউটারে তথ্য দেয়):\n• কীবোর্ড — অক্ষর ও সংখ্যা টাইপ করা।\n• মাউস — পয়েন্ট ও ক্লিক করা।\n• মাইক্রোফোন — শব্দ রেকর্ড করা।\n\nআউটপুট ডিভাইস (ফলাফল দেখায়):\n• মনিটর — লেখা ও ছবি প্রদর্শন করে।\n• প্রিন্টার — কাগজে প্রিন্ট দেয়।\n• স্পিকার — শব্দ শোনায়।",
                classLevel = 3,
                subjectId = Subject.ICT.id,
                tagsCsv = "hardware, input, output",
                updatedAt = now - 6000L
            ),
            // Class 4 - Science & English
            StudyNoteEntity(
                titleEn = "States of Matter: Solid, Liquid & Gas",
                titleBn = "পদার্থের তিন অবস্থা: কঠিন, তরল ও বায়বীয়",
                summaryEn = "Understand how water changes between ice, liquid water, and steam through heating and cooling.",
                summaryBn = "তাপ প্রয়োগ ও শীতল করার মাধ্যমে পদার্থের কঠিন, তরল ও বায়বীয় অবস্থার পরিবর্তন।",
                contentEn = "1. Solid: Fixed shape and fixed volume (ice, brick).\n2. Liquid: Fixed volume, takes container shape (water, milk).\n3. Gas: No fixed shape or volume, spreads out (water vapor, air).\n\nState Changes of Water:\n• Ice + Heat -> Water (Melting)\n• Water + Heat -> Steam (Evaporation)\n• Steam + Cooling -> Water (Condensation)",
                contentBn = "১. কঠিন পদার্থ: নির্দিষ্ট আকার ও আয়তন আছে (বরফ, ইট)।\n২. তরল পদার্থ: নির্দিষ্ট আয়তন আছে কিন্তু পাত্রের আকার নেয় (পানি, দুধ)।\n৩. বায়বীয় পদার্থ: নির্দিষ্ট আকার বা আয়তন নেই (জলীয় বাষ্প, বাতাস)।\n\nঅবস্থার পরিবর্তন:\n• বরফ + তাপ -> পানি (গলন)\n• পানি + তাপ -> বাষ্প (বাষ্পীভবন)",
                classLevel = 4,
                subjectId = Subject.SCIENCE.id,
                tagsCsv = "matter, solid, liquid, gas",
                updatedAt = now - 7000L
            ),
            StudyNoteEntity(
                titleEn = "Tenses Made Easy: Present, Past & Future",
                titleBn = "সহজ Tense: Present, Past ও Future",
                summaryEn = "Master simple present, simple past, and simple future tenses with clear sentence formulas.",
                summaryBn = "Simple Present, Past ও Future Tense-এর গঠন ও দৈনন্দিন উদাহরণ।",
                contentEn = "1. Simple Present (Habit / Fact):\nSubject + Base Verb (+ s/es for he/she/it)\nExample: She reads books every day.\n\n2. Simple Past (Completed action):\nSubject + Past Verb (V2)\nExample: We visited the museum yesterday.\n\n3. Simple Future (Upcoming action):\nSubject + will + Base Verb\nExample: I will finish my homework tonight.",
                contentBn = "১. Simple Present (অভ্যাস বা চিরন্তন সত্য):\nSubject + মূল Verb (he/she/it হলে s/es)\nউদাহরণ: She reads books every day.\n\n২. Simple Past (অতীতের কাজ):\nSubject + Verb-এর Past রূপ\nউদাহরণ: We visited the museum yesterday.\n\n৩. Simple Future (ভবিষ্যতের কাজ):\nSubject + will + মূল Verb\nউদাহরণ: I will finish my homework tonight.",
                classLevel = 4,
                subjectId = Subject.ENGLISH.id,
                tagsCsv = "tenses, verbs, grammar",
                updatedAt = now - 8000L
            ),
            // Class 5 - Math & Bangla
            StudyNoteEntity(
                titleEn = "Fractions, Decimals & Percentages",
                titleBn = "ভগ্নাংশ, দশমিক ও শতকরা",
                summaryEn = "Convert easily between fractions, decimals, and percentages with real exam examples.",
                summaryBn = "ভগ্নাংশ থেকে দশমিক ও শতকরায় রূপান্তর করার সহজ নিয়ম ও উদাহরণ।",
                contentEn = "Core Conversions:\n• 1/2 = 0.5 = 50%\n• 1/4 = 0.25 = 25%\n• 3/4 = 0.75 = 75%\n• 1/5 = 0.2 = 20%\n\nHow to Convert Fraction -> Percentage:\nMultiply the fraction by 100%.\nExample: (3/5) × 100% = 60%.\n\nAdding Unlike Fractions:\nFind the LCM of denominators first: 1/2 + 1/3 = 3/6 + 2/6 = 5/6.",
                contentBn = "গুরুত্বপূর্ণ রূপান্তর:\n• ১/২ = ০.৫ = ৫০%\n• ১/৪ = ০.২৫ = ২৫%\n• ৩/৪ = ০.৭৫ = ৭৫%\n\nভগ্নাংশ থেকে শতকরা:\nভগ্নাংশকে ১০০% দিয়ে গুণ করতে হয়।\nউদাহরণ: (৩/৫) × ১০০% = ৬০%।\n\nভিন্ন হরবিশিষ্ট ভগ্নাংশের যোগ:\nপ্রথমে হরগুলোর ল.সা.গু বের করো: ১/২ + ১/৩ = ৩/৬ + ২/৬ = ৫/৬।",
                classLevel = 5,
                subjectId = Subject.MATH.id,
                tagsCsv = "fractions, decimals, percentage",
                updatedAt = now - 9000L
            ),
            StudyNoteEntity(
                titleEn = "Bangla Pad (Parts of Speech) Overview",
                titleBn = "বাংলা ব্যাকরণ: পদ প্রকরণ (বিশেষ্য থেকে অব্যয়)",
                summaryEn = "Learn the 5 types of Bangla Pad: Bisheshyo, Bisheshon, Sarbonam, Abyoy, and Kriya.",
                summaryBn = "পাঁচ প্রকার পদ—বিশেষ্য, বিশেষণ, সর্বনাম, অব্যয় ও ক্রিয়া উদাহরণসহ শেখা।",
                contentEn = "বাক্যে ব্যবহৃত প্রতিটি অর্থবোধক শব্দকে পদ বলে। পদ ৫ প্রকার:\n১. বিশেষ্য (Noun): নাম বোঝায় (ঢাকা, পদ্মা, বই)।\n২. বিশেষণ (Adjective): দোষ, গুণ, অবস্থা বোঝায় (লাল ফুল, ভালো ছেলে)।\n৩. সর্বনাম (Pronoun): বিশেষ্যের পরিবর্তে বসে (আমি, সে, তারা)।\n৪. অব্যয় (Conjunction/Indeclinable): রূপ বদলায় না (এবং, কিন্তু, অথবা)।\n৫. ক্রিয়া (Verb): কাজ করা বোঝায় (পড়া, খেলা, লেখা)।",
                contentBn = "বাক্যে ব্যবহৃত প্রতিটি অর্থবোধক শব্দকে পদ বলে। পদ ৫ প্রকার:\n১. বিশেষ্য: কোনো কিছুর নাম বোঝায় (ঢাকা, নজরুল, নদী)।\n২. বিশেষণ: গুণ, দোষ, সংখ্যা বা অবস্থা বোঝায় (নীল আকাশ, মেধাবী ছাত্রী)।\n৩. সর্বনাম: বিশেষ্যের পরিবর্তে ব্যবহৃত হয় (আমি, তুমি, সে, তারা)।\n৪. অব্যয়: যা কখনো পরিবর্তিত হয় না (এবং, কিন্তু, অথচ)।\n৫. ক্রিয়া: কোনো কাজ করা বোঝায় (পড়ছি, লিখছে, যাবে)।",
                classLevel = 5,
                subjectId = Subject.BANGLA.id,
                tagsCsv = "ব্যাকরণ, পদ, বিশেষ্য",
                updatedAt = now - 10000L
            ),
            // Class 6 - ICT & Science
            StudyNoteEntity(
                titleEn = "Hardware, Software & Operating Systems",
                titleBn = "হার্ডওয়্যার, সফটওয়্যার ও অপারেটিং সিস্টেম",
                summaryEn = "Distinguish physical computer components from system and application software.",
                summaryBn = "কম্পিউটারের দৃশ্যমান যন্ত্রাংশ ও সফটওয়্যারের প্রকারভেদ।",
                contentEn = "Hardware: Physical parts you can touch (CPU, RAM, Motherboard, SSD, Monitor).\nSoftware: Programs that tell hardware what to do.\n• System Software: Operating systems (Android, Windows, Linux).\n• Application Software: Tools for tasks (Word processor, Browser, Mystic AI).\n\nMemory Hierarchy:\nRAM (Temporary/Volatile) vs. Hard Drive/SSD (Permanent storage).",
                contentBn = "হার্ডওয়্যার: কম্পিউটারের যে অংশগুলো স্পর্শ করা যায় (CPU, RAM, মাদারবোর্ড, মনিটর)।\nসফটওয়্যার: প্রোগ্রাম যা হার্ডওয়্যারকে পরিচালনা করে।\n• সিস্টেম সফটওয়্যার: অপারেটিং সিস্টেম (Android, Windows, Linux)।\n• অ্যাপ্লিকেশন সফটওয়্যার: কাজের প্রোগ্রাম (ব্রাউজার, ওয়ার্ড প্রসেসর)।\n\nমেমোরি:\nRAM অস্থায়ী মেমোরি, আর SSD/হার্ডডিস্ক স্থায়ী মেমোরি।",
                classLevel = 6,
                subjectId = Subject.ICT.id,
                tagsCsv = "hardware, software, os, ram",
                updatedAt = now - 11000L
            ),
            StudyNoteEntity(
                titleEn = "Photosynthesis & Plant Nutrition",
                titleBn = "সালোকসংশ্লেষণ ও উদ্ভিদের পুষ্টি",
                summaryEn = "Discover how green plants use sunlight, water, and CO₂ to make glucose and oxygen.",
                summaryBn = "সবুজ উদ্ভিদ কীভাবে সূর্যালোক, পানি ও কার্বন ডাই-অক্সাইড ব্যবহার করে খাদ্য তৈরি করে।",
                contentEn = "Photosynthesis Formula:\nCarbon Dioxide (6CO₂) + Water (6H₂O) --[Sunlight + Chlorophyll]--> Glucose (C₆H₁₂O₆) + Oxygen (6O₂)\n\nWhere Does It Happen?\nInside chloroplasts in green leaves using chlorophyll pigment.\n\nWhy It Matters:\nProvides food energy for the plant and releases oxygen needed by humans and animals.",
                contentBn = "সালোকসংশ্লেষণ বিক্রিয়া:\nকার্বন ডাই-অক্সাইড + পানি --[সূর্যালোক ও ক্লোরোফিল]--> গ্লুকোজ (শর্করা) + অক্সিজেন\n\nকোথায় ঘটে?\nসবুজ পাতার ক্লোরোপ্লাস্টে থাকা ক্লোরোফিলের মাধ্যমে।\n\nগুরুত্ব:\nউদ্ভিদের খাদ্য তৈরি করে এবং প্রাণীকুলের শ্বাস-প্রশ্বাসের জন্য অক্সিজেন সরবরাহ করে।",
                classLevel = 6,
                subjectId = Subject.SCIENCE.id,
                tagsCsv = "biology, photosynthesis, plants",
                updatedAt = now - 12000L
            ),
            // Class 7 - Math & English
            StudyNoteEntity(
                titleEn = "Algebraic Expressions & Linear Equations",
                titleBn = "বীজগাণিতিক রাশি ও সরল সমীকরণ",
                summaryEn = "Simplify variables, coefficients, and solve one-variable linear equations cleanly.",
                summaryBn = "চলক, সহগ এবং এক চলকবিশিষ্ট সরল সমীকরণ সমাধানের নিয়ম।",
                contentEn = "Key Terms in 5x + 3 = 18:\n• Variable (চলক): x\n• Coefficient (সহগ): 5\n• Constant (ধ্রুবক): 3 and 18\n\nSolving Steps:\n1. 5x + 3 = 18\n2. Subtract 3 from both sides: 5x = 15\n3. Divide both sides by 5: x = 3\n\nCheck: 5(3) + 3 = 15 + 3 = 18 (Correct!)",
                contentBn = "5x + 3 = 18 সমীকরণে:\n• চলক: x\n• সহগ: 5\n• ধ্রুবক: 3 এবং 18\n\nসমাধানের ধাপ:\n১. 5x + 3 = 18\n২. উভয় পক্ষ থেকে 3 বিয়োগ করে পাই: 5x = 15\n৩. উভয় পক্ষকে 5 দিয়ে ভাগ করে পাই: x = 3\n\nযাচাই: 5(3) + 3 = 18 (সঠিক!)",
                classLevel = 7,
                subjectId = Subject.MATH.id,
                tagsCsv = "algebra, equations, linear",
                updatedAt = now - 13000L
            ),
            StudyNoteEntity(
                titleEn = "Active & Passive Voice Rules",
                titleBn = "Active থেকে Passive Voice পরিবর্তনের নিয়ম",
                summaryEn = "Transform active sentences into passive voice across tenses with subject-object swaps.",
                summaryBn = "বিভিন্ন Tense অনুযায়ী Active Voice থেকে Passive Voice করার নিয়ম ও উদাহরণ।",
                contentEn = "Golden Formula for Passive Voice:\nObject becomes Subject + Auxiliary (be) + Past Participle (V3) + by + Subject becomes Object\n\nExamples:\n• Simple Present: She writes a letter. -> A letter is written by her.\n• Simple Past: He solved the puzzle. -> The puzzle was solved by him.\n• Present Continuous: They are planting trees. -> Trees are being planted by them.",
                contentBn = "Passive Voice-এর মূল সূত্র:\nObject-কে Subject + Tense অনুযায়ী সাহায্যকারী Verb + মূল Verb-এর V3 + by + Subject-এর Object রূপ।\n\nউদাহরণ:\n• Simple Present: She writes a letter. -> A letter is written by her.\n• Simple Past: He solved the puzzle. -> The puzzle was solved by him.\n• Present Continuous: They are planting trees. -> Trees are being planted by them.",
                classLevel = 7,
                subjectId = Subject.ENGLISH.id,
                tagsCsv = "voice, passive, grammar",
                updatedAt = now - 14000L
            ),
            // Class 8 - Math, Science & ICT
            StudyNoteEntity(
                titleEn = "Pythagoras Theorem & Right Triangles",
                titleBn = "পিথাগোরাসের উপপাদ্য ও সমকোণী ত্রিভুজ",
                summaryEn = "Calculate hypotenuse and unknown sides using a² + b² = c² in right-angled triangles.",
                summaryBn = "সমকোণী ত্রিভুজের অতিভুজ, লম্ব ও ভূমি নির্ণয়ে a² + b² = c² সূত্রের ব্যবহার।",
                contentEn = "Theorem Statement:\nIn any right-angled triangle, the square of the hypotenuse equals the sum of the squares of the other two sides:\na² + b² = c²\n\nClassic 3-4-5 Example:\nIf base a = 3 cm and height b = 4 cm:\nc² = 3² + 4² = 9 + 16 = 25\nc = √25 = 5 cm.\n\nCommon Pythagorean Triples: (3,4,5), (5,12,13), (8,15,17).",
                contentBn = "উপপাদ্যের বিবৃতি:\nএকটি সমকোণী ত্রিভুজের অতিভুজের ওপর অঙ্কিত বর্গক্ষেত্রের ক্ষেত্রফল অপর দুই বাহুর ওপর অঙ্কিত বর্গক্ষেত্রদ্বয়ের ক্ষেত্রফলের সমষ্টির সমান:\na² + b² = c²\n\nউদাহরণ:\nযদি ভূমি a = ৩ সেমি ও লম্ব b = ৪ সেমি হয়:\nc² = ৩² + ৪² = ৯ + ১৬ = ২৫\nc = √২৫ = ৫ সেমি।",
                classLevel = 8,
                subjectId = Subject.MATH.id,
                tagsCsv = "geometry, pythagoras, triangles",
                updatedAt = now - 15000L
            ),
            StudyNoteEntity(
                titleEn = "Computer Networks & Internet Topologies",
                titleBn = "কম্পিউটার নেটওয়ার্ক ও টপোলজি",
                summaryEn = "Compare PAN, LAN, MAN, WAN and common network topologies: Star, Ring, Bus, and Mesh.",
                summaryBn = "PAN, LAN, MAN, WAN নেটওয়ার্ক এবং Star, Ring, Bus ও Mesh টপোলজির তুলনা।",
                contentEn = "Network Types:\n• PAN: Personal devices (Bluetooth).\n• LAN: School lab or office building.\n• WAN: Global internet spanning countries.\n\nTopologies:\n• Star: All nodes connect to a central Hub/Switch (easy to troubleshoot).\n• Ring: Circular connection.\n• Bus: Single backbone cable.\n• Mesh: Every node interconnects for maximum reliability.",
                contentBn = "নেটওয়ার্কের প্রকারভেদ:\n• PAN: ব্যক্তিগত নেটওয়ার্ক (ব্লুটুথ)।\n• LAN: স্কুল বা অফিস ভবনের নেটওয়ার্ক।\n• WAN: বিশ্বব্যাপী বিস্তৃত ইন্টারনেট।\n\nটপোলজি:\n• Star: কেন্দ্রীয় সুইচ বা হাবের সাথে সব কম্পিউটার যুক্ত থাকে।\n• Ring: বৃত্তাকার সংযোগ।\n• Bus: একটি মূল ব্যাকবোন তারের সাথে যুক্ত।\n• Mesh: প্রতিটি ডিভাইস পরস্পরের সাথে যুক্ত।",
                classLevel = 8,
                subjectId = Subject.ICT.id,
                tagsCsv = "networking, topology, internet",
                updatedAt = now - 16000L
            ),
            // Class 9 - Physics/Science, Bangla & Math
            StudyNoteEntity(
                titleEn = "Newton's Three Laws of Motion",
                titleBn = "নিউটনের গতির তিনটি সূত্র",
                summaryEn = "Master inertia, F = ma, and action-reaction pairs with clear numerical examples.",
                summaryBn = "জড়তা, F = ma এবং ক্রিয়া-প্রতিক্রিয়া সূত্রের ধারণা ও গাণিতিক উদাহরণ।",
                contentEn = "1st Law (Law of Inertia):\nAn object remains at rest or in uniform motion unless acted on by an external net force.\n\n2nd Law (Force & Acceleration):\nF = m × a (Force in Newtons = Mass in kg × Acceleration in m/s²).\nExample: A 4 kg box accelerated at 3 m/s² needs F = 4 × 3 = 12 N.\n\n3rd Law (Action-Reaction):\nFor every action, there is an equal and opposite reaction.",
                contentBn = "১ম সূত্র (জড়তার সূত্র):\nবাহ্যিক বল প্রয়োগ না করলে স্থির বস্তু চিরকাল স্থির এবং গতিশীল বস্তু সুষম বেগে চলতে থাকে।\n\n২য় সূত্র (বল ও ত্বরণ):\nF = m × a (বল = ভর × ত্বরণ)।\nউদাহরণ: ৪ কেজি ভরের বস্তুতে ৩ m/s² ত্বরণ সৃষ্টি করতে বল F = ৪ × ৩ = ১২ নিউটন।\n\n৩য় সূত্র:\nপ্রত্যেক ক্রিয়ারই একটি সমান ও বিপরীতমুখী প্রতিক্রিয়া আছে।",
                classLevel = 9,
                subjectId = Subject.SCIENCE.id,
                tagsCsv = "physics, newton, force, motion",
                updatedAt = now - 17000L
            ),
            StudyNoteEntity(
                titleEn = "Bangla Sandhi (সন্ধি) — Swar & Byanjon Sandhi",
                titleBn = "বাংলা ব্যাকরণ: সন্ধি (স্বরসন্ধি ও ব্যঞ্জনসন্ধি)",
                summaryEn = "Rules for joining adjacent sounds cleanly with classic board-exam examples.",
                summaryBn = "স্বরসন্ধি ও ব্যঞ্জনসন্ধির গুরুত্বপূর্ণ সূত্র এবং পরীক্ষায় আসা উদাহরণ।",
                contentEn = "সন্ধি অর্থ মিলন। পাশাপাশি অবস্থিত দুটি ধ্বনির মিলনকে সন্ধি বলে।\n\nগুরুত্বপূর্ণ উদাহরণ:\n• বিদ্যা + আলয় = বিদ্যালয় (আ + আ = আ)\n• সূর্য + উদয় = সূর্যোদয় (অ + উ = ও)\n• নমঃ + কার = নমস্কার (বিসর্গ সন্ধি)\n• উৎ + চারণ = উচ্চারণ (ব্যঞ্জন সন্ধি)\n• পরি + ঈক্ষা = পরীক্ষা (ই + ঈ = ঈ)",
                contentBn = "সন্ধি অর্থ মিলন। পাশাপাশি অবস্থিত দুটি ধ্বনির মিলনকে সন্ধি বলে।\n\nগুরুত্বপূর্ণ উদাহরণ:\n• বিদ্যা + আলয় = বিদ্যালয় (আ + আ = আ)\n• সূর্য + উদয় = সূর্যোদয় (অ + উ = ও)\n• নমঃ + কার = নমস্কার (বিসর্গ সন্ধি)\n• উৎ + চারণ = উচ্চারণ (ব্যঞ্জন সন্ধি)\n• পরি + ঈক্ষা = পরীক্ষা (ই + ঈ = ঈ)",
                classLevel = 9,
                subjectId = Subject.BANGLA.id,
                tagsCsv = "সন্ধি, ব্যাকরণ, এসএসসি",
                updatedAt = now - 18000L
            ),
            // Class 10 - Math, ICT & English
            StudyNoteEntity(
                titleEn = "Quadratic Equations & Discriminant Formula",
                titleBn = "দ্বিঘাত সমীকরণ ও নিশ্চায়ক সূত্র",
                summaryEn = "Solve ax² + bx + c = 0 using factoring and the quadratic formula x = (-b ± √(b² - 4ac)) / 2a.",
                summaryBn = "উৎপাদকে বিশ্লেষণ ও শ্রীধর আচার্যের সূত্র ব্যবহার করে দ্বিঘাত সমীকরণের মূল নির্ণয়।",
                contentEn = "Standard Form: ax² + bx + c = 0 (a ≠ 0)\nQuadratic Formula: x = (-b ± √(b² - 4ac)) / 2a\n\nDiscriminant D = b² - 4ac:\n• D > 0 -> Two distinct real roots\n• D = 0 -> Two equal real roots\n• D < 0 -> No real roots (complex roots)\n\nExample: x² - 5x + 6 = 0 -> (x - 2)(x - 3) = 0 -> x = 2, 3.",
                contentBn = "আদর্শ রূপ: ax² + bx + c = 0 (a ≠ 0)\nমূল নির্ণয়ের সূত্র: x = (-b ± √(b² - 4ac)) / 2a\n\nনিশ্চায়ক D = b² - 4ac:\n• D > 0 হলে মূলদ্বয় বাস্তব ও অসমান\n• D = 0 হলে মূলদ্বয় বাস্তব ও সমান\n• D < 0 হলে কোনো বাস্তব মূল নেই।\n\nউদাহরণ: x² - 5x + 6 = 0 -> (x - 2)(x - 3) = 0 -> x = 2, 3।",
                classLevel = 10,
                subjectId = Subject.MATH.id,
                tagsCsv = "quadratic, algebra, ssc",
                updatedAt = now - 19000L
            ),
            StudyNoteEntity(
                titleEn = "Binary Numbers, Logic Gates & HTML Basics",
                titleBn = "বাইনারি সংখ্যা, লজিক গেট ও HTML মৌলিক ধারণা",
                summaryEn = "Convert decimal to binary, review AND/OR/NOT truth tables, and structure a basic webpage.",
                summaryBn = "ডেসিমাল থেকে বাইনারি রূপান্তর, AND/OR/NOT গেট ও HTML ট্যাগের ব্যবহার।",
                contentEn = "Decimal to Binary Conversion (Divide by 2):\n• 13₁₀ -> 13÷2=6(1), 6÷2=3(0), 3÷2=1(1), 1÷2=0(1) -> 1101₂.\n\nBasic Logic Gates:\n• AND: Output is 1 only if ALL inputs are 1.\n• OR: Output is 1 if ANY input is 1.\n• NOT: Inverts input (0 -> 1, 1 -> 0).\n\nHTML Skeleton:\n<html><head><title>Mystic</title></head><body><h1>Hello</h1></body></html>",
                contentBn = "ডেসিমাল থেকে বাইনারি রূপান্তর:\n• ১৩₁₀ -> ১১০১₂\n\nমৌলিক লজিক গেট:\n• AND গেট: সব ইনপুট ১ হলে আউটপুট ১ হয়।\n• OR গেট: যেকোনো একটি ইনপুট ১ হলে আউটপুট ১ হয়।\n• NOT গেট: ইনপুটকে উল্টে দেয় (০ -> ১, ১ -> ০)।\n\nHTML মৌলিক কাঠামো:\n<html><head><title>Mystic</title></head><body><h1>স্বাগতম</h1></body></html>",
                classLevel = 10,
                subjectId = Subject.ICT.id,
                tagsCsv = "binary, logic-gates, html, ssc",
                updatedAt = now - 20000L
            ),
            StudyNoteEntity(
                titleEn = "Narration (Direct & Indirect Speech) Guide",
                titleBn = "Narration (Direct ও Indirect Speech) নির্দেশিকা",
                summaryEn = "Master tense backshifting, pronoun changes, and reporting verbs for board exams.",
                summaryBn = "Direct থেকে Indirect Speech-এ পরিবর্তনের Tense, Pronoun ও সময়সূচক শব্দের নিয়ম।",
                contentEn = "Key Transformations:\n1. Reporting verb 'said to' -> 'told' (assertive) / 'asked' (interrogative).\n2. Tense Backshift (when reporting verb is past):\n• Present Simple -> Past Simple\n• Present Continuous -> Past Continuous\n• 'will' -> 'would', 'can' -> 'could'\n3. Time words: today -> that day, tomorrow -> the next day.\n\nExample:\nDirect: He said, \"I am studying math.\"\nIndirect: He said that he was studying math.",
                contentBn = "গুরুত্বপূর্ণ পরিবর্তন:\n১. Reporting verb 'said to' -> 'told' বা 'asked' হয়।\n২. Tense পরিবর্তন:\n• Present Simple -> Past Simple\n• Present Continuous -> Past Continuous\n• 'will' -> 'would', 'can' -> 'could'\n৩. সময়বাচক শব্দ: today -> that day, tomorrow -> the next day।\n\nউদাহরণ:\nDirect: He said, \"I am studying math.\"\nIndirect: He said that he was studying math.",
                classLevel = 10,
                subjectId = Subject.ENGLISH.id,
                tagsCsv = "narration, speech, ssc, grammar",
                updatedAt = now - 21000L
            )
        )
    }
}
