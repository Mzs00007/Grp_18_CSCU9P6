# ⚡ QUICK REFERENCE - ONE PAGE TRACKER

**START TIME**: NOW (23:40 UTC, April 7)  
**DEADLINE**: 23:59 UTC, April 8  
**MODE**: FULL SEND - All components, A-grade target

---

## 🌙 RIGHT NOW (Next 4 hours) - SCREENCAST RECORDING

**Timeline**:
- 23:40-00:10: Setup OBS/Game Bar (30 min)
- 00:10-01:10: Verify compilation (1 hour)
- 01:10-02:30: Record Segments 1-3 (80 min) ← **THIS IS main event**
- 02:30-03:00: Export + verify video (30 min)
- 03:00-04:00: Sleep (1 hour minimum, can be 3-4)

**Recording Segments**:
```
Segment 1 (Admin): org → booth → recruiter → timeline setup (8 min total)
Segment 2 (Recruiter): publish offers → 6 slots generated (13 min total)
Segment 3 (Candidate + Meeting): browse → request → auto-book → meeting lifecycle (30 min total)
```

**Output**: `VCFS_Screencast.mp4` in project root

---

## 🌅 MORNING (Hours 7-11) - TESTING

**Timeline**:
- 07:00-08:00: JUnit setup (download JAR files)
- 08:00-11:00: Execute tests + fix failures

**Key command**:
```powershell
javac -cp bin:lib/junit-platform-console-standalone-1.9.2.jar -d bin src/test/java/vcfs/**/*.java
java -cp bin:lib/* org.junit.platform.console.ConsoleLauncher --scan-classpath --details=verbose
```

**Expected**: 88 tests total (40 LocalDateTime + 18 SystemTimer + 30+ CareerFair)

**Save results**: `TEST_RESULTS_FINAL.txt` in `docs/`

---

## 📝 AFTERNOON (Hours 12-16) - DOCUMENTATION

**Timeline**:
- 12:00-13:00: Write 5 diary entries (200 words each)
- 13:00-14:00: Fill contribution forms
- 14:00-15:00: Write IMPLEMENTATION_NOTES.md
- 15:00-16:00: Update README.md
- 16:00-17:00: Final verification

**Files to create/update**:
- [ ] `docs/plan/REFLECTIVE_DIARY_ENTRIES.md` (5 entries)
- [ ] `docs/plan/INDIVIDUAL_CONTRIBUTION_FORM.md` (your roles)
- [ ] `docs/plan/IMPLEMENTATION_NOTES.md` (technical details)
- [ ] `README.md` (project overview, updated)

---

## ✅ DELIVERABLES CHECKLIST

Must have by 23:59 UTC April 8:

```
SUBMISSION PACKAGE:
├── ✅ Code (already done)
│   ├── src/main/java/vcfs/ (40 .java files)
│   └── bin/ (40 .class files)
├── 🔴 VCFS_Screencast.mp4 (25 min video)
├── 🔴 docs/plan/REFLECTIVE_DIARY_ENTRIES.md (5 entries)
├── 🔴 docs/plan/INDIVIDUAL_CONTRIBUTION_FORM.md (your work)
├── 🔴 docs/plan/IMPLEMENTATION_NOTES.md (technical)
├── ✅ docs/plan/ARCHITECTURE.md (done)
├── ✅ docs/plan/CODE_QUALITY_AUDIT_REPORT.md (done)
├── ✅ docs/plan/JUNIT_TESTING_STRATEGY.md (done)
├── 🔴 TEST_RESULTS_FINAL.txt (88 tests, pass/fail)
├── 🔴 README.md (updated)
└── ✅ .git/ (GitHub repo committed)
```

**Legend**: ✅ = Done | 🔴 = Do tonight/tomorrow

---

## ⏰ HOUR-BY-HOUR PROGRESS

| Time | Task | Status | Notes |
|------|------|--------|-------|
| 23:40 | Setup OBS | ⏳ | Start NOW |
| 00:10 | Verify compile | ⏳ | Should be 0 errors |
| 01:10 | Record Seg 1 | ⏳ | Admin setup |
| 01:40 | Record Seg 2 | ⏳ | Recruiter |
| 02:10 | Record Seg 3 | ⏳ | Candidate meeting |
| 03:00 | Export video | ⏳ | Save as .mp4 |
| 04:00 | **SLEEP 3-4h** | ⏳ | REST IS CRITICAL |
| 08:00 | JUnit setup | ⏳ | Download JAR files |
| 09:00 | Run tests | ⏳ | Execute suite |
| 12:00 | Diary entries | ⏳ | 5 × 200 words |
| 13:00 | Forms + notes | ⏳ | Contribution form |
| 15:00 | README update | ⏳ | Final docs |
| 17:00 | Final verify | ⏳ | All files present |
| 19:00 | Git commit | ⏳ | Push to GitHub |
| 23:00 | Verify deadline | ⏳ | 59 min buffer |
| 23:59 | **SUBMIT** | 🚀 | **DO NOT BE LATE** |

---

## 🚨 IF SOMETHING GOES WRONG

| Problem | Solution | Time |
|---------|----------|------|
| Video corrupted | Re-record that segment (8-30 min each) | 30 min |
| Compilation fails | Debug + recompile | 15-45 min |
| Test fails | Check assertion, fix code/test | 15-30 min per test |
| Screencast too long | Edit/trim segments | 30 min |
| Run out of time | Submit what you have (partial = better than nothing) | - |

**Buffer time available**: 4 hours (18:00-23:00). Use wisely!

---

## 💡 FINAL REMINDERS

1. **Sleep 3+ hours** (even if interrupted)
2. **One task at a time** (don't context-switch)
3. **Save frequently** (commit to backup)
4. **Test before final submit** (verify video plays)
5. **Don't panic** (you've got a plan, just execute it)
6. **Missing sleep = mistake quality** (REST = SPEED)

---

## 🎯 EXECUTION COMMAND

```powershell
# Right now, verify system is ready
cd "c:\Users\ZAID SIDDIQUI\OneDrive - University of Stirling\stir uni\SEMESTERS\sem6 2026\CSCU9P6\ASSIGNMENTS\Grp_9_CSCU9P6"

# Check compilation
$files = Get-ChildItem -Path src\main\java -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
javac -d bin -sourcepath src\main\java $files 2>&1 | Select-String "error" | Measure-Object
# Should show: Count : 0

# Launch system test
java -cp bin vcfs.App &
Start-Sleep -Seconds 5
# Kill with: Stop-Job -Id XX

# If all successful → START RECORDING
```

---

## ✨ YOU'VE GOT THIS

You have:
- Complete code (0 errors)
- Complete plan (hour-by-hour)
- Complete scripts (copy-paste ready)
- 23 hours (11+ needed = 12+ buffer)
- Full capacity (single focus, no distractions)

**Result: A grade is achievable. Execute and deliver.** 🚀

---

**NOW: Open OBS/Game Bar. Start recording. Don't delay.**

