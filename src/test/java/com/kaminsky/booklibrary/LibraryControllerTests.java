package com.kaminsky.booklibrary;

import com.kaminsky.booklibrary.controller.LibraryController;
import com.kaminsky.booklibrary.entity.Book;
import com.kaminsky.booklibrary.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryController.class)
public class LibraryControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("The Great Gatsby");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Great Gatsby II");

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        Page<Book> page = new PageImpl<>(books, PageRequest.of(0, 1), 2);

        when(bookRepository.findAll(PageRequest.of(0, 1))).thenReturn(page);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        when(bookRepository.existsById(1L)).thenReturn(true);
    }


    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("The Great Gatsby"));
    }




    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("The Great Gatsby"));

    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/1"))
                .andExpect(status().isOk());

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testPost() throws Exception {
        String newBookJson = """
                {
                    "title": "The Great Gatsby"
                }
                """;

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newBookJson))
                .andExpect(status().isOk());


        verify(bookRepository, times(1)).save(argThat(book ->
                book.getTitle().equals("The Great Gatsby")));
    }
}
