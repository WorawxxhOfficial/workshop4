package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import services.LibraryService

@Serializable
data class CreateLendingRequest(val bookId: Int, val borrowerName: String)

@Serializable
data class UpdateLendingRequest(val bookId: Int, val borrowerName: String)

fun Route.lendingRoutes(libraryService: LibraryService) {
    route("/lending") {
        // GET /lending - Get all lending records
        get {
            val records = libraryService.getAllLendingRecords()
            call.respond(HttpStatusCode.OK, records)
        }
        
        // GET /lending/{id} - Get lending record by ID
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@get
            }
            
            val record = libraryService.getLendingRecordById(id)
            if (record == null) {
                call.respond(HttpStatusCode.NotFound, "Lending record not found")
            } else {
                call.respond(HttpStatusCode.OK, record)
            }
        }
        
        // GET /lending/book/{bookId} - Get lending records by book ID
        get("/book/{bookId}") {
            val bookId = call.parameters["bookId"]?.toIntOrNull()
            if (bookId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
                return@get
            }
            
            val records = libraryService.getLendingRecordsByBookId(bookId)
            call.respond(HttpStatusCode.OK, records)
        }
        
        // GET /lending/active - Get active lending records
        get("/active") {
            val records = libraryService.getActiveLendingRecords()
            call.respond(HttpStatusCode.OK, records)
        }
        
        // POST /lending - Create new lending record (borrow book)
        post {
            try {
                val request = call.receive<CreateLendingRequest>()
                val record = libraryService.addLendingRecord(request.bookId, request.borrowerName)
                if (record == null) {
                    call.respond(HttpStatusCode.BadRequest, "Book not found or not available")
                } else {
                    call.respond(HttpStatusCode.Created, record)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }
        
        // PUT /lending/{id} - Update lending record
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@put
            }
            
            try {
                val request = call.receive<UpdateLendingRequest>()
                val record = libraryService.updateLendingRecord(id, request.bookId, request.borrowerName)
                if (record == null) {
                    call.respond(HttpStatusCode.NotFound, "Lending record not found")
                } else {
                    call.respond(HttpStatusCode.OK, record)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }
        
        // DELETE /lending/{id} - Delete lending record
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@delete
            }
            
            val success = libraryService.deleteLendingRecord(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Lending record not found")
            }
        }
        
        // POST /lending/{id}/return - Return book
        post("/{id}/return") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@post
            }
            
            val success = libraryService.returnBook(id)
            if (success) {
                call.respond(HttpStatusCode.OK, "Book returned successfully")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Lending record not found or book already returned")
            }
        }
    }
} 