# Secure Notes Microservices Project

This project consists of two separate Spring Boot microservices:

- **UserService** – handles user registration and note submission.
- **NoteService** – securely stores user notes in decrypted form.

The system uses **AES** (symmetric encryption) for encrypting note content and **RSA** (asymmetric encryption) for securely exchanging the AES key between services.

---

## 🔐 Encryption Design

### 🔁 Data Flow

1. **UserService**
    - Registers users.
    - Accepts plaintext note from user.
    - Generates a random AES key.
    - Encrypts note content using AES.
    - Encrypts the AES key using NoteService’s RSA public key.
    - Sends the encrypted note content + encrypted AES key to NoteService via REST API.

2. **NoteService**
    - Loads RSA private key on startup (from `note_private.key`).
    - Accepts AES-encrypted note and RSA-encrypted AES key.
    - Decrypts AES key using RSA private key.
    - Decrypts the note content using AES key.
    - Stores **decrypted (plaintext)** note content in the database.


