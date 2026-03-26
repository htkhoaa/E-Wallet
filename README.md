# 🚀 Core E-Wallet Engine | Financial-Grade Backend

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Security-black?style=for-the-badge&logo=JSON%20web%20tokens)

## 📖 Giới thiệu dự án (Overview)

**Core E-Wallet Engine** là một hệ thống lõi ví điện tử (Monolithic Architecture) được thiết kế tập trung vào việc giải quyết các bài toán khắt khe nhất của hệ thống tài chính: **Tính toàn vẹn dữ liệu (Data Integrity)**, **Xử lý đồng thời (Concurrency Control)** và **Truy xuất nguồn gốc dòng tiền (Auditability)**.

Dự án không chỉ dừng lại ở các thao tác CRUD cơ bản mà đi sâu vào việc mô phỏng một môi trường tài chính khép kín (Closed-loop wallet system), đảm bảo không bao giờ xảy ra tình trạng thất thoát hay sai lệch dữ liệu.

## ✨ Tính năng cốt lõi (Core Features)

Hệ thống được thiết kế với tư duy phòng thủ (Defensive Programming) và áp dụng các tiêu chuẩn Fintech:

* **Double-Entry Ledger System (Hệ thống Sổ cái kép):** Mọi giao dịch tài chính (Nạp, Rút, Chuyển tiền) đều sinh ra các bút toán Ghi Nợ (Debit) và Ghi Có (Credit) đối ứng giữa Ví Người dùng và Ví Hệ thống (System Pool). Đảm bảo tổng tài sản luôn bằng 0 (Zero-sum game).
* **Concurrency Control & Deadlock Prevention:** Sử dụng **Pessimistic Locking** (`SELECT ... FOR UPDATE`) để ngăn chặn Race Condition khi xử lý hàng ngàn giao dịch đồng thời. Áp dụng thuật toán sắp xếp khóa (Lock Ordering) để triệt tiêu hoàn toàn rủi ro Deadlock.
* **Idempotent API Design:** Cơ chế chống trùng lặp giao dịch (Idempotency Key) bảo vệ hệ thống khỏi các request bị gửi lặp do lỗi mạng (Double-spending prevention).
* **Robust Security:** Xác thực người dùng bằng JWT Token (mang theo claims tùy chỉnh cho KYC) và mã hóa mật khẩu bằng BCrypt.
* **Centralized Error Handling:** Hệ thống mã lỗi (Error Codes) và chuẩn hóa API Response chuẩn mực, sẵn sàng tích hợp với các hệ thống Mobile/Web Clients.

## 🛠 Kiến trúc Database (Database Architecture)

Cơ sở dữ liệu MySQL (InnoDB) được thiết kế với các ràng buộc khắt khe:

1.  `users`: Quản lý định danh (Phone Number, BCrypt Password) và trạng thái KYC.
2.  `wallets`: Két sắt người dùng. Sử dụng kiểu dữ liệu `DECIMAL(19,4)` và ràng buộc `CHECK (balance >= 0)` ở cấp độ Database.
3.  `transactions`: Ghi nhận "ý định" giao dịch và theo dõi State Machine (SUCCESS, FAILED).
4.  `ledger_entries`: Bảng Sổ cái lưu vết mọi dòng tiền ra/vào và số dư sau giao dịch (`post_balance`) phục vụ công tác đối soát (Reconciliation).

## 🚀 Cài đặt & Vận hành (Getting Started)

### Yêu cầu hệ thống (Prerequisites)
* JDK 17 trở lên
* Maven 3.8+
* MySQL 8.0

### Các bước cài đặt
1.  **Clone repository:**
    ```bash
    git clone [https://github.com/your-username/core-ewallet-engine.git](https://github.com/your-username/core-ewallet-engine.git)
    cd core-ewallet-engine
    ```

2.  **Cấu hình Database:**
    Tạo một database trong MySQL có tên `ewallet_db`. Mở file `src/main/resources/application.yml` và cấu hình thông tin kết nối:
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/ewallet_db
        username: root
        password: your_password
    ```

3.  **Khởi chạy ứng dụng:**
    ```bash
    mvn spring-boot:run
    ```
    *Hệ thống sẽ chạy tại cổng mặc định: `http://localhost:8080`*

## 📚 API Endpoints (Tóm tắt)

| Feature | HTTP Method | Endpoint | Authorization |
| :--- | :--- | :--- | :--- |
| **Đăng ký** | POST | `/api/v1/auth/register` | No |
| **Đăng nhập** | POST | `/api/v1/auth/login` | No |
| **Nạp tiền** | POST | `/api/v1/transactions/deposit` | Bearer Token |
| **Rút tiền** | POST | `/api/v1/transactions/withdraw` | Bearer Token |
| **Chuyển tiền** | POST | `/api/v1/transactions/transfer` | Bearer Token |

*(Bạn có thể import file Postman Collection đính kèm trong thư mục `/docs` để test toàn bộ luồng API).*

## 👨‍💻 Tác giả (Author)

**MIUKY**
* **Vai trò:** Backend Developer
* **Định hướng:** Xây dựng các hệ thống tài chính/Fintech chịu tải cao và an toàn dữ liệu.
* **Liên hệ:** [Link LinkedIn/Email của bạn]
