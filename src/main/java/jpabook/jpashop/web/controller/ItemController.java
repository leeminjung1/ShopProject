package jpabook.jpashop.web.controller;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.web.form.AlbumForm;
import jpabook.jpashop.web.form.BookForm;
import jpabook.jpashop.web.form.ItemForm;
import jpabook.jpashop.web.form.MovieForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createItem() {
        return "items/createItemForm";
    }

    @GetMapping("/book/new")
    public String createBookForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createBookForm";
    }

    @PostMapping("/book/new")
    public String createBook(BookForm form) {

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setPublisher(form.getPublisher());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/movie/new")
    public String createMovieForm(Model model) {
        model.addAttribute("form", new MovieForm());
        return "items/createMovieForm";
    }

    @PostMapping("/movie/new")
    public String createMovie(MovieForm form) {
        Movie movie = new Movie();
        movie.setName(form.getName());
        movie.setPrice(form.getPrice());
        movie.setStockQuantity(form.getStockQuantity());
        movie.setDirector(form.getDirector());
        movie.setActor(form.getActor());
        itemService.saveItem(movie);
        return "redirect:/items";
    }

    @GetMapping("/album/new")
    public String createAlbumForm(Model model) {
        model.addAttribute("form", new AlbumForm());
        return "items/createAlbumForm";
    }

    @PostMapping("/album/new")
    public String createAlbum(AlbumForm form) {
        Album album = new Album();
        album.setName(form.getName());
        album.setPrice(form.getPrice());
        album.setStockQuantity(form.getStockQuantity());
        album.setArtist(form.getArtist());
        album.setPublisher(form.getPublisher());
        itemService.saveItem(album);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        List<ItemForm> forms = new ArrayList<>();
        for (Item item : items) {
            ItemForm itemForm = new ItemForm(item.getId(), item.getName(), item.getPrice(), item.getStockQuantity());
            itemForm.setDType(item.getDiscriminatorValue());
            forms.add(itemForm);
        }

        model.addAttribute("items", forms);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);
        model.addAttribute("form", item);
        return "items/updateItemForm";
    }

//    @PostMapping("/items/{itemId}/edit")
/*    public String updateItem(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }*/

    /**
     * 상품 수정, 권장 코드
     */
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {
        itemService.updateItem(form.getId(), form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}

