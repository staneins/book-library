package com.kaminsky.booklibrary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaminsky.booklibrary.entity.Book;
import com.kaminsky.booklibrary.exceptions.JsonException;
import com.kaminsky.booklibrary.repository.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class LibraryController {
    private final BookRepository bookRepository;
    private final ObjectMapper mapper;

    public LibraryController(BookRepository bookRepository, ObjectMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    @GetMapping()
    public String getAllBooks(Pageable pageable) {
        try {
            return mapper.writeValueAsString(bookRepository.findAll(pageable));
        } catch (JsonProcessingException e) {
            throw new JsonException();
        }
    }

    @GetMapping("{id}")
    public String getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            try {
                return mapper.writeValueAsString(book.get());
            } catch (JsonProcessingException e) {
                throw new JsonException();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена");
    }


    @PostMapping
    public void addBook(@RequestBody String bookJson) {
        try {
            bookRepository.save(mapper.readValue(bookJson, Book.class));
        } catch (JsonProcessingException e) {
            throw new JsonException();
        }
    }

    @PutMapping("{id}")
    public void updateBook(@RequestBody String bookJson, @PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Книга не найдена");
        }
        Book book;
        try {
            book = mapper.readValue(bookJson, Book.class);
            book.setId(id);
            bookRepository.save(book);
        } catch (JsonProcessingException e) {
            throw new JsonException();
        }
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
