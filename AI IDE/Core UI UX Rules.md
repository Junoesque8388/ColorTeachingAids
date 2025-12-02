# Core UI/UX Rules for Inclusive Special Needs Education App

These rules must be strictly followed by the AI-assisted IDE when generating UI components, layouts, interactions, and visual designs for the mobile app.

---
## Alignment with Universal Design Principles

These rules are explicitly designed to fulfill the 7 Principles of Universal Design:

- **Principle #1: Equitable Use**  
  → Rules §6 (Inclusive Representation) ensure the app is useful and marketable to people with diverse abilities.
  
- **Principle #2: Flexibility in Use**  
  → Rules §4 (Robust & Adaptable) and §5 (Sensory & Cognitive Load Management) provide choice in methods of use and adaptability.
  
- **Principle #3: Simple and Intuitive Use**  
  → Rules §3 (Understandable & Predictable) eliminate unnecessary complexity and support varied literacy levels.
  
- **Principle #4: Perceptible Information**  
  → Rules §1 (Perceivable Information) communicate necessary information effectively regardless of ambient conditions or the user’s sensory abilities.
  
- **Principle #5: Tolerance for Error**  
  → Rules §7 (Error Tolerance & Forgiveness) minimize hazards and adverse consequences of accidental actions.
  
- **Principle #6: Low Physical Effort**  
  → Rules §2 (Operable Interface) support efficient, comfortable interaction with minimal fatigue.
  
- **Principle #7: Size and Space for Approach and Use**  
  → Rules §2 (48×48 dp touch targets, 8 dp spacing) provide appropriate size and space for approach and use regardless of user’s body size, posture, or mobility.


## 1. Perceivable Information
- **Never rely on color alone** to convey meaning, status, or instructions. Always combine color with:
  - Text labels
  - Icons
  - Shapes or patterns
  - Positional cues
- **Ensure sufficient contrast**:
  - Minimum **4.5:1** contrast ratio for standard text (WCAG AA)
  - Minimum **3:1** for large text (≥18pt or ≥14pt bold)
  - Target **7:1** for enhanced readability (WCAG AAA)
- Provide **text alternatives** (alt text) for all meaningful non-text content (e.g., instructional images, icons with semantic meaning).

## 2. Operable Interface
- All interactive elements (buttons, draggable items, toggles) must have a **minimum touch target size of 48×48 dp**.
- Maintain **at least 8 dp spacing** between adjacent touch targets to prevent accidental activation.
- Support **alternative input methods**, including:
  - Touch
  - Switch control (via Bluetooth or on-screen scanning)
  - System-level accessibility gestures
- Ensure all functionality is **reachable without precise gestures** (e.g., avoid pinch-to-zoom as the only way to view content).

## 3. Understandable & Predictable
- Use **consistent navigation patterns** across all screens (e.g., persistent bottom tab bar or top navigation).
- **Never auto-advance** to the next screen or activity—always require explicit user or teacher action.
- Use **simple, concrete language**:
  - Short sentences (≤8 words)
  - Active voice
  - Avoid idioms, metaphors, or abstract terms (e.g., use “Match the red block” instead of “Find the ruby treasure”)
- Provide **clear feedback** for every user action (e.g., visual highlight + sound on correct match).

## 4. Robust & Adaptable
- Honor **system accessibility settings**:
  - Dynamic Type (respect user-selected font sizes)
  - Bold text
  - Reduce motion
  - High contrast mode
- Ensure full compatibility with:
  - **Screen readers** (VoiceOver on iOS, TalkBack on Android)
  - **Switch access** and **eye-tracking** systems (via standard accessibility APIs)
- Allow **user customization** of sensory inputs (e.g., toggle sound, reduce animation intensity).

## 5. Sensory & Cognitive Load Management
- Provide a **“Calm Mode”** that disables:
  - Background music
  - Sound effects
  - Decorative animations
  - Complex or busy backgrounds
- Use **minimalist visual design**:
  - Solid, neutral backgrounds (white, light gray, black)
  - Ample white space
  - No flashing, blinking, or rapidly moving elements (comply with **WCAG 2.2: Seizure Safety**)
- Break tasks into **discrete, single-step actions** with immediate, clear feedback.

## 6. Inclusive Representation
- Use **diverse, realistic imagery** in learning content:
  - Varied skin tones
  - Children with visible disabilities (e.g., wheelchairs, hearing aids)
  - Culturally neutral or globally inclusive contexts
- Avoid **exaggerated cartoon styles** that may confuse literal thinkers (e.g., some autistic learners).

## 7. Error Tolerance & Forgiveness
- Make all actions **reversible** where possible (e.g., “Undo” after drag-and-drop).
- Use **non-punitive, supportive feedback**:
  - Correct: gentle chime + green glow
  - Incorrect: neutral tone + subtle shake (no red “X” or harsh sounds)
- Prevent critical errors through **confirmation dialogs** only for irreversible actions (e.g., deleting a student profile).

---

> **Note to AI IDE**: These rules override default UI generation templates. Prioritize accessibility, predictability, and sensory safety over aesthetic novelty. All generated screens must pass automated accessibility checks (e.g., contrast, touch target size, screen reader compatibility) before approval.