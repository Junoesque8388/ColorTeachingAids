# Accessibility Compliance Index  
*Mapping Rules to Universal Design & WCAG 2.1 AA*

> **Purpose**: This document validates that all design rules enforce **3+ Universal Design Principles** and **WCAG 2.1 AA** compliance. Use this to audit AI IDE output.

## Universal Design Principle Coverage
| Principle | Supported By | Example Rules |
|-----------|--------------|---------------|
| **#1 Equitable Use** | Core UI UX Rules §6, Universal Layer §1 | Non-stigmatizing imagery; sensory customization |
| **#2 Flexibility in Use** | Universal Layer §1, §2 | Sound/haptic toggles; switch control support |
| **#3 Simple & Intuitive** | Core UI UX Rules §3, §5 | No idioms; calm mode; predictable navigation |
| **#4 Perceptible Info** | Core UI UX Rules §1, Theme Color §4 | Color + icon + text; contrast ratios |
| **#5 Tolerance for Error** | Core UI UX Rules §7, Universal Layer §3 | Undo actions; non-punitive feedback |
| **#6 Low Physical Effort** | Core UI UX Rules §2, Universal Layer §2 | 48dp touch targets; single-tap actions |
| **#7 Size/Space for Approach** | Core UI UX Rules §2 | 8dp spacing between targets |

## WCAG 2.1 AA Compliance
| Success Criterion | Implemented In | Validation Method |
|-------------------|----------------|------------------|
| **1.4.1 Use of Color** | Theme Color §4, Core UI UX §1 | AI IDE: Check no color-only indicators |
| **1.4.3 Contrast (AA)** | Theme Color §1, Core UI UX §1 | AI IDE: Verify ≥4.5:1 for text (use [axe-core](https://www.deque.com/axe/)) |
| **2.1.1 Keyboard Access** | Universal Layer §2 | AI IDE: Ensure all actions reachable via switch control |
| **2.2.1 Timing Adjustable** | Universal Layer §3 | AI IDE: Confirm no time limits in activities |
| **2.5.5 Target Size** | Core UI UX §2, Universal Layer §2 | AI IDE: Validate ≥48×48 dp touch targets |

## AI IDE Enforcement Protocol
Before finalizing any screen:
1. Run `axe-core` scan for WCAG violations
2. Check against `Universal Interaction Layer` checklist
3. Verify color usage against `Theme Color Rules` tokens
4. Confirm language simplicity via `Core UI UX Rules` §3

> **Note**: Violation of any mapped criterion = **non-compliant with MVP Must-Haves**.