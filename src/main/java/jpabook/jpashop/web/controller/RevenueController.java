package jpabook.jpashop.web.controller;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.RevenueSearch;
import jpabook.jpashop.service.OrderService;
import jpabook.jpashop.service.RevenueDto;
import jpabook.jpashop.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping(value = "/revenue")
    public String revenueList(@ModelAttribute("revenueSearch") RevenueSearch revenueSearch, Model model) {
        List<RevenueDto> sales = revenueService.findRevenue(revenueSearch);
        model.addAttribute("sales", sales);
        return "revenue/revenueList";
    }

}
