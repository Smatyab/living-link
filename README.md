# 🌐 Living Link

![Living Link Logo](https://img.shields.io/badge/Society-Management-blueviolet?style=for-the-badge)  
**A Next-Gen Society Management System**

---

## ✨ Overview

**Living Link** is a cutting-edge Society Management System designed to streamline and simplify the complexities of modern living communities. Built for both administrators and residents, it offers a seamless platform for managing housing societies, fostering community engagement, and enhancing the residential experience.

> **Vision:**  
> "Empowering housing societies with technology for efficient management, transparent operations, and a vibrant community life."

---

## 👩‍💻 Founders & Visionaries

- **Abhaykumar Ambaliya**
- **Parth Lathiya**

United by their passion for technology, these founders have set out to revolutionize residential living with a mission to make society operations more efficient, transparent, and user-friendly.

---

## 🚀 Key Features

### 🏠 Society Management

- **Society Directory:** Add and manage multiple societies, apartments, and buildings.
- **Members & Roles:** Member listing, role assignment (e.g., Secretary, Admin), and secure authentication.
- **Secretary Tools:** Payment requests, society details, and member tracking.

### 📅 Events & Meetings

- **Event Creation:** Schedule society events, attach banners, and manage notifications.
- **Meeting Scheduler:** Organize meetings (including Zoom integration), with topic, description, and auto reminders.
- **Notes:** Built-in notes system for collaborative information sharing.

### 💬 Complaints & Communication

- **Complaints Portal:** Residents can easily file, track, and resolve complaints.
- **Inbox:** Central communication hub for society announcements and updates.

### 💰 Finance & Maintenance

- **Maintenance Dashboard:** Track payments, generate requests, and toggle between paid/unpaid transactions.
- **Automated Records:** All payment and maintenance data stored securely using Firebase.

### 📢 Banners & Announcements

- Dynamic banners for society-wide announcements.
- Easy upload and scheduling for visual communication.

### 🔒 Security & Authentication

- Firebase-backed authentication and role-based access.
- Password reset and secure login mechanisms.

### 📈 Analytics & Dashboard

- Admin panel with stats: events, meetings, complaints, banners, and notes.
- Visual charts and calendars for actionable insights.

---

## 🛠️ Tech Stack

| Technology   | Usage                    |
|--------------|--------------------------|
| **Kotlin**   | Android client apps      |
| **HTML/CSS** | Web admin panels, design |
| **JavaScript**| Dynamic dashboard & logic|
| **Java**     | Android backend modules  |
| **Firebase** | Backend services, Auth, DB|
| **Bootstrap**| Responsive UI/UX         |

---

## 📲 Multi-Platform

- **Admin Web Panel:** Full-featured management via browser.
- **Android Apps:** Separate modules for secretaries/administration and residents.
- **Progressive Web App:** Accessible on any device.

---

## 📝 How to Install & Run

### Web Admin Panel

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Smatyab/living-link.git
   cd living-link/societyAdmin/societyManagment/admin/
   ```
2. **Configure Firebase:**
   - Edit `src/FireBaseConfig.js` with your Firebase credentials.
3. **Run Locally:**
   - Open `index.html` in your browser.
   - Or use a local server (recommended for module imports):
     ```bash
     python -m http.server 8080
     # Then navigate to http://localhost:8080
     ```
4. **Login:**  
   Use your society admin credentials.

### Android App

- Open the respective module (`societySec` for secretaries, `societyUser` for residents) in Android Studio.
- Make sure to update Firebase settings in the app.
- Build and run on Android device/emulator.

---

## 🔒 Terms & Privacy

- All data is handled securely and in accordance with our [Privacy Policy](https://lathiyaparth.github.io/policy/).
- By using Living Link, you agree to abide by the platform's terms and conditions.

---

## 📝 Contributing

We welcome contributions!  
If you have suggestions or want to add features, feel free to open an issue or submit a pull request.

---

## 📬 Contact & Support

- [Open Issues](https://github.com/Smatyab/living-link/issues)
- Email: [Contact the team](mailto:HoPhSeeofficial@gmail.com)

---

**Living Link** — Simplifying Society Living, One Link at a Time.