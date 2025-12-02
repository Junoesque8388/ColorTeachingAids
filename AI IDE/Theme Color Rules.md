# Theme Color Rules for Inclusive Special Needs Education App

These rules define the permissible and recommended color usage for the app’s UI theme in both **Light Mode** and **Dark Mode**, grounded in accessibility, cognitive load reduction, and sensory sensitivity principles. All UI components must adhere to this scheme to ensure inclusivity for children with diverse needs, including autism, ADHD, visual impairments, and color vision deficiency (CVD).

---

## 1. Base Palette: High-Contrast Neutrals

| Element | Light Mode | Dark Mode |
|--------|-----------|----------|
| Primary Background | `#FFFFFF` | `#121212` |
| Surface / Card Background | `#FFFFFF` | `#1E1E1E` |
| Primary Text | `#1A1A1A` | `#FFFFFF` |
| Secondary Text (labels, hints) | `#5F6368` | `#B0B0B0` |

> **Why?**  
> High luminance contrast between text and background maximizes readability for users with visual processing differences or low vision. Pure white and near-black (`#121212`, not `#000000`) reduce glare and halation. Gray backgrounds are avoided as they lower contrast and increase cognitive effort (WCAG Principle: **Perceivable**).

---

## 2. Accent Colors: Limited, Purposeful, and Calming

| Purpose | Light Mode | Dark Mode |
|--------|-----------|----------|
| Primary Accent (actions, success) | `#2E8B57` (SeaGreen) | `#4CAF50` (Soft Green) |
| Secondary Accent (navigation, secondary actions) | `#4A90E2` (Soft Blue) | `#64B5F6` (Muted Blue) |
| Caution / Attention (minimal use) | `#FFA500` (Orange) | `#FFB74D` (Warm Amber) |

> **Why?**  
> Red is **prohibited** in the UI palette to avoid triggering anxiety or feelings of failure—common in neurodivergent learners. Green and blue are calming, highly distinguishable, and maintain strong contrast. Orange/amber provides gentle, emotionally neutral attention cues. All accents are desaturated to prevent visual vibration or overstimulation.

---

## 3. Learning Content Colors ≠ UI Theme Colors

- **Educational content** (e.g., red apple, blue ball) may use **full-spectrum, saturated hues** as needed for pedagogy.
- **UI chrome** (buttons, menus, feedback icons) **must never use** the same colors being taught (e.g., if teaching “red,” do not use red for any UI element).

> **Why?**  
> Prevents **conceptual interference**. A child should associate “red” with the learning object—not with a button or system message. This supports **Understandable & Predictable** design and reduces cognitive confusion.

### Learning Content Color Palette (Student-Facing Only)

The following saturated colors may be used **exclusively in educational activities** (e.g., red apple, blue ball). They **MUST NEVER appear in UI chrome** (buttons, navigation, feedback icons).

| Color  | HEX     | Example Objects                     |
|--------|---------|-------------------------------------|
| Red    | `#E53935` | Apple, Fire Truck, Strawberry       |
| Blue   | `#1E88E5` | Sky, Water, Ball, Jeans             |
| Yellow | `#FFD600` | Sun, Banana, Lemon, Rubber Duck     |
| Green  | `#43A047` | Leaf, Frog, Cucumber, Traffic Light |
| Orange | `#FB8C00` | Orange, Carrot, Traffic Cone        |
| Purple | `#8E24AA` | Grape, Eggplant, Balloon            |

> **Warning to AI IDE**: These HEX values are **for learning content only**. Using them in UI components (e.g., a red "Delete" button) violates Rule #3 and causes conceptual interference.
---

## 4. No Color-Only Indicators

- Never use color alone to indicate:
  - Correct/incorrect responses
  - Active/inactive states
  - Progress or status
- Always pair with:
  - Icons (✓, ✗, ▶)
  - Text labels (“Great job!”, “Try again”)
  - Shape, position, or motion

> **Why?**  
> ~8% of males and ~0.5% of females have color vision deficiency (CVD). Relying on color excludes users and violates **WCAG 1.4.1: Use of Color**. Multi-channel signaling ensures universal comprehension.

---

## 5. System-Responsive Theme Support

- The app **automatically respects** the device’s system theme (Light or Dark).
- All color pairings in both modes meet **WCAG 2.1 AA** contrast requirements:
  - Text: ≥ 4.5:1 (standard), ≥ 3:1 (large)
  - UI components: ≥ 3:1 against adjacent colors
- No manual theme toggle is required or recommended.

> **Why?**  
> Some users (e.g., those with photophobia, migraines, or sensory sensitivities) rely on dark mode for comfort. Automatic adaptation enhances **Robust & Adaptable** design and reduces cognitive friction.

---

## 6. Prohibited Colors & Combinations

- ❌ **Red** (`#FF0000`, `#D32F2F`, etc.) in any UI context  
- ❌ **Yellow**, **light green**, or **pastels** on white backgrounds (low contrast)  
- ❌ **Complementary pairs** (e.g., red/green, blue/orange) in adjacent UI elements  
- ❌ **Pure black** (`#000000`) backgrounds or **pure white** (`#FFFFFF`) text in dark mode  
- ❌ **Gradients**, **textures**, or **busy patterns** in learning or navigation areas  

> **Why?**  
> These choices increase visual stress, reduce legibility, or risk triggering discomfort, anxiety, or photosensitive responses. Adherence supports **Sensory Safety** and **Seizure Prevention** (WCAG 2.2.1).

---

> **Design Principles Anchoring These Rules**:
> - **WCAG 2.1/2.2**: Contrast, Use of Color, Seizures and Physical Reactions  
> - **Universal Design Principle #4**: *Perceptible Information*  
> - **Cognitive Load Theory**: Minimize extraneous visual elements  
> - **Sensory Processing Sensitivity**: Avoid overstimulation and emotional triggers  
> - **Evidence-Based Practice**: Research on color perception in autism, ADHD, CVI, and CVD  

> **Note to AI IDE**:  
> All colors must be defined as **design tokens** (e.g., `--color-primary`, `--color-background`). UI components must reference these tokens—never hardcode HEX values. Validate every screen against automated accessibility checks for contrast, color dependency, and semantic clarity before approval.