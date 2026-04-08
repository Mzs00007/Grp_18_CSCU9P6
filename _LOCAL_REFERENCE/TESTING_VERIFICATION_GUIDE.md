# 🧪 ED Testing & Verification Guide

## Overview
This guide explains exactly what to test to verify that the EDT threading fixes resolved the recruiter/candidate synchronization issues.

---

## ✅ Test 1: Recruiter Registration in Admin Portal

### Setup
1. Launch the Application
2. Login as Administrator
3. Navigate to Admin Portal
4. Go to the **"Recruiters"** tab

### Test Steps
1. Observe the recruiter table (should be mostly empty or show existing recruiters)
2. Click **"Register New Recruiter"** button
3. Fill in recruiter details:
   - **Display Name:** `Test Recruiter 1`
   - **Email:** `recruiter.test@company.com`
   - **Password:** `testpass123`
4. Click **"Register"** button
5. Watch the recruiter table

### ✅ Expected Result
- **IMMEDIATELY** after clicking Register, the recruiter table should:
  - Clear and repopulate
  - Show new recruiter `Test Recruiter 1` with email `recruiter.test@company.com`
  - Display `0` offers (since no offers published yet)

### ❌ If It Fails
- Table does NOT update
- You need to manually click "Refresh Data" button to see the new recruiter
- **This means EDT fix didn't work**

### 🔍 Visual Verification
```
Before Registration:
┌─────────────────────────────────────────────────┐
│ Recruiter Name  │ Email        │ Booth │ Offers │
├─────────────────────────────────────────────────┤
│ (No recruiters) │ -            │ -     │ 0      │
└─────────────────────────────────────────────────┘

After Registration (should be INSTANT):
┌──────────────────────────────────────────────────┐
│ Recruiter Name   │ Email                  │ Booth │ Offers │
├──────────────────────────────────────────────────┤
│ Test Recruiter 1 │ recruiter.test@company │ -     │ 0      │
└──────────────────────────────────────────────────┘
```

---

## ✅ Test 2: Candidate Registration in Admin Portal

### Setup
1. Stay in Admin Portal
2. Go to the **"Candidates"** tab
3. Observe the candidate table

### Test Steps
1. Click **"Register New Candidate"** button
2. Fill in candidate details:
   - **Display Name:** `Test Candidate 1`
   - **Email:** `candidate.test@university.edu`
   - **Password:** `testpass123`
3. Click **"Register"** button
4. Watch the candidate table

### ✅ Expected Result
- **IMMEDIATELY** after clicking Register, the candidate table should:
  - Clear and repopulate
  - Show new candidate `Test Candidate 1` with email `candidate.test@university.edu`
  - Show `-` for Bio and Skills (since not set yet)

### ❌ If It Fails
- Table does NOT update
- You need to manually click "Refresh Data" button to see the new candidate
- **This means EDT fix didn't work**

### 🔍 Visual Verification
```
Before Registration:
┌──────────────────────────────────────────┐
│ Candidate Name   │ Email       │ Bio │ Skills │
├──────────────────────────────────────────┤
│ (No candidates)  │ -           │ -   │ -      │
└──────────────────────────────────────────┘

After Registration (should be INSTANT):
┌──────────────────────────────────────────┐
│ Candidate Name  │ Email              │ Bio │ Skills │
├──────────────────────────────────────────┤
│ Test Candidate1 │ candidate.test@... │ -   │ -      │
└──────────────────────────────────────────┘
```

---

## ✅ Test 3: Offer Publishing & Real-Time Sync

### Setup
1. **Open TWO windows** of the application side-by-side
   - **Window A:** Admin Portal logged in as Admin
   - **Window B:** Recruiter Portal logged in as recruiter (use Test Recruiter 1)

### Test Steps
1. In **Window A**, go to Admin Portal → "Offers" tab
2. Observe the offers table (should be mostly empty)
3. In **Window B**, go to Recruiter Portal → "Publish Offer" panel
4. Fill in offer details:
   - **Offer Title:** `Senior Developer Position`
   - **Duration:** `60` minutes
   - **Tags:** `Java, Spring Boot, REST APIs`
   - **Capacity:** `5`
5. Click **"Publish Offer"** button
6. Watch **Window A** offers table

### ✅ Expected Result
- **IMMEDIATELY** after publishing in Window B:
  - Window A's offers table should:
    - Clear and repopulate
    - Show new offer `Senior Developer Position`
    - Show `0` bookings (no candidates booked yet)
    - Show capacity `5`
    - Show recruiter name `Test Recruiter 1`

### ❌ If It Fails
- Window A's offers table does NOT update
- Offer appears only after manually clicking "Refresh Data" in Window A
- **This means PropertyChangeEvent firing or EDT marshaling failed**

---

## ✅ Test 4: Cross-Portal Candidate/Recruiter Sync

### Setup
1. Have multiple windows open:
   - Admin Portal
   - Recruiter Portal
   - Candidate Portal (if different instance needed)

### Test Steps - Register Multiple Recruiters
1. Click "Register New Recruiter" multiple times:
   - `Alice Manager` - alice@company.com
   - `Bob Manager` - bob@company.com
   - `Charlie Manager` - charlie@company.com
2. Watch Admin Portal's recruiter table

### ✅ Expected Result
- Each recruiter appears in the table as you register them
- No lag, no need to refresh manually
- All 3 recruiters visible in recruiter list

### Test Steps - Register Multiple Candidates
1. Click "Register New Candidate" multiple times:
   - `David Student` - david@university.edu
   - `Emma Student` - emma@university.edu
   - `Frank Student` - frank@university.edu
2. Watch Admin Portal's candidate table

### ✅ Expected Result
- Each candidate appears in the table as you register them
- No lag, no need to refresh manually
- All 3 candidates visible in candidate list

---

## 📋 Test Checklist

Use this checklist to verify all synchronization is working:

### Recruiter Registration
- [ ] Recruiter appears in table immediately after registration
- [ ] Table clears and repopulates (not just appends row)
- [ ] Offer count shows `0` for new recruiter
- [ ] Email matches what was registered
- [ ] Display name shows correctly

### Candidate Registration
- [ ] Candidate appears in table immediately after registration
- [ ] Table clears and repopulates (not just appends row)
- [ ] Email matches what was registered
- [ ] Display name shows correctly
- [ ] Bio and Skills show `-` initially

### Offer Publishing
- [ ] Offer appears in Admin Portal table immediately
- [ ] Offer count updates for recruiter row (0 → 1, etc.)
- [ ] Recruiter name, title, duration, tags all shown correctly
- [ ] Capacity shows correct value
- [ ] Booking count starts at `0`

### Multi-Window Sync
- [ ] Changes in one window appear in another without manual refresh
- [ ] No race conditions or duplicate entries
- [ ] Tables stay in sync across windows

### Performance
- [ ] Updates appear within 1-2 seconds
- [ ] No UI freezing or lag during updates
- [ ] No errors in console logs

---

## 🔍 Logging Output to Watch

When each event fires, you'll see logs like:

```
[AdminScreen] Recruiters updated - refreshing recruiter table
[AdminScreen] Candidates updated - refreshing candidate table
[AdminScreen] Organizations updated - refreshing dropdowns
[AdminScreen] All tables refreshed
```

### Step-by-Step Log for Test 1 (Register Recruiter)
```
[RegisterRecruiterPanel] Register button clicked
[CareerFairSystem] registerRecruiter called: Test Recruiter 1
[CareerFairSystem] Recruiter registered: test.recruiter@company.com
[CareerFairSystem] Firing PropertyChangeEvent: recruiters
[AdminScreen] Recruiters updated - refreshing recruiter table
[AdminScreen] Recruiter table refreshed successfully
```

If you **DON'T see** the "[AdminScreen] Recruiters updated" log after registration, the PropertyChange event wasn't fired - check CareerFairSystem.registerRecruiter().

---

## ⚠️ Common Issues & Troubleshooting

### Issue: Table doesn't update after registration

**Possible Causes:**
1. PropertyChange event not firing
   - Check: CareerFairSystem.registerRecruiter() line ~366
   - Should have: `firePropertyChange("recruiters", null, recruiter);`

2. PropertyChangeListener not registered
   - Check: AdminScreen constructor, line ~130
   - Should have: `CareerFairSystem.getInstance().addPropertyChangeListener(this);`

3. Handler exists but not calling EDT wrapper
   - Check: AdminScreen.propertyChange() lines 175-225
   - Should have: `SwingUtilities.invokeLater(() -> { ... })`

### Issue: Updates appear but are delayed (2-5 seconds)

**Likely Cause:** EDT queue is backed up
- Check: Are you registering lots of entities rapidly?
- Solution: This is normal - EDT processes all updates in order

### Issue: Duplicate entries or partial updates

**Likely Cause:** Table not being cleared properly
- Check: refreshRecruitersTable() should call `setRowCount(0)` before populating
- Should be at line ~956: `this.recruitersTableModel.setRowCount(0);`

### Issue: Errors in console

**Check Console For:**
```
java.lang.NullPointerException - recruitersTableModel is null
    at AdminScreen.refreshRecruitersTable()
```
- Create the table model in the constructor
- Initialize all table models before listening to events

---

## 🎯 Final Verification

When all tests pass:
- ✅ Recruiter registration updates table IMMEDIATELY
- ✅ Candidate registration updates table IMMEDIATELY  
- ✅ Offer publishing updates Admin table IMMEDIATELY
- ✅ Multi-window sync working perfectly
- ✅ No manual refresh needed
- ✅ All 4 portals see real-time updates
- ✅ No errors or edge cases

**You're done! The EDT threading fix is working perfectly!** 🚀

---

## 📚 Additional Resources

- **AdminScreen.java:** Lines 175-225 (propertyChange handler)
- **AdminScreen.java:** Lines 948-1065 (table refresh methods)
- **CareerFairSystem.java:** Lines 366-410 (register methods)
- **EDT_THREADING_SYNC_FIX_DOCUMENTATION.md:** Detailed technical explanation

