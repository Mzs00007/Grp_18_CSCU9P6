# 🚀 PHASE 1 EXECUTION — Pull GitHub & Merge All Team Code
## Detailed Step-by-Step Walkthrough

**Objective**: Clone the GitHub repository and merge all team branches into a single unified codebase

**Duration**: 6 hours (but usually faster — most time is understanding what was built)

**Expected Outcome**: One complete codebase locally with all team member contributions merged

---

## 📍 Current State (Before Phase 1)
```
Grp_9_CSCU9P6/
├── docs/plan/           ← Architecture + implementation blueprints (created earlier)
├── src/main/java/vcfs/  ← Skeleton files (empty implementations, perfect structure)
├── README.md
├── compile_errors.txt   ← Old compile errors (we'll be fixing these)
└── .git/                ← Local git repository

GitHubVersion/          ← DOES NOT EXIST YET!
```

After Phase 1, we'll have:
```
Grp_9_CSCU9P6/          ← Your main project (will be updated)
GitHubVersion/          ← Clone of team's GitHub code
├── com.mycompany.admin/  (YAMI's code)
├── Recruitment/          (Taha's code)
├── Main.java, LoginFrame.java (Anonymous team member)
└── ... all other files merged from branches
```

---

## 🔍 Understanding GitHub Branches

Your team has been working on **separate branches**:

```
GitHub Repository: Mzs00007/Grp_9_CSCU9P6
│
└─ main (default branch)
   ├─ recruitement-system (branch)  ← Taha's recruiter UI code
   └─ PR #1: yami-admin-pr          ← YAMI's admin dashboard + model
```

**What you're doing in this phase:**
1. Clone the repo locally (gets main + all branches)
2. Checkout main
3. Merge recruitement-system into main
4. Merge yami-admin-pr into main
5. Verify everything is there

---

# STEP 1: Clone The GitHub Repository

## 1.1 Open PowerShell and Navigate to Assignments Folder

```powershell
cd "c:\Users\ZAID SIDDIQUI\OneDrive - University of Stirling\stir uni\SEMESTERS\sem6 2026\CSCU9P6\ASSIGNMENTS\Grp_9_CSCU9P6"
```

Verify you're in the right place:
```powershell
Get-Location
pwd  # Alias for Get-Location
```

Expected output: Path ending with `...Grp_9_CSCU9P6`

## 1.2 Clone the GitHub Repository

```powershell
git clone https://github.com/Mzs00007/Grp_9_CSCU9P6.git GitHubVersion
```

**What this does:**
- Downloads everything from the GitHub remote
- Creates a folder called `GitHubVersion/`
- Copies all history, all branches, all commits

**Expected output:**
```
Cloning into 'GitHubVersion'...
remote: Enumerating objects: X, done.
remote: Counting objects: 100% (X/X), done.
remote: Compressing objects: 100% (X/X), done.
remote: Receiving objects: 100% (X/X), done.
Resolving deltas: 100% (X/X), done.
```

## 1.3 Navigate into the Cloned Repository

```powershell
cd GitHubVersion
```

Verify:
```powershell
ls -la  # List all files, including hidden .git folder
```

Expected: You should see `.git/` folder present.

---

# STEP 2: Examine All Branches

## 2.1 List All Available Branches

```powershell
git branch -a
```

**What this shows:**
- `* main` (or master) — Current branch (marked with *)
- `remotes/origin/main` — Remote version of main
- `remotes/origin/recruitement-system` — Taha's branch
- Possibly `remotes/origin/yami-admin-pr` or other branches

**Example output:**
```
* main
  remotes/origin/HEAD -> origin/main
  remotes/origin/main
  remotes/origin/recruitement-system
```

## 2.2 List All Available Files Currently on Main

```powershell
Get-ChildItem -Path "." -Filter "*.java" -Recurse | Select-Object Name, @{N='RelPath'; E={$_.FullName -replace [regex]::Escape((Get-Location)), ''}}
```

Or simpler:
```powershell
dir -Recurse -Filter "*.java"
```

**What you should see**: Files like `Main.java`, `LoginFrame.java`, etc. — the GitHub team's working code.

---

# STEP 3: Merge Taha's Recruiter Branch

## 3.1 Ensure You're on Main

```powershell
git checkout main
```

Expected output:
```
Switched to branch 'main'
```

## 3.2 Fetch Latest from Remote (Just in case)

```powershell
git fetch origin
```

This pulls the latest metadata from GitHub.

## 3.3 Merge Taha's Recruiter Code

```powershell
git merge origin/recruitement-system
```

**What happens:**
- Git finds all commits in `recruitement-system` branch that aren't in `main`
- Applies those commits to your current main branch
- Updates all files accordingly

**Expected output** (if there are no conflicts):
```
Updating abc1234..def5678
Fast-forward
 RecruiterScreen.java         | 150 ++
 PublishOfferPanel.java       | 100 ++
 SchedulePanel.java           |  80 ++
 VirtualRoomPanel.java        |  60 ++
 Recruiter.java               |  50 ++
 Offer.java                   |  40 ++
 ... etc ...
 7 files changed, 480 insertions(+)
```

**If there ARE conflicts** (unlikely since Taha worked in isolation):
```
Auto-merging some_file.java
CONFLICT (content merge conflict)
```

Then run:
```powershell
git merge --abort  # Cancel this merge
# OR
git status  # See which files have conflicts
# Fix conflicts manually, then:
git add .
git commit -m "Merge recruitement-system branch"
```

---

# STEP 4: Merge YAMI's Admin Branch

## 4.1 List Pull Requests and Branches

```powershell
git branch -a
git log --oneline --all --graph -20  # Show last 20 commits on all branches
```

Look for YAMI's branch. It might be:
- `origin/yami-admin-pr`
- `origin/yami-pr` 
- `origin/administrator-module`
- Or a PR number like `pull/1`

## 4.2 Fetch YAMI's PR Branch (If It's a Pull Request)

If YAMI's code is in a pull request (not a branch), fetch it as:

```powershell
git fetch origin pull/1/head:yami-admin-pr
```

(Replace `1` with the actual PR number if different)

**What this does:**
- `pull/X/head` is GitHub's special reference for PR #X
- `:yami-admin-pr` creates a local branch with that name

## 4.3 Merge YAMI's Code into Main

```powershell
git merge yami-admin-pr
```

Expected output (if no conflicts):
```
Merge made by the 'recursive' strategy.
 AdministratorScreen.java    | 200 ++
 AdminController.java        | 150 ++
 CentralModel.java           | 180 ++
 Observable.java             |  50 ++
 Observer.java               |  30 ++
 ... etc ...
```

---

# STEP 5: Verify All Files Are Present

## 5.1 Count Total Java Files

```powershell
(Get-ChildItem -Path "." -Filter "*.java" -Recurse).Count
```

**Expected**: Somewhere between 15-30 .java files (depends on team's output).

## 5.2 List All Java Files with Their Paths

```powershell
Get-ChildItem -Path "." -Filter "*.java" -Recurse | 
  Select-Object FullName | 
  ForEach-Object { $_.FullName -replace [regex]::Escape((Get-Location) + '\'), '' } | 
  Sort-Object
```

**Expected files**:
```
Main.java
LoginFrame.java
AdministratorScreen.java
AdminController.java
CentralModel.java
Observable.java
Observer.java
RecruiterScreen.java
PublishOfferPanel.java
SchedulePanel.java
VirtualRoomPanel.java
Recruiter.java
Offer.java
Booking.java
... etc ...
```

## 5.3 Check for Folder Structure

```powershell
Get-ChildItem -Path "." -Directory -Recurse | Select-Object Name, FullName
```

You should see folders like:
- `com.mycompany.admin/` (YAMI's code)
- `Recruitment/` (Taha's code)
- Or various other arrangements

## 5.4 Create a Detailed File Listing Report

```powershell
Get-ChildItem -Path "." -Filter "*.java" -Recurse | 
  Select-Object Name, Directory, @{N='SizeMB';E={[math]::Round($_.Length/1MB, 2)}} | 
  Sort-Object Directory | 
  Format-Table -AutoSize | 
  Tee-Object -FilePath "file_inventory.txt"

# Also show the count
Write-Host "Total Java files: $((Get-ChildItem -Path '.' -Filter '*.java' -Recurse).Count)"
```

---

# STEP 6: Understand What Each File Does

Create a mapping document:

```powershell
$mapping = @"
FILE INVENTORY — GitHub Code (After Phase 1 Merge)
====================================================

ENTRY POINT:
├─ Main.java                 Purpose: App entry point, launches LoginFrame
├─ LoginFrame.java           Purpose: Swing login UI (username/password)

ADMIN MODULE (YAMI):
├─ AdministratorScreen.java  Purpose: Admin dashboard with tabs (orgs, booths, recruiters, timeline)
├─ AdminController.java      Purpose: Business logic for admin actions
├─ CentralModel.java         Purpose: Shared data model (Observable pattern)
├─ Observable.java           Purpose: Base class for Observer pattern
├─ Observer.java             Purpose: Observer interface

RECRUITER MODULE (Taha):
├─ RecruiterScreen.java      Purpose: Recruiter dashboard with 3 tabs
├─ PublishOfferPanel.java    Purpose: Panel for recruiter to publish availability
├─ SchedulePanel.java        Purpose: Panel showing recruiter's schedule
├─ VirtualRoomPanel.java     Purpose: Panel for virtual meeting room

DATA MODELS:
├─ Recruiter.java            Purpose: Recruiter user model
├─ Offer.java                Purpose: Availability/booking slot model
├─ Booking.java              Purpose: Confirmed reservation model
├─ [Others: Candidate.java, Organization.java, Booth.java, etc.]

ARCHITECTURE:
├─ Package structure:
│  ├─ root (no package) → Main, LoginFrame
│  ├─ com.mycompany.admin → AdminController, AdministratorScreen, CentralModel, Observable, Observer
│  ├─ Recruitment → RecruiterScreen, RecruiterScreen panels, Recruiter, Offer, Booking
│  └─ [Other packages as deployed]
"@

$mapping | Tee-Object -FilePath "PHASE1_FILE_INVENTORY.txt"
```

---

# STEP 7: Check Compilation Status

## 7.1 Try Compiling GitHub Code (See What Errors Exist)

```powershell
javac -d out *.java 2>&1 | Tee-Object -FilePath "github_compile_errors.txt"
```

**Expected**: You will see compilation errors (imports, cross-package references) — this is normal and expected. We'll fix these in Phase 2.

## 7.2 Count Compilation Errors

```powershell
$errors = @(Get-Content github_compile_errors.txt | Select-String "error:")
$errors.Count
$errors | Select-Object -First 10  # Show first 10 errors
```

---

# STEP 8: Create Phase 1 Summary Report

```powershell
$summary = @"
╔══════════════════════════════════════════════════════════════╗
║           PHASE 1 EXECUTION SUMMARY                          ║
║     Pull GitHub Code & Merge All Team Branches               ║
║                                                              ║
║  Date: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')       ║
╚══════════════════════════════════════════════════════════════╝

✅ COMPLETED ACTIONS:
----------------------------------------------------
1. ✅ Cloned GitHub repository to GitHubVersion/
2. ✅ Merged origin/recruitement-system (Taha's recruiter code)
3. ✅ Merged yami-admin-pr (YAMI's admin code)
4. ✅ Verified all files present
5. ✅ Documented file inventory
6. ✅ Checked compilation (errors documented)

📊 CURRENT STATE:
----------------------------------------------------
Location: GitHubVersion/
Total Java Files: $(((Get-ChildItem -Path '.' -Filter '*.java' -Recurse).Count))
Git Status: $(git status --short | Measure-Object).Count changes pending
Current Branch: $(git branch --show-current)

📝 FILES MERGED FROM TEAM:
----------------------------------------------------
Team Member: Taha (CodeByTaha18)
- Merged from: origin/recruitement-system
- Files: RecruiterScreen.java, PublishOfferPanel.java, SchedulePanel.java, VirtualRoomPanel.java, etc.
- Purpose: Complete Recruiter UI module

Team Member: YAMI (6igglepill)
- Merged from: yami-admin-pr (PR #1)
- Files: AdministratorScreen.java, AdminController.java, CentralModel.java, Observable.java, Observer.java
- Purpose: Complete Admin UI module + shared data model

Team Member: Anonymous
- Files: Main.java, LoginFrame.java, Booking.java
- Purpose: App entry point + login flow + booking model

⚠️  NEXT PHASE: Phase 2 — Fix Compilation Errors
----------------------------------------------------
Current Issues: Compilation errors due to:
  - Missing import statements (cross-package references)
  - Package structure mismatch
  - Access level issues (private vs public methods)

Action: Will systematically add imports and fix visibility issues
Estimated Time: 4 hours
Expected Outcome: 0 compilation errors, ready for Phase 3

📌 KEY INSIGHTS:
----------------------------------------------------
- Team did NOT integrate code into skeleton (vcfs/ package structure)
- Team created parallel implementation in com.mycompany.admin/ and Recruitment/
- Team code is FUNCTIONAL and working
- Solution: Phase 3 will integrate both (skeleton architecture + team UI)

"@

$summary | Tee-Object -FilePath "PHASE1_SUMMARY_REPORT.txt"
Write-Host $summary -ForegroundColor Green
```

---

# STEP 9: Push to GitHub (Optional — Record Your Progress)

If you want to save your progress to GitHub:

```powershell
git push origin main  # Push merged main back to GitHub
```

---

## ✅ PHASE 1 COMPLETE CHECKLIST

- [ ] Navigated to Grp_9_CSCU9P6 folder
- [ ] Cloned GitHub repo to GitHubVersion/
- [ ] Listed all branches with `git branch -a`
- [ ] Merged `origin/recruitement-system` (Taha's code)
- [ ] Merged `yami-admin-pr` (YAMI's code)
- [ ] Verified all .java files are present
- [ ] Created file inventory
- [ ] Attempted compilation (documented errors)
- [ ] Created Phase 1 Summary Report
- [ ] Understood what each team member contributed

---

## 📊 PHASE 1 → PHASE 2 TRANSITION

**Input to Phase 2**: 
- Single unified codebase with all team contributions merged
- Documented compilation errors
- Clear understanding of what was built

**Output from Phase 2**: 
- Zero compilation errors
- Ready to implement your core (SystemTimer, CareerFair, etc.)

**Continuation**: After Phase 1 is done, move to **PHASE_2_COMPILATION_FIXES.md** (I'll create it)

---

**Document Version**: 1.0  
**Status**: Ready for execution  
**Updated**: April 6, 2026 17:00
