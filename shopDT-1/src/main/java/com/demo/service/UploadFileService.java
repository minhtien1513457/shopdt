package com.demo.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.demo.entity.UploadFile;

public interface UploadFileService {
	List<File> doUpload(HttpServletRequest request, UploadFile myUploadForm);
}
