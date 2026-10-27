package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {
    return when (choice) {
        "1" -> {
            addBook(service)
            true
        }
        "2" -> {
            registerPatron(repository)
            true
        }
        "3" -> {
            borrowBook(service)
            true
        }
        "4" -> {
            returnBook(service)
            true
        }
        "5" -> {
            search(service)
            true
        }
        "6" -> {
            listAllBooks(repository)
            true
        }
        "0" -> false
        else -> {
            println("Invalid option")
            true
        }
    }
}

fun addBook(service: LibraryService) {
    print("Enter ISBN: ")
    val isbn = readlnOrNull()?.trim().orEmpty()

    print("Enter title: ")
    val title = readlnOrNull()?.trim().orEmpty()

    print("Enter author: ")
    val author = readlnOrNull()?.trim().orEmpty()

    print("Enter publication year: ")
    val year = readlnOrNull()?.trim()?.toIntOrNull()

    if (isbn.isBlank() || title.isBlank() || author.isBlank() || year == null) {
        println("Invalid book information.")
        return
    }

    val added = service.addBook(Book(isbn, title, author, year))
    println(if (added) "Book added successfully." else "A book with that ISBN already exists.")
}

fun registerPatron(repository: LibraryRepository) {
    print("Enter patron ID: ")
    val id = readlnOrNull()?.trim().orEmpty()

    print("Enter patron name: ")
    val name = readlnOrNull()?.trim().orEmpty()

    if (id.isBlank() || name.isBlank()) {
        println("Invalid patron information.")
        return
    }

    val added = repository.addPatron(Patron(id, name))
    println(if (added) "Patron registered successfully." else "A patron with that ID already exists.")
}

fun borrowBook(service: LibraryService) {
    print("Enter patron ID: ")
    val patronId = readlnOrNull()?.trim().orEmpty()

    print("Enter book ISBN: ")
    val isbn = readlnOrNull()?.trim().orEmpty()

    when (service.borrowBook(patronId, isbn)) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully.")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Book is not available.")
        BorrowResult.LIMIT_REACHED -> println("Patron has reached the borrowing limit.")
    }
}

fun returnBook(service: LibraryService) {
    print("Enter patron ID: ")
    val patronId = readlnOrNull()?.trim().orEmpty()

    print("Enter book ISBN: ")
    val isbn = readlnOrNull()?.trim().orEmpty()

    val returned = service.returnBook(patronId, isbn)
    println(if (returned) "Book returned successfully." else "Unable to return book.")
}

fun search(service: LibraryService) {
    print("Enter title, author, or ISBN to search: ")
    val query = readlnOrNull()?.trim().orEmpty()

    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found.")
    } else {
        results.forEach { book ->
            println("${book.title} by ${book.author} | ISBN: ${book.isbn} | Year: ${book.year} | Available: ${book.isAvailable}")
        }
    }
}

fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()

    if (books.isEmpty()) {
        println("No books in the library.")
    } else {
        books.forEach { book ->
            println("${book.title} by ${book.author} | ISBN: ${book.isbn} | Year: ${book.year} | Available: ${book.isAvailable}")
        }
    }
}
