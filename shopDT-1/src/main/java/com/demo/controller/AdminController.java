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
import com.demo.entity.Sanpham;
import com.demo.service.ChitietSpService;
import com.demo.service.LoaiSpService;
import com.demo.service.SanphamService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {

    @Autowired
    private LoaiSpService loaiSpService;
    
    @Autowired
    private SanphamService sanphamService;
    
    @Autowired
    private ChitietSpService chitietSpService;

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
        model.addAttribute("sanphams", sanphamService.findAll());
        return "listSanpham";
    }

	@RequestMapping(value = "/sanpham/search", method = RequestMethod.GET)
    public String search(@RequestParam("term") String term, Model model) {
        if (StringUtils.isEmpty(term)) {
            return "redirect:/admin/sanpham";
        }
        model.addAttribute("sanphams", sanphamService.search(term));
        return "listSanpham";
    }

	@RequestMapping(value = "/sanpham/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("sanpham", new Sanpham());
        model.addAttribute("loaiSps", loaiSpService.findAll());
        return "formSanpham";
    }

	@RequestMapping(value = "/sanpham/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("sanpham", sanphamService.findOne(id));
        return "formSanpham";
    }

	@RequestMapping(value = "/sanpham/save", method = RequestMethod.POST)
    public String save(@Valid Sanpham sanpham, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "formSanpham";
        }
        sanphamService.save(sanpham);
        redirect.addFlashAttribute("successMessage", "Saved sanpham successfully!");
        return "redirect:/admin/sanpham";
    }

	@RequestMapping(value = "/sanpham/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable int id, RedirectAttributes redirect) {
    	loaiSpService.delete(id);
        redirect.addFlashAttribute("successMessage", "Deleted loai SP successfully!");
        return "redirect:/admin/sanpham";
    }
	
	//api chi tiet san pham
	//api SP---------------------------------------------------------------------------------
		@RequestMapping(value = "/chitietSp", method = RequestMethod.GET)
		public String listChitietSp(Model model) {
	        model.addAttribute("chitietSps", chitietSpService.findAll());
	        return "listChitietSp";
	    }

		@RequestMapping(value = "/chitietSp/search", method = RequestMethod.GET)
	    public String searchChitietSp(@RequestParam("term") String term, Model model) {
	        if (StringUtils.isEmpty(term)) {
	            return "redirect:/admin/chitietSp";
	        }
	        model.addAttribute("chitietSps", chitietSpService.search(term));
	        return "listChitietSp";
	    }

		@RequestMapping(value = "/chitietSp/add", method = RequestMethod.GET)
	    public String addChitietSp(Model model) {
	        model.addAttribute("sanpham", new Sanpham());
	        model.addAttribute("loaiSps", loaiSpService.findAll());
	        return "formChitietSp";
	    }

		@RequestMapping(value = "/chitietSp/{id}/edit", method = RequestMethod.GET)
	    public String editChitietSp(@PathVariable("id") Integer id, Model model) {
	        model.addAttribute("chitietSp", loaiSpService.findOne(id));
	        return "formChitietSp";
	    }

		@RequestMapping(value = "/chitietSp/save", method = RequestMethod.POST)
	    public String saveChitietSp(@Valid Sanpham sanpham, BindingResult result, RedirectAttributes redirect) {
	        if (result.hasErrors()) {
	            return "formChitietSp";
	        }
	        sanphamService.save(sanpham);
	        redirect.addFlashAttribute("successMessage", "Saved sanpham successfully!");
	        return "redirect:/admin/chitietSp";
	    }

		@RequestMapping(value = "/chitietSp/{id}/delete", method = RequestMethod.GET)
	    public String deleteChitietSp(@PathVariable int id, RedirectAttributes redirect) {
	    	loaiSpService.delete(id);
	        redirect.addFlashAttribute("successMessage", "Deleted loai SP successfully!");
	        return "redirect:/admin/chitietSp";
	    }

}
