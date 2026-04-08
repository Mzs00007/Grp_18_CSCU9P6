# Individual Reflective Diary: Virtual Career Fair System (VCFS)

**Name:**  Mohammad Zaid Siddiqui
**Role:** Project Manager & Lead Developer
**Group:** 18
**Module:** CSCU9P6

## STAR-L Reflection

### Situation (S)
As the Project Manager and Lead Developer for Group 9, I was responsible for overseeing the development of the Virtual Career Fair System (VCFS) and ensuring the architectural integrity of our solution. Towards the end of the project lifecycle, we encountered significant challenges integrating disparate modules submitted by team members (Taha, YAMI, MJAMishkat, Mohamed). The codebase suffered from compilation errors, particularly on different Operating Systems, encapsulation violations with exposed public fields in core data models, and a fragmented approach to core features such as system time synchronization and meeting scheduling. The final deadline was imminent, requiring an emergency recovery and integration phase.

### Task (T)
My primary goal was to rescue the project by standardizing the architecture, resolving critical compilation blockers, and finalizing the core backend infrastructure so that the User Interfaces would function properly. Specifically, I needed to:
1. Fix the cross-platform execution script (`run_vcfs.bat`) which was failing due to improper classpath handling and escape sequences on Windows environments.
2. Implement strict Object-Oriented principles by refactoring all exposed public fields (e.g., in `AttendanceRecord`, `MeetingSession`) to private fields with robust getters/setters.
3. Centralize system time management using the Observer pattern (`SystemTimer`) so all UI screens and backend scheduling logic shared the same simulated temporal context.
4. Finalize the centralized matching engine (`CareerFairSystem`) and logging utilities to ensure consistent program execution and bug tracking.

### Action (A)
I executed a strict code freeze on new features and initiated a unified code audit and refactoring phase:
*   **Batch Script Overhaul:** I rewrote the execution logic in `run_vcfs.bat`, ensuring paths dynamically sanitized backslashes into forward slashes before parsing via `javac`, ultimately resolving the escape sequence errors and ensuring the project compiled out of the box.
*   **Encapsulation Refactoring:** I spearheaded the refactoring of all domain models. I systematically converted public fields to private across classes like `AttendanceRecord`, and updated all dependent controllers and views to utilize the new accessor methods, effectively restoring data hiding and structural integrity.
*   **Observer Pattern & State Management:** I finalized the application bootstrap inside `App.java`. I instituted a strict cyclical dependency check between the `Logger`, `SystemTimer`, and `CareerFairSystem` models to ensure `SystemTimer` did not throw `StackOverflowError` exceptions upon initialization. I verified that listeners correctly updated when time skipped forward.
*   **System Logging Cleanup:** I wrote automated replacements for legacy `System.out.println` debug stubs across `AdminScreenController`, `CandidateController`, and central core routines, hooking them into my robust `Logger.java` utility to produce clean, timestamped runtime traces.

### Result (R)
The emergency intervention was highly successful. The project now successfully compiles without errors using a single terminal command. Cross-team integration issues were resolved as all components now route through the centralized `CareerFairSystem` facade and `SystemTimer`. The terminal output during execution was polished into a professional startup sequence devoid of confusing compiler errors, and the system securely manages application state adhering strictly to OOP principles.

### Learnings (L)
This project profoundly underscored the necessity of establishing strict architectural boundaries (like Encapsulation and standardized Logging) at the *start* of a project rather than retrofitting them at the end. I learned the critical value of cross-platform build scripts in collaborative environments to prevent local configuration disparities from blocking system integration. If I were to manage a similar project again, I would implement Continuous Integration pipelines earlier to catch these structural inconsistencies incrementally, rather than during the final assembly phase. I also gained invaluable practical exposure to diagnosing sophisticated runtime cyclical dependency bugs and coordinating complex Observer patterns in MVC architectures.
