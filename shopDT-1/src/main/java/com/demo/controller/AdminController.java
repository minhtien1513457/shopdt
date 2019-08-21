package com.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entity.LoaiSp;
import com.demo.service.LoaiSpService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {

    @Autowired
    private LoaiSpService loaiSpService;

    //api loại SP---------------------------------------------------------------------------------
	@RequestMapping(value = "/loaiSp", method = RequestMethod.GET)
	public String listLoaiSp(Model model) {
        model.addAttribute("loaiSps", loaiSpService.findAll());
        return "listLoaiSp";
    }

	@RequestMapping(value = "/loaiSp/search", method = RequestMethod.GET)
    public String searchLoaiSp(@RequestParam("term") String term, Model model) {
        if (StringUtils.isEmpty(term)) {
            return "redirect:/admin/loaiSp";
        }
        model.addAttribute("loaiSps", loaiSpService.search(term));
        return "listLoaiSp";
    }

	@RequestMapping(value = "/loaiSp/add", method = RequestMethod.GET)
    public String addLoaiSp(Model model) {
        model.addAttribute("loaiSp", new LoaiSp());
        return "formLoaiSp";
    }

	@RequestMapping(value = "/loaiSp/{id}/edit", method = RequestMethod.GET)
    public String editLoaiSp(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("loaiSp", loaiSpService.findOne(id));
        return "formLoaiSp";
    }

	@RequestMapping(value = "/loaiSp/save", method = RequestMethod.POST)
    public String saveLoaiSp(@Valid LoaiSp loaiSp, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "formLoaiSp";
        }
        loaiSpService.save(loaiSp);
        redirect.addFlashAttribute("successMessage", "Saved loaiSp successfully!");
        return "redirect:/admin/loaiSp";
    }

	@RequestMapping(value = "/loaiSp/{id}/delete", method = RequestMethod.GET)
    public String deleteLoaiSp(@PathVariable int id, RedirectAttributes redirect) {
    	loaiSpService.delete(id);
        redirect.addFlashAttribute("successMessage", "Deleted loai SP successfully!");
        return "redirect:/admin/loaiSp";
    }
	
	//api SP---------------------------------------------------------------------------------
	@RequestMapping(value = "/sanpham", method = RequestMethod.GET)
	public String list(Model model) {
        model.addAttribute("loaiSps", loaiSpService.findAll());
        return "listSanpham";
    }

	@RequestMapping(value = "/sanpham/search", method = RequestMethod.GET)
    public String search(@RequestParam("term") String term, Model model) {
        if (StringUtils.isEmpty(term)) {
            return "redirect:/admin/sanpham";
        }
        model.addAttribute("loaiSps", loaiSpService.search(term));
        return "listLoaiSp";
    }

	@RequestMapping(value = "/sanpham/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("loaiSp", new LoaiSp());
        return "form";
    }

	@RequestMapping(value = "/sanpham/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("loaiSp", loaiSpService.findOne(id));
        return "form";
    }

	@RequestMapping(value = "/sanpham/save", method = RequestMethod.POST)
    public String save(@Valid LoaiSp loaiSp, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "form";
        }
        loaiSpService.save(loaiSp);
        redirect.addFlashAttribute("successMessage", "Saved sanpham successfully!");
        return "redirect:/admin/sanpham";
    }

	@RequestMapping(value = "/sanpham/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable int id, RedirectAttributes redirect) {
    	loaiSpService.delete(id);
        redirect.addFlashAttribute("successMessage", "Deleted loai SP successfully!");
        return "redirect:/admin/sanpham";
    }

}
