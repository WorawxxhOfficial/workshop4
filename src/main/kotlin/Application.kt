import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import repositories.BookRepository
import repositories.LendingRecordRepository
import routes.bookRoutes
import routes.lendingRoutes
import services.LibraryService

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    
    // Initialize repositories and services
    val bookRepository = BookRepository()
    val lendingRecordRepository = LendingRecordRepository()
    val libraryService = LibraryService(bookRepository, lendingRecordRepository)
    
    routing {
        bookRoutes(libraryService)
        lendingRoutes(libraryService)
        
        // Health check endpoint
        get("/health") {
            call.respondText("OK")
        }
    }
} 