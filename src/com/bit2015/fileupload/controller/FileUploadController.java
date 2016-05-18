package com.bit2015.fileupload.controller;

import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	private static final Log LOG = LogFactory.getLog(FileUploadController.class);
	// SAVE_PATH 저장위치 ; 리눅스위치기준인듯
	// window에서는 c가 붙음(eclipse 사용하기위함)
	private static final String SAVE_PATH = "/temp";

	@RequestMapping("/form")
	public String form() {
		return "form";
	}

	@RequestMapping("/upload")
	public String upload(@RequestParam String email, @RequestParam("file1") MultipartFile file1, Model model) {
		// @ModelAttribute BoardVo vo -> 이게 될까? 안되면 multipart에 넣어야하고; 그럼 지저분하고;
		// 하지만된다
		// 보통 파라미터의 값 ;그냥 남겨봄
		LOG.debug(" ######## email : " + email);

		// 첫 번째 파일 처리
		if (file1.isEmpty() == false) {

			// file이름을 보통 original file명으로 하지 않음 --> 관리하기 위한 특별한 이름으로 보통 만드는데,
			// 날짜+시간+확장자로 보통 하는데, 그게 관리하기 편함
			// linux 등에 한글이름 등으로 저장하는 거도 별로임
			String fileOriginalName = file1.getOriginalFilename();

			// 마지막 .을찾아서 ! last.부터는 확장자를 의미하겠지!
			String extName = fileOriginalName.substring(fileOriginalName.lastIndexOf(".") + 1,
					fileOriginalName.length());

			// 시스템에 유일한 정보로 만들어야 좋음! -> 그래서 시간이 보통 확실하지
			String saveFileName = genSaveFileName(extName);
			Long size = file1.getSize();

			writeFile(file1, SAVE_PATH, saveFileName);

			String url = "/product-images/" + saveFileName;
			model.addAttribute("productImageUrl1", url);

			LOG.debug(" ######## fileOriginalName : " + fileOriginalName);
			LOG.debug(" ######## fileSize : " + size);
			LOG.debug(" ######## fileExtensionName : " + extName);
			LOG.debug(" ######## saveFileName : " + saveFileName);
		}

		return "result";
	}

	private void writeFile(MultipartFile file, String path, String fileName) {
		FileOutputStream fos = null;
		try {
			byte fileData[] = file.getBytes();
			fos = new FileOutputStream(path + "/" + fileName);
			fos.write(fileData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private String genSaveFileName(String extName) {

		Calendar calendar = Calendar.getInstance();
		String fileName = "";

		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += ("." + extName);

		return fileName;
	}
}
