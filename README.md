## Administrator Module

### System Role

This module manages the **Preparing State** of the application. It is responsible for initializing system data, configuring the event timeline, and monitoring system activity via audit logging.


## Architecture Overview

The module follows an **MVC + Observer Pattern design**:

```
        +------------------------+
        |  AdministratorScreen   |
        |        (View)          |
        +-----------+------------+
                    |
                    v
        +------------------------+
        |    AdminController     |
        |      (Controller)      |
        +-----------+------------+
                    |
                    v
        +------------------------+
        |     CentralModel       |
        |        (Model)         |
        +-----------+------------+
                    |
        notifyObservers()
                    |
                    v
        +------------------------+
        |  AdministratorScreen   |
        |     (Audit Log)        |
        +------------------------+
```


## Components

### 1. AdministratorScreen (View)

* Built using Java Swing
* Provides UI for:

  * Organization creation
  * Booth creation
  * Recruiter assignment
  * Timeline configuration
* Displays a live **Audit Log**


### 2. AdminController (Controller)

* Handles all user actions from the UI
* Communicates with the Model
* Responsible for:

  * Creating entities
  * Assigning relationships
  * Passing timeline data


### 3. CentralModel (Model)

* Stores all system data:

  * Organizations
  * Booths
  * Recruiters
* Maintains:

  * System timeline
  * Runtime data (reservations, sessions, notifications)


### 4. Observer Pattern (Audit Logging)

* The Model acts as **Observable**
* AdministratorScreen acts as **Observer**
* Any module can log events via:

```java
notifyObservers("Event description");
```


## System Timeline

The following timestamps must be defined:

* `BookingsOpenTime`
* `BookingsCloseTime`
* `StartTime`
* `EndTime`

Format:

```
yyyy-MM-ddTHH:mm
```

These values are used by the **SystemTimer module** to control system state transitions.


## Data Lifecycle Management

### Purge Operation

Triggered when transitioning:

```
Dormant → Preparing
```

Method:

```java
model.purgeData();
```

Clears:

* Reservations
* Meeting Sessions
* Notifications


## Integration Guidelines

* Use `CentralModel` as the single data source
* Avoid redundant data structures
* All system events should be logged using:

```java
notifyObservers(...)
```


---

## Developer Notes

Hey team

This is the **Admin side of the system**, which basically sets everything up before the fair starts.

### What this part does

* This is the **first screen of the app**
* Lets the admin:

  * Create Organizations
  * Create Booths under those orgs
  * Assign Recruiters to booths
  * Set the system timeline
* Also shows a **live Audit Log** of everything happening in the system


### How to work with my part

#### Model usage

* Please use the **CentralModel** as the main data source
* Don’t create duplicate lists/data in your own classes


#### Logging events (IMPORTANT)

If your feature does anything like:

* booking a slot
* cancelling a meeting
* sending notifications

Just call:

```java
model.notifyObservers("Your message here");
```

That will automatically show up in the Admin Audit Log


#### Timeline format

When working with time, use:

```
yyyy-MM-ddTHH:mm
```

Example:

```
2026-04-05T14:00
```


#### Data Reset

* When system leaves **Dormant → Preparing**
* Call:

```java
model.purgeData();
```

This clears:

* reservations
* sessions
* notifications


### Structure (simple)

* `AdministratorScreen` → UI
* `AdminController` → handles actions
* `CentralModel` → shared system data
* `Observer` → audit log system


### TLDR

* I handle setup + logging
* You guys plug into the model
* Use `notifyObservers()` for logging anything important

Let me know if you need help connecting your part
