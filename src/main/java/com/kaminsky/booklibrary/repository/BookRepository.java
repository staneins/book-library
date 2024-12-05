package com.kaminsky.booklibrary.repository;

import com.kaminsky.booklibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
