# ⚡ VCFS QUICK REFERENCE CARD

**Print this page and post on your monitor!**

---

## 📊 CURRENT STATUS AT A GLANCE

| Item | Status | Evidence |
|------|--------|----------|
| **Compilation** | ✅ READY | 40 .class files, 0 errors |
| **Core System** | ✅ READY | VCFS-001,002,003,004 implemented |
| **Models** | ✅ READY | All 37 classes complete |
| **Controllers** | ✅ READY | 3 controllers fully functional |
| **Tests** | 🟡 80% READY | 40 LocalDateTimeTest written |
| **Video** | 🔴 NOT STARTED | 0 hours used, 4 hours needed |
| **Docs** | 🟡 60% DONE | 5 docs written, diary pending |
| **Time Left** | ⏱️ ~25 hours | Plan for 20 hours work |

---

## 🎯 IMMEDIATE NEXT STEPS (In Order)

### Step 1: Verify Compilation (5 min)
```powershell
cd "C:\Users\ZAID SIDDIQUI\OneDrive - University of Stirling\stir uni\SEMESTERS\sem6 2026\CSCU9P6\ASSIGNMENTS\Grp_9_CSCU9P6"
$files = Get-ChildItem -Path src\main\java -Recurse -Filter "*.java"
javac -d bin -sourcepath src\main\java $files
(Get-ChildItem -Path bin -Recurse -Filter "*.class" | Measure-Object).Count
# Should output: 40
```

### Step 2: Run System Test (10 min)
- Launch App.java if exists
- Or launch Main.java
- Verify: No runtime exceptions

### Step 3: Record Video (3-4 hours)
- Use OBS Studio or screen recorder
- Follow workflow:
  1. Admin: Create org → booth → recruiter → set times
  2. Recruiter: Publish offers (should create 6 slots)
  3. Candidate: Submit request → auto-booking happens
  4. System: Time advances, meeting happens
- Export as VCFS_Screencast.mp4

### Step 4: Run Tests (2 hours)
- Mohamed focuses on: JUnit test execution
- Run LocalDateTimeTest first (40 tests)
- Debug any failures

### Step 5: Write Diary (2-3 hours)
- Each person writes 1 entry (20 min each = 1.5 hrs)
- Zaid consolidates into REFLECTIVE_DIARY.md (30 min)

### Step 6: Submit! (1 hour)
- Verify all files
- Upload to portal
- Save receipt

---

## 📁 KEY FILES LOCATION

| What | Where | Purpose |
|------|-------|---------|
| **Execution Plan** | docs/plan/PHASE_5_EXECUTION_SUMMARY.md | READ FIRST |
| **VCFS Algorithms** | docs/plan/ZAID_PART2_DIAGRAMS_AND_ALGORITHMS.md | Proof of VCFS-003/004 |
| **Test Blueprint** | docs/plan/JUNIT_TESTING_STRATEGY.md | How to test |
| **Submission Checklist** | docs/plan/FINAL_SUBMISSION_READINESS_CHECKLIST.md | Final verification |
| **Test Example** | src/test/java/vcfs/core/LocalDateTimeTest.java | 40 tests ready |
| **Source Code** | src/main/java/vcfs/ | 40 .java files |
| **Compiled Code** | bin/ | 40 .class files |

---

## 🎬 SCREENCAST CHECKLIST

**Before Recording**:
- [ ] Close email, Slack, browser tabs
- [ ] Set background to blank desktop
- [ ] Arrange windows for clear view
- [ ] Test audio works
- [ ] Have script/notes ready

**During Recording** (20-25 minutes):
- [ ] Admin: Create organization (2 min)
- [ ] Admin: Create booth, recruiter, set times (2 min)
- [ ] Recruiter: Publish offer block → verify 6 slots created (3 min)
- [ ] Candidate: Browse offers (2 min)
- [ ] Candidate: Submit request → auto-booking (3 min)
- [ ] System: Time advances, phase changes (3 min)
- [ ] Meeting: Join lobby, start, end (3 min)
- [ ] View results: Reservation, attendance records (2 min)

**After Recording**:
- [ ] Review video plays
- [ ] Audio is clear
- [ ] All workflows visible
- [ ] File size < 500MB
- [ ] Saved as VCFS_Screencast.mp4

---

## 📝 REFLECTIVE DIARY TEMPLATE

```markdown
# Entry 1: Architecture & Design
- Decision: Why MVC pattern?
- Justification: Separation of concerns
- Challenge: [describe]
- Resolution: [describe]
- Learning: [what you learned]

# Entry 2: Critical Issues Resolution
- Problem: [compilation errors, missing methods, etc]
- Analysis: [root cause]
- Solution: [how you fixed it]
- Prevention: [how to avoid next time]

[Continue for entries 3-5...]
```

---

## ⚙️ COMPILATION TROUBLESHOOTING

| Error | Solution |
|-------|----------|
| "cannot find symbol" | Check imports at top of file |
| "error: class X is public, should be declared in a file named X.java" | Rename file to match class name |
| "incompatible types" | Check method parameters/return types |
| "'javac' is not recognized" | Add Java to system PATH |
| "sourcepath: not a valid option" | Use `-sourcepath` (hyphen) not `/sourcepath` |

---

## 🧪 TEST EXECUTION QUICK GUIDE

### Using JUnit 5 (Preferred)
```bash
# Run single test class
java -cp bin org.junit.platform.console.ConsoleLauncher \
  --scan-classpath --select-class vcfs.core.LocalDateTimeTest

# Run all tests
java -cp bin org.junit.platform.console.ConsoleLauncher --scan-classpath
```

### If JUnit not available
```java
// Manual test execution in LocalDateTimeTest main()
public static void main(String[] args) {
    LocalDateTimeTest test = new LocalDateTimeTest();
    test.setUp();
    
    // Run each test
    test.testConstructor_ValidInputs();
    test.testIsBefore_EarlierTime_ReturnsTrue();
    // ... etc
    
    System.out.println("All tests passed!");
}
```

---

## 💾 FINAL SUBMISSION PACKAGE

```
VCFS_Group9_Submission.zip
├── README.md (HOW TO USE THIS SUBMISSION)
├── ARCHITECTURE.md (System design)
├── PHASE_5_EXECUTION_SUMMARY.md (This document)
├── REFLECTIVE_DIARY.md (5 entries)
├── GROUP_CONTRIBUTION_SUMMARY.md (Who did what)
├── VCFS_SCREENCAST.mp4 (20-25 min video)
├── src/
│   ├── main/java/vcfs/ (40 .java source files)
│   └── test/java/vcfs/ (test files)
├── bin/ (40 .class compiled files)
└── docs/
    ├── ARCHITECTURE.md
    ├── ZAID_PART2_DIAGRAMS_AND_ALGORITHMS.md
    ├── JUNIT_TESTING_STRATEGY.md
    └── [all other planning docs]
```

---

## 🔴 DO NOT FORGET

- ❌ **NO LATE SUBMISSIONS** - Deadline is absolute
- ❌ **NO HARDCODED PATHS** in code (remove before submitting)
- ❌ **NO DEBUG PRINT STATEMENTS** left in production code
- ✅ **DO SAVE BACKUP** of everything locally
- ✅ **DO NOTE SUBMISSION TIME** for records
- ✅ **DO KEEP RECEIPTS** of upload confirmation

---

## 📞 TEAM ASSIGNMENTS

| Person | Task | Hours | Hours Left |
|--------|------|-------|-----------|
| Zaid | Overall coordination | — | 5 |
| Mohamed | JUnit test execution | 2 | 2 |
| Taha | Screencast recording | 3-4 | 3-4 |
| YAMI | Reflective diary entry | 0.5 | 0.5 |
| MJAMishkat | Reflective diary entry | 0.5 | 0.5 |
| Zaid (cont) | Consolidate & submit | 2 | 2 |

---

## ✅ FINAL CHECKLIST

Before hitting SUBMIT:
- [ ] Compilation: 40 classes, 0 errors ✅
- [ ] Video: 20-25 min, .mp4 format, < 500MB ✅
- [ ] Diary: 5 entries, thoughtful & detailed ✅
- [ ] Documentation: All 5 docs complete ✅
- [ ] No secrets/credentials in files ✅
- [ ] Total size < 500MB ✅
- [ ] All team members credited ✅
- [ ] Submission portal ready ✅
- [ ] TIME CHECK: Before 23:59 UTC April 8 ✅

---

## 🎉 SUCCESS METRICS

| Metric | Target | What You Have |
|--------|--------|---------------|
| Compilation | 0 errors | ✅ 0 errors verified |
| VCFS specs | All 4 (001-004) | ✅ All implemented |
| Tests | 50+ methods | 🟡 40 written, rest templated |
| Screencast | 20-25 min | 🔴 In progress |
| Grade prediction | A-/B+ | 🟡 On track |

---

**SAVE THIS PAGE. Print it. Reference it hourly.**

**You've got this! 🚀**

