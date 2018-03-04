package com.example.demo;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.AWSStaticCredentialsProvider;



@Controller
public class ProfileController {

	@GetMapping(value = "/")
	public ModelAndView Startpage() {

		ModelAndView indexPage = new ModelAndView();
		indexPage.setViewName("index");
		return indexPage;
	}

	@PostMapping(value="/upload")
	public ModelAndView uploadtoS3(
			@RequestParam("file") MultipartFile image
			) {
		ModelAndView profilePage = new ModelAndView();

		//Please Replace <AWS access_key_id> & <AWS secret_key_id> with relevent keys.
		BasicAWSCredentials credentials = new BasicAWSCredentials("<AWS access_key_id>","<AWS secret_key_id>");
		AmazonS3 s3clt = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_2)
				.build();
		try {
		PutObjectRequest putobreq = new PutObjectRequest("rutujabucket1",image.getOriginalFilename(),image.getInputStream(), new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead);

		s3clt.putObject(putobreq);

			String imgsrc1 = "http://" + "rutujabucket1" + ".s3.amazonaws.com/"+image.getOriginalFilename();
			profilePage.addObject("imgsrc", imgsrc1);
			profilePage.setViewName("profile");
			return profilePage;


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			profilePage.setViewName("error");
			return profilePage;

		}
	}

}
