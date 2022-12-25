package jpabook.jpashop.controller;


import jakarta.validation.Valid;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.BookForm;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("bookForm", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("form") @Valid BookForm form, BindingResult result) {
        if (result.hasErrors()) return "items/createItemForm";
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());
        book.setAuthor(form.getAuthor());
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", itemService.findItems());
        return "items/itemList";
    }

    @GetMapping("{itemId}/edit")
    public String editForm(Model model, @PathVariable Long itemId) {
        Book book = (Book) itemService.findOne(itemId);
        BookForm form = BookForm.builder()
                .id(book.getId())
                .price(book.getPrice())
                .author(book.getAuthor())
                .name(book.getName())
                .stockQuantity(book.getStockQuantity())
                .isbn(book.getIsbn())
                .build();


        model.addAttribute("form", form);
        return "items/editForm";
    }

    @PatchMapping("{itemId}/edit")
    public String edit(@ModelAttribute("form") @Valid BookForm form, BindingResult result) {
        if (result.hasErrors()) return "items/editForm";
        Book book = (Book) itemService.findOne(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());
        book.setAuthor(form.getAuthor());
        return "redirect:/items";
    }
}
