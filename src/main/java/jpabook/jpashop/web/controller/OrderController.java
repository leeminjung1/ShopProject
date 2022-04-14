package jpabook.jpashop.web.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import jpabook.jpashop.web.form.MemberForm;
import jpabook.jpashop.web.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value = "/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();
        model.addAttribute("members", members);
        model.addAttribute("items", items);

        model.addAttribute("orderForm", new OrderForm());

        return "order/orderForm";
    }

    @PostMapping(value = "/order")
    public String order(@ModelAttribute OrderForm form, BindingResult result, Model model) {
        try {
            orderService.order(form.getMemberId(), form.getItemId(), form.getCount());
            return "redirect:/orders";
        } catch (Exception e) {
            result.addError(new FieldError("orderForm", "count", e.getMessage()));
            model.addAttribute("members", memberService.findMembers());
            model.addAttribute("items", itemService.findItems());
            return "order/orderForm";
        }
    }

    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping(value = "/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }
}
