# AltisCore Bootstrap 🚧
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Status](https://img.shields.io/badge/Status-In_Development-yellow.svg)]()
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)]()

A bootstrap application for managing the AltisCore Launcher environment. This project is currently under active development and needs improvements.

## 🚧 Project Status

This is a work in progress! The bootstrap currently handles basic runtime management and launcher updates, but there's still a lot to improve. Feel free to contribute and help make it better.

### What Works
- Basic Java runtime detection and installation
- Simple launcher update mechanism
- Progress display for downloads
- Cross-platform support (needs testing)

### What Needs Work
- [ ] Progress bar animation during Java download needs fixing
- [ ] Error handling could be more robust
- [ ] Code organization needs improvement
- [ ] Logging system could be better
- [ ] Testing coverage is minimal
- [ ] Documentation is incomplete

## 🛠️ Current Features

### Runtime Management
- Detects Java installation (could be more robust)
- Downloads Java 21 with JavaFX when needed
- Basic validation of Java environment

### Update System
- Simple manifest-based updates
- Basic download progress tracking
- Launcher version management (needs improvement)

## 🔧 Building & Development

### Prerequisites
- JDK 21
- Gradle 8.5+
- Some patience with the current state of the code 😅

### Quick Start
```bash
git clone https://github.com/Arinonia/altiscore-bootstrap.git
cd altiscore-bootstrap
gradle build
```

### Running in Development
```bash
gradle run
```

## 🐛 Known Issues

- Progress bar during Java download doesn't update correctly
- Error handling is basic and needs improvement
- Some hard-coded values need to be moved to configuration
- UI could be responsive or use layout at least
- Logging is inconsistent

## 💡 Future Ideas

Things that would be nice to have:
- Better progress visualization
- More detailed error reporting
- Configuration file support
- Proper logging system
- Unit test coverage
- CI/CD pipeline

## 📞 Questions?

Having trouble with the code? Want to contribute but don't know where to start?
- Discord: [Join our community](https://discord.gg/Xut47pGAXC)
- Email: arinonia.dev@gmail.com

Don't hesitate to ask questions or suggest improvements!

## 📝 License
MIT License - Feel free to use, modify, and distribute as needed

---
🚧 This is a work in progress 🚧