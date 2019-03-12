package com.example.demo.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private Whitelist whitelist = Whitelist.relaxed();
	private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
	private boolean isUpload = false;

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		String contentType = request.getContentType();
		if (null != contentType && contentType.indexOf("multipart/form-data") != -1) {
			isUpload = true;
		}

		whitelist.addAttributes("a", "style", "class").addAttributes("b", "style", "class")
				.addAttributes("blockquote", "style", "class").addAttributes("br", "style", "class")
				.addAttributes("caption", "style", "class").addAttributes("cite", "style", "class")
				.addAttributes("code", "style", "class").addAttributes("col", "style", "class")
				.addAttributes("colgroup", "style", "class").addAttributes("dd", "style", "class")
				.addAttributes("div", "style", "class").addAttributes("dl", "style", "class")
				.addAttributes("dt", "style", "class").addAttributes("em", "style", "class")
				.addAttributes("h1", "style", "class").addAttributes("h2", "style", "class")
				.addAttributes("h3", "style", "class").addAttributes("h4", "style", "class")
				.addAttributes("h5", "style", "class").addAttributes("h6", "style", "class")
				.addAttributes("i", "style", "class").addAttributes("img", "style", "class")
				.addAttributes("li", "style", "class").addAttributes("ol", "style", "class")
				.addAttributes("p", "style", "class").addAttributes("pre", "style", "class")
				.addAttributes("q", "style", "class").addAttributes("small", "style", "class")
				.addAttributes("span", "style", "class").addAttributes("strike", "style", "class")
				.addAttributes("strong", "style", "class").addAttributes("sub", "style", "class")
				.addAttributes("sup", "style", "class").addAttributes("table", "style", "class")
				.addAttributes("tbody", "style", "class").addAttributes("td", "style", "class")
				.addAttributes("tfoot", "style", "class").addAttributes("th", "style", "class")
				.addAttributes("thead", "style", "class").addAttributes("tr", "style", "class")
				.addAttributes("u", "style", "class").addAttributes("ul", "style", "class");
	}

	@Override
	public String getHeader(String name) {
		return clean(super.getHeader(name));
	}

	@Override
	public String getQueryString() {
		return clean(super.getQueryString());
	}

	@Override
	public String getParameter(String name) {
		return clean(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			int length = values.length;
			String[] escapseValues = new String[length];
			for (int i = 0; i < length; i++) {
				escapseValues[i] = clean(values[i]);
			}
			return escapseValues;
		}
		return super.getParameterValues(name);
	}

	@Override
	public ServletInputStream getInputStream() {
		try {
			if (isUpload) {
				return super.getInputStream();
			} else {
				final ByteArrayInputStream inputStream = new ByteArrayInputStream(
						inputHandlers(super.getInputStream()).getBytes("utf-8"));
				return new ServletInputStream() {
					@Override
					public int read() throws IOException {
						return inputStream.read();
					}

					@Override
					public boolean isFinished() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public boolean isReady() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void setReadListener(ReadListener listener) {
						// TODO Auto-generated method stub

					}

				};
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public String inputHandlers(ServletInputStream servletInputStream) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(servletInputStream, Charset.forName("UTF-8")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (servletInputStream != null) {
				try {
					servletInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return clean(sb.toString());
	}

	public String clean(String content) {
		if (content == null || content.trim().length() == 0) {
			return content;
		}
		// jsoup 属性解析bug
		content = content.replace("text-align: ", "text-align:");
		return Jsoup.clean(content, "", whitelist, outputSettings);
	}
}