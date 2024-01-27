package com.t.logic.common.Job;

import com.t.logic.config.CmdConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Callable;

@AllArgsConstructor
@Slf4j
public class DescCallable implements Callable<Boolean> {
	Long id;
	String file_name;
	Integer page;

	@Override
	public Boolean call() throws Exception {
		Process process = Runtime.getRuntime()
				.exec(CmdConfig.create().toString(file_name,page));
		// TODO: 2023/3/31 Cmd进程 暂停
		//启动读取buffer缓冲区子线程,避免buffer缓冲区被写满,导致线程等待问题
		BufferedReader normalReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
		BufferedReader errorReader = new BufferedReader(
				new InputStreamReader(process.getErrorStream()));

		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = normalReader.readLine()) != null) {
			sb.append(line);
		}
		String errorLine;
		boolean errorFlag=false;
		while ((errorLine = errorReader.readLine()) != null) {
			log.warn("脚本文件执行信息ErrorStream:{}", errorLine);
			errorFlag=true;
		}
		// 等待程序执行结束并输出状态
		int exitCode = process.waitFor();

		if (errorFlag)
		{
			log.info(file_name+"_"+page+" predict failed");
			return false;
		}
		log.info(file_name+"_"+page+" predict success");
		return true;
	}
}
