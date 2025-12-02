# Project Agents

This file defines the primary user agents (personas) for the Color Teaching Aids application. The AI IDE should use these definitions to tailor its responses, generate code, and design UI components that are appropriate for each user type.

---

## 1. Teacher Agent

-   **Role**: Educator, Caregiver, Administrator
-   **Technical Skill**: Varies (from novice to proficient). Assume low to moderate technical skill.
-   **Primary Goal**: To manage student profiles, assign activities, and monitor progress in a simple, efficient, and stress-free manner.
-   **Core Needs**:
    -   Clear, uncluttered interfaces.
    -   Simple and intuitive navigation.
    -   Quick access to student lists and settings.
    -   Confidence that the student-facing side of the app is safe and effective.
    -   Ability to use the app offline in a classroom setting.

> **AI Implementation Notes**: Teacher-facing UI should be clean, professional, and focused on task completion. Avoid playful animations or sounds. Prioritize clarity and ease of use. Use standard UI patterns.

---

## 2. Student Agent

-   **Role**: Learner
-   **Abilities**: Represents users with a wide range of special needs, including:
    -   Autism Spectrum Disorder (ASD)
    -   Attention-Deficit/Hyperactivity Disorder (ADHD)
    -   Color Vision Deficiency (CVD)
    -   Sensory Processing Disorders
    -   Cognitive and developmental delays.
-   **Primary Goal**: To learn and identify colors through engaging, non-punitive, and sensory-friendly activities.
-   **Core Needs**:
    -   Predictable and consistent interactions.
    -   Minimal cognitive load (one task at a time).
    -   Positive and encouraging feedback (no negative reinforcement).
    -   Customizable sensory input (sounds, haptics, animations).
    -   High-contrast, unambiguous visuals.
    -   No time pressure or distracting elements.

> **AI Implementation Notes**: Student-facing UI must strictly adhere to the `Core UI UX Rules.md` and `Theme Color Rules.md`. Prioritize safety, predictability, and sensory comfort over novelty. All interactions must be simple, clear, and reversible where possible.
