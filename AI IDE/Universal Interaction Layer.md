# Universal Interaction Layer – AI IDE Implementation Rules

> **Purpose**: This document defines non-negotiable, cross-cutting interaction patterns that **MUST** be applied to **every screen and component** in the app—both teacher-facing and student-facing. These rules ensure motor accessibility, sensory safety, cognitive clarity, and offline resilience.

> **Instruction for AI IDE**:  
> Before generating any UI component, form, button, or activity logic, **validate against these rules**. If a generated element violates any rule, **reject and regenerate**.

---

## 1. Sensory Customization (Per-Student)
- **All student-facing activities** must include a **⚙️ Settings button** in the top-right corner.
- Tapping it opens a modal with:
  - Toggle: **Sound Effects** (default: On)
  - Toggle: **Haptic Feedback** (default: On)
  - Slider: **Animation Speed** (labels: Slow / Medium / Fast; default: Medium)
- These settings **override** the student’s profile defaults for the current session.
- Settings **must persist locally** (no cloud dependency).

## 2. Motor & Input Accessibility
- **All interactive elements** (buttons, draggable items, cards) **MUST** be:
  - **Minimum 48×48 dp** in size
  - **Spaced ≥8 dp apart** from other touch targets
- **No gesture-only actions**: Every critical function must be reachable via **single tap**.
- **Full Switch Control support**: All screens must be navigable via sequential focus (no dead ends).
- Full Switch Control support → **WCAG 2.1 Success Criterion 2.1.1 (Keyboard Access)**  

## 3. Cognitive & Emotional Safety
- **Never use red for errors**. Incorrect actions must trigger:
  - **Visual**: Gentle object return (no “X”)
  - **Audio**: Neutral “boop” (not harsh)
  - **Haptic**: None
- **No time limits, countdowns, or auto-advancing screens**.
- **All navigation must be predictable**: Back button always in top-left; no hidden menus.

## 4. Perception & Readability
- **Text contrast**: All text must meet **WCAG AA** (≥4.5:1 for standard text).
- **No color-only indicators**: Status must use **icon + text + shape**.
- **Real photos only** in “Color in Daily Life” (no cartoons or abstract icons).
- Text contrast ≥4.5:1 → **WCAG 2.1 Success Criterion 1.4.3 (Contrast Minimum)**  
- No color-only indicators → **WCAG 2.1 Success Criterion 1.4.1 (Use of Color)**  

## 5. Contextual Resilience
- **Offline-first**: All MVP Must-Have features work without internet.
- **Shared-device safe**: App **must not store persistent login state**. Always return to Launch Screen on background/close.
- **No external analytics or tracking** during student activities.

---

## Enforcement Checklist for AI IDE
Before finalizing any screen, confirm:
- [ ] Touch targets ≥48 dp
- [ ] ⚙️ Settings panel present in all activities
- [ ] No red used for feedback
- [ ] Works offline
- [ ] Passes WCAG contrast check
- [ ] Navigable via Switch Control (logical focus order)

> **Note**: Violation of any rule = **non-compliant with MVP Must-Haves**.