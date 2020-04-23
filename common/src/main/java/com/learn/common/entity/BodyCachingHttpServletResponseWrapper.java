package com.learn.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title BodyCachingHttpServletResponseWrapper
 * @package com.learn.common.entity
 * @description 返回体包装类
 * @date 2020/4/23 9:25 上午
 */
public class BodyCachingHttpServletResponseWrapper extends HttpServletResponseWrapper {
	
	private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	private final HttpServletResponse response;
	
	/**
	 * Constructs a response adaptor wrapping the given response.
	 *
	 * @param response The response to be wrapped
	 * @throws IllegalArgumentException if the response is null
	 */
	public BodyCachingHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		this.response = response;
	}
	
	public byte[] getBody() {
		return byteArrayOutputStream.toByteArray();
	}
	
	public String getBodyString() {
		return  byteArrayOutputStream.toString();
	}
	
	@Override
	public ServletOutputStream getOutputStream() {
		return new ServletOutputStreamWrapper(this.byteArrayOutputStream, this.response);
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(new OutputStreamWriter(this.byteArrayOutputStream, this.response.getCharacterEncoding()));
	}
	
	@EqualsAndHashCode(callSuper = true)
	@Data
	@AllArgsConstructor
	private static class ServletOutputStreamWrapper extends ServletOutputStream {
		
		private ByteArrayOutputStream outputStream;
		private HttpServletResponse response;
		
		@Override
		public boolean isReady() {
			return true;
		}
		
		@Override
		public void setWriteListener(WriteListener listener) {
		}
		
		@Override
		public void write(int b) throws IOException {
			this.outputStream.write(b);
		}
		
		@Override
		public void flush() throws IOException {
			if (!this.response.isCommitted()) {
				byte[] body = this.outputStream.toByteArray();
				ServletOutputStream outputStream = this.response.getOutputStream();
				outputStream.write(body);
				outputStream.flush();
			}
		}
		
	}
}
