# Book Lending Library API

Server จะรันที่ `http://localhost:8080`

## API Endpoints

### Books

- `GET /books` - ดึงรายการหนังสือทั้งหมด
- `GET /books/{id}` - ดึงหนังสือตาม ID
- `POST /books` - เพิ่มหนังสือใหม่
- `PUT /books/{id}` - แก้ไขข้อมูลหนังสือ
- `DELETE /books/{id}` - ลบหนังสือ

### Lending Records

- `GET /lending` - ดึงรายการการยืมทั้งหมด
- `GET /lending/{id}` - ดึงรายการการยืมตาม ID
- `GET /lending/book/{bookId}` - ดึงรายการการยืมตาม ID หนังสือ
- `GET /lending/active` - ดึงรายการการยืมที่ยังไม่ได้คืน
- `POST /lending` - ยืมหนังสือ (สร้างรายการการยืม)
- `PUT /lending/{id}` - แก้ไขรายการการยืม
- `DELETE /lending/{id}` - ลบรายการการยืม
- `POST /lending/{id}/return` - คืนหนังสือ




```
src/
├── main/kotlin/
│   ├── models/
│   │   ├── Book.kt
│   │   └── LendingRecord.kt
│   ├── repositories/
│   │   ├── BookRepository.kt
│   │   └── LendingRecordRepository.kt
│   ├── services/
│   │   └── LibraryService.kt
│   ├── routes/
│   │   ├── BookRoutes.kt
│   │   └── LendingRoutes.kt
│   └── Application.kt
└── test/kotlin/
    ├── BookRepositoryTest.kt
    ├── LendingRecordRepositoryTest.kt
    └── LibraryServiceTest.kt
``` 

---



