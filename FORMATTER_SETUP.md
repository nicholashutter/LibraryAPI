# VS Code Java Formatter Setup Guide

## What's Been Configured

I've set up automatic code formatting for your LibraryAPI project with the following files:

### 1. `.editorconfig`
- Defines consistent formatting rules across the entire project
- Specifies UTF-8 encoding, 4-space indentation, line endings

### 2. `.vscode/settings.json`
- Workspace-specific VS Code settings
- Enables formatting on save
- Configures Java formatter to use Red Hat Language Support for Java
- Auto-organizes imports on save

### 3. `.vscode/java-formatter.xml`
- Custom Eclipse formatter configuration
- Configures Allman-style braces (opening brace on new line)
- Sets proper spacing and blank line rules

## Required VS Code Extensions

Install these extensions for full functionality:

1. **Extension Pack for Java** (Microsoft)
   - ID: `vscjava.vscode-java-pack`
   - Includes Language Support for Java, Debugger, etc.

2. **Language Support for Java (Red Hat)**
   - ID: `redhat.java`
   - Main formatter/linter

3. **Prettier - Code formatter** (Optional, for JSON/XML)
   - ID: `esbenp.prettier-vscode`

4. **XML** (Red Hat)
   - ID: `redhat.vscode-xml`

## Installation Steps

1. Open VS Code
2. Go to Extensions (`Ctrl+Shift+X` / `Cmd+Shift+X`)
3. Install the extensions listed above
4. Reload VS Code
5. Open a `.java` file and save it - formatting should apply automatically

## What Gets Automated

**Automatically formatted on save:**
- Allman-style braces (opening brace on new line)
- 4-space indentation
- Import organization (grouped by package)
- Trailing whitespace removal
- Final newline insertion
- Proper spacing around operators

**Manual adjustments needed:**
- Blank lines between every statement (most formatters don't support this)
- Method chaining formatting (may need occasional manual tweaks)
- Function parameter spacing (may need occasional manual tweaks)

## How to Use

Simply save a file (`Ctrl+S` / `Cmd+S`) and the formatter will automatically apply:
- Brace positioning
- Import organization
- Basic whitespace rules

