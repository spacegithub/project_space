package com.mr.data.config;

import com.mr.data.common.OCRUtil;
import com.mr.framework.core.date.DateUtil;
import com.mr.framework.core.io.FileUtil;
import com.mr.framework.core.util.CharsetUtil;
import com.mr.framework.cron.CronUtil;
import com.mr.framework.cron.task.Task;
import com.mr.framework.log.Log;
import com.mr.framework.log.LogFactory;
import com.mr.data.modules.api.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by feng on 18-5-14
 */
@Configuration
public class CronJob {
	private static Log log = LogFactory.get();

	@Value("${spring.mail.cron}")
	private String pattern;

	private String testPattern = "*/1 * * * *";

	@Autowired
	private SiteService siteService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String from;

	@Bean
	public Void startJob() {
		System.out.println("----===="+pattern);
		//每天4:45 run task
		CronUtil.schedule(pattern, new Task() {
			@Override
			public void execute() {
				log.info("Task excuted.");
				try {

				} catch (Exception e) {
					log.warn(e.getMessage());
					log.warn("定时日报邮件异常");
				}
			}
		});
		CronUtil.start();
		log.info("Cron Task start.");

		return null;
	}

	private void sendSimpleMail(String[] to, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);

		try {
			mailSender.send(message);
			log.info("简单邮件已经发送。");
		} catch (Exception e) {
			log.warn("发送简单邮件时发生异常！ " + e.getMessage());
		}
	}

	private void clearLog(String logPath){
		if(!FileUtil.exist(logPath)) return;
		BufferedWriter bw = FileUtil.getWriter(logPath, "utf-8", false);
		try {
			bw.write("");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (!Objects.isNull(bw)) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
